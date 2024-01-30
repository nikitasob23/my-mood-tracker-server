package com.niksob.database_service.dao.user.cached;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.resource.ResourceAlreadyExistsException;
import com.niksob.database_service.exception.resource.ResourceSavingException;
import com.niksob.database_service.exception.resource.ResourceDeletionException;
import com.niksob.database_service.exception.resource.ResourceNotFoundException;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public abstract class CachedUserEntityDao implements UserEntityDao, CacheCleaner {
    public static final String USER_CACHE_ENTITY_NAME = "users";
    private final Cache cache;

    protected final UserRepository userRepository;
    protected final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityDao.class);

    @Override
    @Cacheable(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#username")
    public UserEntity load(String username) {
        log.debug("Start loading user entity by username from repository", username);

        final UserEntity userEntity;
        try {
            userEntity = userRepository.getByUsername(username);
        } catch (Exception e) {
            throw createResourceNotFoundException(username, e);
        }
        if (userEntity == null) {
            throw createResourceNotFoundException(username);
        }
        log.debug("User entity loaded from repository", userEntity);
        log.debug("Cached user entity", userEntity);
        return userEntity;
    }

    @Override
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity save(UserEntity userEntity) {
        log.debug("Start saving user to repository", userEntity);
        if (userRepository.existsByUsername(userEntity.getUsername())) {
            var existsException = new ResourceAlreadyExistsException("User already exists", null, userEntity.getUsername());
            log.error("Failed saving user to repository", null, userEntity);
            throw existsException;
        }
        try {
            addDbReferences(userEntity);
            final UserEntity saved = userRepository.save(userEntity);
            log.debug("User entity saved", userEntity);
            log.debug("User entity cache updated", userEntity);
            return saved;
        } catch (Exception e) {
            var savingException = new ResourceSavingException("User has not saved", userEntity.getUsername(), e);
            log.error("Failed saving user to repository", e, userEntity);
            throw savingException;
        }
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.debug("Start deleting user by username from repository", username);
        if (!userRepository.existsByUsername(username)) {
            throw createResourceNotFoundException(username);
        }
        try {
            cache.evict(username);
            userRepository.deleteByUsername(username);
            log.debug("User deleted from repository", username);
            log.debug("Deleted user cache", username);
        } catch (Exception e) {
            var deletionException = new ResourceDeletionException("The user was not deleted", e, username);
            log.error("Failed deleting user by username from repository", deletionException, username);
            throw deletionException;
        }
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    protected void addDbReferences(UserEntity userEntity) {
        final List<MoodTagEntity> allMoodTags = userEntity.getMoodEntries().stream()
                .flatMap(moodEntry -> moodEntry.getMoodTags().stream())
                .toList();

        final Map<String, MoodTagEntity> combinedMoodTagMap = allMoodTags.stream()
                .collect(Collectors.toMap(MoodTagEntity::getName, moodTagEntity -> moodTagEntity,
                        (existing, replacement) -> {
                            final Set<MoodEntryEntity> combinedMoodEntries = Stream.concat(
                                    existing.getMoodEntries().stream(),
                                    replacement.getMoodEntries().stream()
                            ).collect(Collectors.toSet());

                            final MoodTagEntity newMoodTag = new MoodTagEntity(existing);
                            newMoodTag.setMoodEntries(combinedMoodEntries);
                            return newMoodTag;
                        }));
        userEntity.getMoodEntries()
                .forEach(moodEntry -> {
                    final Set<MoodTagEntity> newCombinedMoodTags = moodEntry.getMoodTags().stream()
                            .map(moodTag -> combinedMoodTagMap.get(moodTag.getName()))
                            .collect(Collectors.toSet());
                    moodEntry.setMoodTags(newCombinedMoodTags);
                });
        userEntity.setMoodTags(new HashSet<>(combinedMoodTagMap.values()));
    }

    protected ResourceNotFoundException createResourceNotFoundException(String username) {
        return createResourceNotFoundException(username, null);
    }

    private ResourceNotFoundException createResourceNotFoundException(String username, Exception e) {
        var notFoundException = new ResourceNotFoundException("The user was not found", e, username);
        log.error("Failed getting user by username from repository", notFoundException, username);
        return notFoundException;
    }
}
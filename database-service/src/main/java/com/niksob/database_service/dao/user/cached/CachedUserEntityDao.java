package com.niksob.database_service.dao.user.cached;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.entity.EntityNotDeletedException;
import com.niksob.database_service.exception.entity.EntitySavingException;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
        final UserEntity userEntity = userRepository.getByUsername(username);
        log.debug("User entity loaded from repository", userEntity == null ? "-" : userEntity);
        log.debug("Cached user entity", userEntity == null ? "-" : userEntity);
        return userEntity;
    }

    @Override
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity save(UserEntity userEntity) {
        log.debug("Saving user info", userEntity);

        mergeMoodTagsWithSameNameInEntities(userEntity);
        try {
            return Stream.of(userEntity)
                    .peek(userRepository::save)
                    .peek(u -> log.debug("User entity saved", u))
                    .peek(u -> log.debug("User entity cache updated", u))
                    .findFirst().get();
        } catch (Exception e) {
            final EntitySavingException entitySavingException = new EntitySavingException(userEntity.getUsername(), e);
            log.error("User entity has not been saved", e, userEntity);
            throw entitySavingException;
        }
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.debug("Start deleting user entity by username from repository", username);
        try {
            Stream.of(username)
                    .peek(cache::evict)
                    .peek(userRepository::deleteUserEntityByUsername)
                    .peek(u -> log.debug("User entity deleted from repository", u))
                    .forEach(u -> log.debug("Deleted user entity cache", u));
        } catch (Exception e) {
            final EntityNotDeletedException entityNotDeletedException =
                    new EntityNotDeletedException("User entity not delete by username", username);
            log.error("Failed deleting user by username from repository", e, username);
            throw entityNotDeletedException;
        }
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    private void mergeMoodTagsWithSameNameInEntities(UserEntity userEntity) {
        final List<MoodTagEntity> allMoodTags = userEntity.getMoodEntries().stream()
                .flatMap(moodEntry -> moodEntry.getMoodTags().stream())
                .toList();

        if (allMoodTags.stream()
                .map(MoodTagEntity::getId)
                .allMatch(Objects::nonNull)) {
            return;
        }
        final Map<String, MoodTagEntity> combinedMoodEntryMap = allMoodTags.stream()
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
                            .map(moodTag -> combinedMoodEntryMap.get(moodTag.getName()))
                            .collect(Collectors.toSet());
                    moodEntry.setMoodTags(newCombinedMoodTags);
                });
    }
}
package com.niksob.database_service.dao.mood.tag.facade;

import com.niksob.database_service.dao.mood.tag.loader.TagEntityLoaderDao;
import com.niksob.database_service.dao.mood.tag.updater.TagEntityUpdaterDao;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CachedTagDaoFacade implements TagEntityDaoFacade {
    private final TagEntityLoaderDao loaderDao;
    private final TagEntityUpdaterDao updaterDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedTagDaoFacade.class);

    @Override
    public Set<MoodTagEntity> loadByUserId(Long userId) {
        final UserMoodTagEntities stored = loaderDao.loadByUserId(userId);
        return stored.getTags();
    }

    @Override
    public MoodTagEntity save(MoodTagEntity tag) {
        final UserMoodTagEntities stored = updaterDao.save(tag);
        return stored.getTags().stream()
                .filter(tag::equals)
                .findFirst().orElse(null);
    }

    @Override
    public void update(MoodTagEntity tag) {
        updaterDao.update(tag);
    }

    @Override
    @Transactional
    public Set<MoodTagEntity> mergeAll(Set<MoodTagEntity> tags) {
        final Long userId = extractSingleUserId(tags);
        final Set<Long> storedIds = loadByUserId(userId).stream()
                .map(MoodTagEntity::getId)
                .collect(Collectors.toSet());

        final Set<Long> tagIds = tags.stream()
                .map(MoodTagEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (Long tagId : tagIds) {
            if (!storedIds.contains(tagId)) {
                final String message = "The tag that needs to be updated was not found";
                log.error(message, null, tagId);
                throw new ResourceSavingException(message, tagId, null);
            }
        }
        final UserMoodTagEntities userMoodTagEntities = updaterDao.mergeAll(tags);
        return userMoodTagEntities.getTags();
    }

    @Override
    public void deleteById(MoodTagEntity tag) {
        updaterDao.deleteById(tag);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        updaterDao.deleteAllByUserId(userId);
    }

    private Long extractSingleUserId(Collection<MoodTagEntity> moodTags) {
        final Set<Long> userIds = moodTags.stream()
                .map(MoodTagEntity::getUserId)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException(
                    "Mood tag entities not merged because they haven't ids", null, userIds);
        }
        if (userIds.size() > 1) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException(
                    "Mood tag entities not merged because they have different ids", null, userIds);
        }
        return userIds.iterator().next();
    }
}

package com.niksob.database_service.dao.mood.tag.facade;

import com.niksob.database_service.dao.mood.tag.loader.TagEntityLoaderDao;
import com.niksob.database_service.dao.mood.tag.updater.TagEntityUpdaterDao;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class CachedTagDaoFacade implements TagEntityDaoFacade {
    private final TagEntityLoaderDao loaderDao;
    private final TagEntityUpdaterDao updaterDao;

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
    public Set<MoodTagEntity> mergeAll(Set<MoodTagEntity> tags) {
        final UserMoodTagEntities stored = updaterDao.mergeAll(tags);
        return stored.getTags();
    }

    @Override
    public void deleteById(MoodTagEntity tag) {
        updaterDao.deleteById(tag);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        updaterDao.deleteAllByUserId(userId);
    }
}

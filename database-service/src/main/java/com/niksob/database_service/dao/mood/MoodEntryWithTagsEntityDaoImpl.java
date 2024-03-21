package com.niksob.database_service.dao.mood;

import com.niksob.database_service.dao.mood.entry.MoodEntryEntityDaoImpl;
import com.niksob.database_service.dao.mood.tag.updater.TagEntityUpdaterDao;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;
import com.niksob.database_service.repository.mood.entry.MoodEntryEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MoodEntryWithTagsEntityDaoImpl extends MoodEntryEntityDaoImpl implements MoodEntryWithTagsEntityDao {
    private final TagEntityUpdaterDao tagUpdaterDao;

    public MoodEntryWithTagsEntityDaoImpl(TagEntityUpdaterDao tagUpdaterDao, MoodEntryEntityRepository moodEntryRepository) {
        super(moodEntryRepository);
        this.tagUpdaterDao = tagUpdaterDao;
    }

    @Override
    @Transactional
    public MoodEntryEntity saveEntryWithTags(MoodEntryEntity moodEntry) {
        final UserMoodTagEntities userMergedTags = tagUpdaterDao.mergeAll(moodEntry.getMoodTags());
        moodEntry.setMoodTags(userMergedTags.getTags());
        return super.save(moodEntry);
    }

    @Override
    @Transactional
    public void updateEntryWithTags(MoodEntryEntity moodEntry) {
        final UserMoodTagEntities userMergedTags = tagUpdaterDao.mergeAll(moodEntry.getMoodTags());
        moodEntry.setMoodTags(userMergedTags.getTags());
        super.update(moodEntry);
    }
}

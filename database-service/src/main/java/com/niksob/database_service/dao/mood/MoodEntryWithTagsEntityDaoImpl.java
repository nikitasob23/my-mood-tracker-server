package com.niksob.database_service.dao.mood;

import com.niksob.database_service.dao.mood.entry.MoodEntryEntityDaoImpl;
import com.niksob.database_service.dao.mood.entry.MoodEntryWithTagsEntityDao;
import com.niksob.database_service.dao.mood.tag.MoodTagEntityDao;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.repository.mood.entry.MoodEntryEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class MoodEntryWithTagsEntityDaoImpl extends MoodEntryEntityDaoImpl implements MoodEntryWithTagsEntityDao {
    private final MoodTagEntityDao moodTagDao;

    public MoodEntryWithTagsEntityDaoImpl(MoodTagEntityDao moodTagDao, MoodEntryEntityRepository moodEntryRepository) {
        super(moodEntryRepository);
        this.moodTagDao = moodTagDao;
    }

    @Override
    @Transactional
    public MoodEntryEntity saveEntryWithTags(MoodEntryEntity moodEntry) {
        final Set<MoodTagEntity> mergedTags = moodTagDao.mergeAll(moodEntry.getMoodTags());
        moodEntry.setMoodTags(mergedTags);
        return super.save(moodEntry);
    }

    @Override
    @Transactional
    public void updateEntryWithTags(MoodEntryEntity moodEntry) {
        final Set<MoodTagEntity> mergedTags = moodTagDao.mergeAll(moodEntry.getMoodTags());
        moodEntry.setMoodTags(mergedTags);
        super.update(moodEntry);
    }
}

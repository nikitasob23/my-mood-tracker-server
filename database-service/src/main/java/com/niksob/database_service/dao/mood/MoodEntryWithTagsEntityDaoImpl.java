package com.niksob.database_service.dao.mood;

import com.niksob.database_service.dao.mood.entry.MoodEntryEntityDaoImpl;
import com.niksob.database_service.dao.mood.tag.facade.TagEntityDaoFacade;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.repository.mood.entry.MoodEntryEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class MoodEntryWithTagsEntityDaoImpl extends MoodEntryEntityDaoImpl implements MoodEntryWithTagsEntityDao {
    private final TagEntityDaoFacade tagEntityDaoFacade;

    public MoodEntryWithTagsEntityDaoImpl(TagEntityDaoFacade tagEntityDaoFacade, MoodEntryEntityRepository moodEntryRepository) {
        super(moodEntryRepository);
        this.tagEntityDaoFacade = tagEntityDaoFacade;
    }

    @Override
    @Transactional
    public MoodEntryEntity saveEntryWithTags(MoodEntryEntity moodEntry) {
        final Set<MoodTagEntity> moodTags = tagEntityDaoFacade.mergeAll(moodEntry.getMoodTags());
        moodEntry.setMoodTags(moodTags);
        return super.save(moodEntry);
    }

    @Override
    @Transactional
    public void updateEntryWithTags(MoodEntryEntity moodEntry) {
        final Set<MoodTagEntity> moodTags = tagEntityDaoFacade.mergeAll(moodEntry.getMoodTags());
        moodEntry.setMoodTags(moodTags);
        super.update(moodEntry);
    }
}

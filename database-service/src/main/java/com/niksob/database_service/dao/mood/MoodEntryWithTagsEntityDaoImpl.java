package com.niksob.database_service.dao.mood;

import com.niksob.database_service.dao.mood.entry.MoodEntryEntityDaoImpl;
import com.niksob.database_service.dao.mood.tag.facade.TagEntityDaoFacade;
import com.niksob.database_service.entity.mood.entry.MoodEntryEntity;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.repository.mood.entry.MoodEntryEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MoodEntryWithTagsEntityDaoImpl extends MoodEntryEntityDaoImpl implements MoodEntryWithTagsEntityDao {
    private final TagEntityDaoFacade tagEntityDaoFacade;

    public MoodEntryWithTagsEntityDaoImpl(
            TagEntityDaoFacade tagEntityDaoFacade, MoodEntryEntityRepository moodEntryRepository
    ) {
        super(moodEntryRepository);
        this.tagEntityDaoFacade = tagEntityDaoFacade;
    }

    @Override
    @Transactional
    public MoodEntryEntity saveEntryWithTags(MoodEntryEntity moodEntry) {
        final Set<MoodTagEntity> allUserTags = tagEntityDaoFacade.mergeAll(moodEntry.getMoodTags());

        final Set<MoodTagEntity> entryTags = filterMoodTagsByEntry(allUserTags, moodEntry);
        moodEntry.setMoodTags(entryTags);
        return super.save(moodEntry);
    }

    @Override
    @Transactional
    public void updateEntryWithTags(MoodEntryEntity moodEntry) {
        final Set<MoodTagEntity> allUserTags = tagEntityDaoFacade.mergeAll(moodEntry.getMoodTags());

        final Set<MoodTagEntity> entryTags = filterMoodTagsByEntry(allUserTags, moodEntry);
        moodEntry.setMoodTags(entryTags);
        super.update(moodEntry);
    }

    private Set<MoodTagEntity> filterMoodTagsByEntry(Set<MoodTagEntity> tags, MoodEntryEntity entry) {
        final Set<String> entryTagNames = entry.getMoodTags().stream()
                .map(MoodTagEntity::getName)
                .collect(Collectors.toSet());
        return tags.stream()
                .filter(mergedTag -> entryTagNames.contains(mergedTag.getName()))
                .collect(Collectors.toSet());
    }
}

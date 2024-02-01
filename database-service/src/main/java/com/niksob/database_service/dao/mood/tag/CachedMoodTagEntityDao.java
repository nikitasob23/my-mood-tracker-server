package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.exception.resource.ResourceAlreadyExistsException;
import com.niksob.database_service.exception.resource.ResourceSavingException;
import com.niksob.database_service.repository.mood.tag.MoodTagRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CachedMoodTagEntityDao implements MoodTagEntityDao {
    public static final String MOOD_TAG_CACHE_ENTITY_NAME = "mood_tags";
    private final MoodTagRepository moodTagRepository;
    private final Cache cache;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedMoodTagEntityDao.class);

    @Override
    @Transactional
    public MoodTagEntity save(MoodTagEntity moodTag) {
        log.debug("Start saving mood tag entity to repository", moodTag);
        if (moodTagRepository.existsByName(moodTag.getName())) {
            var existsException = new ResourceAlreadyExistsException("Mood tag entity already exists", null, moodTag.getName());
            log.error("Failed saving mood tag to repository", null, moodTag);
            throw existsException;
        }
        try {
            moodTagRepository.save(moodTag.getName(), moodTag.getDegree(), moodTag.getUser().getId());
            var saved = moodTagRepository.getByName(moodTag.getName());
            log.debug("Mood tag entity saved", moodTag);
            log.debug("Mood tag entity cache updated", moodTag);
            return saved;
        } catch (Exception e) {
            var savingException = new ResourceSavingException("Mood tag has not saved", moodTag.getName(), e);
            log.error("Failed saving mood tag to repository", e, moodTag);
            throw savingException;
        }
    }
}

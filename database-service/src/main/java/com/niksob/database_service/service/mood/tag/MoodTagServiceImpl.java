package com.niksob.database_service.service.mood.tag;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class MoodTagServiceImpl implements MoodTagService {
    private final MoodTagDao moodTagDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagServiceImpl.class);

    @Override
    public Mono<MoodTag> save(MoodTag moodTag) {
        return MonoAsyncUtil.create(() -> moodTagDao.save(moodTag))
                .doOnNext(ignore -> log.debug("Save mood tag to user DAO", moodTag));
    }
}

package com.niksob.database_service.service.mood.tag;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.mood.tag.MoodTagName;
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
    public Mono<MoodTag> load(MoodTagName name) {
        return Mono.just(name)
                .map(moodTagDao::load)
                .doOnNext(moodTag -> log.debug("Get mood tag from DAO", moodTag));
    }

    @Override
    public Mono<MoodTag> delete(MoodTagName name) {
        return Mono.just(name)
                .filter(this::exists)
                .map(moodTagDao::load)
                .doOnNext(ignore -> moodTagDao.delete(name))
                .doOnNext(loaded -> log.debug("Deleted mood tag from DAO", loaded));
    }

    @Override
    public boolean exists(MoodTagName name) {
        return load(name).blockOptional().isPresent();
    }
}

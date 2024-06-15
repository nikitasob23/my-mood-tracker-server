package com.niksob.database_service.service.mood.tag;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoaderImpl;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MoodTagServiceImpl extends MoodTagLoaderImpl implements MoodTagService {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagServiceImpl.class);

    public MoodTagServiceImpl(UserExistenceService userExistenceService, MoodTagDao moodTagDao) {
        super(userExistenceService, moodTagDao);
    }

    @Override
    public Mono<MoodTag> save(MoodTag moodTag) {
        return userExistenceService.existsOrThrow(moodTag.getUserId())
                .flatMap(user -> MonoAsyncUtil.create(() -> moodTagDao.save(moodTag))
                        .doOnNext(ignore -> log.debug("Save mood tag to DAO", moodTag))
                        .doOnError(throwable -> log.error("Mood tag save error", throwable, moodTag)));
    }

    @Override
    public Mono<Void> update(MoodTag moodTag) {
        return userExistenceService.existsOrThrow(moodTag.getUserId())
                .flatMap(user -> MonoAsyncUtil.create(() -> moodTagDao.update(moodTag))
                        .then()
                        .doOnSuccess(ignore -> log.debug("Update mood tag to DAO", moodTag)));
    }

    @Override
    public Mono<Set<MoodTag>> mergeAll(Set<MoodTag> moodTags) {
        final UserId userId = getSingleUserId(moodTags);
        return userExistenceService.existsOrThrow(userId)
                .flatMap(user -> MonoAsyncUtil.create(() -> moodTagDao.mergeAll(moodTags))
                        .doOnNext(ignore -> log.debug("Merge mood tag to DAO", moodTags))
                        .doOnError(throwable -> log.error("Mood tag merge error", throwable, moodTags)));
    }

    @Override
    public Mono<Void> deleteById(MoodTag moodTag) {
        return userExistenceService.existsOrThrow(moodTag.getUserId())
                .flatMap(user -> MonoAsyncUtil.create(() -> moodTagDao.deleteById(moodTag))
                        .doOnSuccess(ignore -> log.debug("Delete mood tag from DAO", moodTag)));
    }

    private UserId getSingleUserId(Collection<MoodTag> moodTags) {
        final Set<Long> userIds = moodTags.stream()
                .map(MoodTag::getUserId)
                .map(UserId::getValue)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            log.error("Failed merging mood tags in repository", null, moodTags);
            throw new ResourceUpdatingException(
                    "Mood tags not merged because they haven't ids", null, userIds);
        }
        if (userIds.size() > 1) {
            log.error("Failed merging mood tag entities in repository", null, moodTags);
            throw new ResourceUpdatingException(
                    "Mood tag entities not merged because they have different ids", null, userIds);
        }
        final Long id = userIds.iterator().next();
        return new UserId(id);
    }
}

package com.niksob.database_service.controller.mood.tag;

import com.niksob.database_service.util.controller.ResourceControllerErrorUtil;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.path.controller.database_service.mood.tag.MoodTagControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(MoodTagControllerPaths.BASE_URI)
public class MoodTagController {
    private final MoodTagControllerService moodTagControllerService;
    @Qualifier("moodTagControllerUtil")
    private final ResourceControllerErrorUtil controllerUtil;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagController.class);

    @GetMapping
    public Flux<MoodTagDto> loadByUserId(@RequestParam("user_id") UserIdDto userIdDto) {
        return moodTagControllerService.loadByUserId(userIdDto)
                .doOnNext(ignore -> log.debug("Successful mood tag loading", userIdDto))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerUtil::createLoadingErrorFlux)
                .switchIfEmpty(controllerUtil.returnNoContentStatus());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MoodTagDto> save(@RequestBody MoodTagDto moodTagDto) {
        return moodTagControllerService.save(moodTagDto)
                .doOnSuccess(ignore -> log.debug("Successful mood tag saving", moodTagDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(controllerUtil::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody MoodTagDto moodTagDto) {
        return moodTagControllerService.update(moodTagDto)
                .doOnSuccess(ignore -> log.debug("Successful mood tag updating", moodTagDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@RequestBody MoodTagDto tagDto) {
        return moodTagControllerService.deleteById(tagDto)
                .doOnSuccess(ignore -> log.debug("Successful mood tag deletion", tagDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createDeleteError);
    }
}

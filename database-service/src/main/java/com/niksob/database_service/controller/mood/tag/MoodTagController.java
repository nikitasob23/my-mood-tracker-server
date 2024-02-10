package com.niksob.database_service.controller.mood.tag;

import com.niksob.database_service.exception.resource.*;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.path.controller.database_service.mood.tag.MoodTagControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(MoodTagControllerPaths.BASE_URI)
public class MoodTagController {
    private final MoodTagControllerService moodTagControllerService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagController.class);

    @GetMapping
    public Flux<MoodTagDto> loadByUserId(@RequestParam("user_id") UserIdDto userIdDto) {
        return moodTagControllerService.loadByUserId(userIdDto)
                .doOnNext(ignore -> log.debug("Successful mood tag loading", userIdDto))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(throwable -> createLoadingError(throwable, userIdDto));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MoodTagDto> save(@RequestBody MoodTagDto moodTagDto) {
        return moodTagControllerService.save(moodTagDto)
                .doOnSuccess(ignore -> log.debug("Successful mood tag saving", moodTagDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(this::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody MoodTagDto moodTagDto) {
        return moodTagControllerService.update(moodTagDto)
                .doOnSuccess(ignore -> log.debug("Successful mood tag updating", moodTagDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(throwable -> createUpdatingError(throwable, moodTagDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@RequestBody MoodTagDto tagDto) {
        return moodTagControllerService.deleteById(tagDto)
                .doOnSuccess(ignore -> log.debug("Successful mood tag deletion", tagDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(throwable -> createDeleteError(throwable, tagDto));
    }

    private Flux<MoodTagDto> createLoadingError(Throwable throwable, Object state) {
        log.error("Mood tag load error", throwable, state);
        ControllerResponseException errorResponse;
        if (throwable instanceof IllegalUserAccessException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.FORBIDDEN,
                    String.format("%s/%s", contextPath, MoodTagControllerPaths.BASE_URI)
            );
        } else if (throwable instanceof ResourceNotFoundException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.NOT_FOUND,
                    String.format("%s/%s", contextPath, MoodTagControllerPaths.BASE_URI)
            );
        } else {
            log.error("Controller returning failed status", null, HttpStatus.INTERNAL_SERVER_ERROR);
            return Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", null, errorResponse);
        return Flux.error(errorResponse);
    }

    private Mono<MoodTagDto> createSavingError(Throwable throwable) {
        ControllerResponseException errorResponse;
        if (throwable instanceof ResourceSavingException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, MoodTagControllerPaths.BASE_URI)
            );
        } else if (throwable instanceof ResourceAlreadyExistsException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.CONFLICT,
                    String.format("%s/%s", contextPath, MoodTagControllerPaths.BASE_URI)
            );
        } else {
            log.error("Controller returning failed status", throwable, HttpStatus.INTERNAL_SERVER_ERROR);
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", throwable, errorResponse);
        return Mono.error(errorResponse);
    }

    private Mono<Void> createUpdatingError(Throwable throwable, Object state) {
        log.error("Mood tag update error", throwable, state);
        if (throwable instanceof ResourceUpdatingException) {
            var errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, MoodTagControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingError(throwable, state).then();
    }

    private Mono<Void> createDeleteError(Throwable throwable, Object state) {
        log.error("Failed to delete mood tag", throwable, state);
        if (throwable instanceof ResourceDeletionException) {
            final ControllerResponseException errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, MoodTagControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingError(throwable, state).then();
    }
}

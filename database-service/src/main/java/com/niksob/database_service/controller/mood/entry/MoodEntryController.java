package com.niksob.database_service.controller.mood.entry;

import com.niksob.database_service.exception.resource.*;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.MoodEntryIdDto;
import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.path.controller.database_service.mood.entry.MoodEntryControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(MoodEntryControllerPaths.BASE_URI)
public class MoodEntryController {
    private final MoodEntryControllerService moodEntryControllerService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryController.class);

    @GetMapping
    public Flux<MoodEntryDto> loadByDateRange(
            @RequestParam("user_id") Long userId,
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date") LocalDate endDate
    ) {
        final UserEntryDateRangeDto userEntryDateRangeDto = new UserEntryDateRangeDto(userId, startDate, endDate);
        return moodEntryControllerService.loadByDateRange(userEntryDateRangeDto)
                .doOnNext(ignore -> log.debug("Successful mood entries loading", userEntryDateRangeDto))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(this::createLoadingError);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MoodEntryDto> save(@RequestBody MoodEntryDto moodEntryDto) {
        return moodEntryControllerService.save(moodEntryDto)
                .doOnSuccess(ignore -> log.debug("Successful mood entry saving", moodEntryDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(this::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody MoodEntryDto moodEntryDto) {
        return moodEntryControllerService.update(moodEntryDto)
                .doOnSuccess(ignore -> log.debug("Successful mood entry updating", moodEntryDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(this::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@RequestParam("id") MoodEntryIdDto id) {
        return moodEntryControllerService.deleteById(id)
                .doOnSuccess(ignore -> log.debug("Successful mood entry deletion", id))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(this::createDeleteError);
    }

    private Flux<MoodEntryDto> createLoadingError(Throwable throwable) {
        ControllerResponseException errorResponse;
        if (throwable instanceof IllegalUserAccessException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.FORBIDDEN,
                    String.format("%s/%s", contextPath, MoodEntryControllerPaths.BASE_URI)
            );
        } else if (throwable instanceof ResourceNotFoundException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.NOT_FOUND,
                    String.format("%s/%s", contextPath, MoodEntryControllerPaths.BASE_URI)
            );
        } else {
            log.error("Controller returning failed status", null, HttpStatus.INTERNAL_SERVER_ERROR);
            return Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", null, errorResponse);
        return Flux.error(errorResponse);
    }

    private Mono<MoodEntryDto> createSavingError(Throwable throwable) {
        ControllerResponseException errorResponse;
        if (throwable instanceof ResourceSavingException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, MoodEntryControllerPaths.BASE_URI)
            );
        } else if (throwable instanceof ResourceAlreadyExistsException) {
            errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.CONFLICT,
                    String.format("%s/%s", contextPath, MoodEntryControllerPaths.BASE_URI)
            );
        } else {
            log.error("Controller returning failed status", throwable, HttpStatus.INTERNAL_SERVER_ERROR);
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }

    private Mono<Void> createUpdatingError(Throwable throwable) {
        if (throwable instanceof ResourceUpdatingException) {
            var errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, MoodEntryControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingError(throwable).then();
    }

    private Mono<Void> createDeleteError(Throwable throwable) {
        if (throwable instanceof ResourceDeletionException) {
            final ControllerResponseException errorResponse = new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, MoodEntryControllerPaths.BASE_URI)
            );
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingError(throwable).then();
    }
}


package com.niksob.gateway_service.controller.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.MoodEntryIdDto;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.http.controller.handler.mood.entry.ResourceControllerErrorUtil;
import com.niksob.gateway_service.path.controller.mood.entry.MoodEntryControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(MoodEntryControllerPaths.BASE_URI)
public class MoodEntryController {
    private final MoodEntryControllerService moodEntryControllerService;
    @Qualifier("moodEntryControllerUtil")
    private final ResourceControllerErrorUtil controllerUtil;

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
                .onErrorResume(controllerUtil::createLoadingErrorFlux)
                .switchIfEmpty(controllerUtil.returnNoContentStatus());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MoodEntryDto> save(@RequestBody MoodEntryDto moodEntryDto) {
        return moodEntryControllerService.save(moodEntryDto)
                .doOnSuccess(ignore -> log.debug("Successful mood entry saving", moodEntryDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(controllerUtil::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@RequestBody MoodEntryDto moodEntryDto) {
        return moodEntryControllerService.update(moodEntryDto)
                .doOnSuccess(ignore -> log.debug("Successful mood entry updating", moodEntryDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@RequestParam("id") MoodEntryIdDto id) {
        return moodEntryControllerService.deleteById(id)
                .doOnSuccess(ignore -> log.debug("Successful mood entry deletion", id))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createDeleteError);
    }
}

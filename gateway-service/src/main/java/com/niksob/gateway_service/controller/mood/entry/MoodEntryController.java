package com.niksob.gateway_service.controller.mood.entry;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.dto.mood.entry.UserMoodEntryIdDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.http.controller.handler.mood.entry.ResourceControllerErrorUtil;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import com.niksob.gateway_service.path.controller.mood.entry.MoodEntryControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date") LocalDate endDate,
            @AuthenticationPrincipal UserSecurityDetails userDetails
    ) {
        var userEntryDateRangeDto = new UserEntryDateRangeDto(userDetails.getId(), startDate, endDate);
        return moodEntryControllerService.loadByDateRange(userEntryDateRangeDto)
                .doOnNext(ignore -> log.debug("Successful mood entries loading", userEntryDateRangeDto))
                .doOnNext(ignore -> log.debug("Controller returning success status", HttpStatus.OK))
                .onErrorResume(controllerUtil::createLoadingErrorFlux)
                .switchIfEmpty(controllerUtil.returnNoContentStatus());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MoodEntryDto> save(
            @RequestBody MoodEntryDto moodEntryDto,
            @AuthenticationPrincipal UserSecurityDetails userDetails
    ) {
        setUserId(moodEntryDto, userDetails.getId());
        return moodEntryControllerService.save(moodEntryDto)
                .doOnSuccess(ignore -> log.debug("Successful mood entry saving", moodEntryDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.CREATED))
                .onErrorResume(controllerUtil::createSavingError);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(
            @RequestBody MoodEntryDto moodEntryDto,
            @AuthenticationPrincipal UserSecurityDetails userDetails
    ) {
        setUserId(moodEntryDto, userDetails.getId());
        return moodEntryControllerService.update(moodEntryDto)
                .doOnSuccess(ignore -> log.debug("Successful mood entry updating", moodEntryDto))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createUpdatingError);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteByIdAndUserId(
            @RequestParam("id") UserIdDto id, @AuthenticationPrincipal UserSecurityDetails userDetails
    ) {
        var userEntryId = new UserMoodEntryIdDto(Long.parseLong(id.getValue()), userDetails.getId());
        return moodEntryControllerService.deleteByIdAndUserId(userEntryId)
                .doOnSuccess(ignore -> log.debug("Successful mood entry deletion", userEntryId))
                .doOnSuccess(ignore -> log.debug("Controller returning success status", HttpStatus.NO_CONTENT))
                .onErrorResume(controllerUtil::createDeleteError);
    }

    private void setUserId(MoodEntryDto moodEntryDto, Long id) {
        moodEntryDto.setUserId(id);
        for (var tag : moodEntryDto.getMoodTags()) {
            tag.setUserId(id);
        }
    }
}

package com.niksob.database_service.controller.mood.tag;

import com.niksob.database_service.exception.entity.EntityNotDeletedException;
import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.mood.tag.MoodTagNameDto;
import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.domain.path.controller.database_service.mood.tag.MoodTagControllerPaths;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public Mono<MoodTagDto> load(@RequestParam("name") MoodTagNameDto nameDto) {
        return moodTagControllerService.load(nameDto)
                .onErrorResume(throwable -> createLoadingError(throwable, nameDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestParam("name") MoodTagNameDto nameDto) {
        return moodTagControllerService.delete(nameDto)
                .then()
                .onErrorResume(throwable -> createDeleteError(throwable, nameDto));
    }

    private Mono<MoodTagDto> createLoadingError(Throwable throwable, Object nameDto) {
        log.error("Mood tag load error", throwable, nameDto);
        if (throwable instanceof EntityNotFoundException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.NOT_FOUND,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<Void> createDeleteError(Throwable throwable, Object nameDto) {
        log.error("Mood tag delete error", throwable, nameDto);
        if (throwable instanceof EntityNotDeletedException) {
            return Mono.error(new ControllerResponseException(
                    throwable, HttpStatus.BAD_REQUEST,
                    String.format("%s/%s", contextPath, UserControllerPaths.BASE_URI)
            ));
        }
        return createLoadingError(throwable, nameDto).then();
    }
}

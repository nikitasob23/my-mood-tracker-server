package com.niksob.mailsender.controller;

import com.niksob.domain.path.controller.mail_sender.ActiveCodeMailSenderControllerPaths;
import com.niksob.mailsender.converter.base.Converter;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeMailSendingRequest;
import com.niksob.mailsender.model.mail.active_code.ActiveCodeSendingInfo;
import com.niksob.mailsender.service.base.ExecutableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(ActiveCodeMailSenderControllerPaths.BASE_URI)
@AllArgsConstructor
public class ActiveCodeMailSenderController {

    private final Converter<ActiveCodeMailSendingRequest, ActiveCodeSendingInfo> mailSendingInfoConverter;
    private ExecutableService<ActiveCodeSendingInfo, Void> sendMailToUserService;

    @PostMapping
    public ResponseEntity<?> sendActiveCode(@RequestBody ActiveCodeMailSendingRequest activeCodeRequest) {
        try {
            final ActiveCodeSendingInfo activeCodeSendingInfo = mailSendingInfoConverter.convert(activeCodeRequest);
            sendMailToUserService.execute(activeCodeSendingInfo);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
}
package com.niksob.mailsender.config.mail_message.active_code;

import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMailSubject;
import com.niksob.mailsender.model.values.MailMessageEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveCodeMailSubjectConfig {
    @Bean
    public ActiveCodeMailSubject getActiveCodeMailSubject() {
        return new ActiveCodeMailSubject(MailMessageEnum.SUBJECT.getData());
    }
}

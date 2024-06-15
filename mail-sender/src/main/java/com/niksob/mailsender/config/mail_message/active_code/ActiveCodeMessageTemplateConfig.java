package com.niksob.mailsender.config.mail_message.active_code;

import com.niksob.mailsender.model.mail.active_code.message.ActiveCodeMessageTemplate;
import com.niksob.mailsender.model.values.MailMessageEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveCodeMessageTemplateConfig {
    @Bean
    public ActiveCodeMessageTemplate getActiveCodeMessageTemplate() {
        return new ActiveCodeMessageTemplate(MailMessageEnum.TEMPLATE_MESSAGE.getData());
    }
}

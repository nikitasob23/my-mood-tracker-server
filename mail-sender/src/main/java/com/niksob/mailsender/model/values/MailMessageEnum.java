package com.niksob.mailsender.model.values;

import lombok.Getter;

@Getter
public enum MailMessageEnum {

    TEMPLATE_MESSAGE("Hello, %s \n"
            + "Welcome to GoodMood. Please, visit next link to confirm you mail: %s://%s/signup/activate/%s"),

    SUBJECT("Activation code");

    private final String data;

    MailMessageEnum(String data) {
        this.data = data;
    }
}

package com.niksob.domain.model.user;

import com.niksob.domain.annotation.Default;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private final UserId id;
    private final Email email;
    private final Username username;
    private final Password password;

    @Default
    public User(UserId id, Email email, Username username, Password password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(User userDetails) {
        this.id = userDetails.getId();
        this.email = userDetails.getEmail();
        this.username = userDetails.getUsername();
        this.password = userDetails.getPassword();
    }

    public User(Email email, Username username, Password password) {
        this.id = null;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}

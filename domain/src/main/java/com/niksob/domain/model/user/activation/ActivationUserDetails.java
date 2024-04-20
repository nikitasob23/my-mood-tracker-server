package com.niksob.domain.model.user.activation;

import com.niksob.domain.model.auth.login.active_code.ActiveCode;
import com.niksob.domain.model.user.SecurityUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ActivationUserDetails implements Serializable {
    private final ActiveCode activeCode;
    private final SecurityUserDetails userDetails;
}

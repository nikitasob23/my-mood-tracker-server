package com.niksob.gateway_service.model.user.security;

import com.niksob.gateway_service.model.user.security.role.SecurityRole;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@Getter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSecurityDetails implements UserDetails {
    private Long id;
    private String username;
    private String nickname;
    private String encodedPassword;
    //    private Set<SecurityRole> authorities;
    private boolean nonExpired = true;
    private boolean nonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enable = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(SecurityRole.USER);
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return nonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}

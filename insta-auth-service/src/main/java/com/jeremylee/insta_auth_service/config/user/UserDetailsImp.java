package com.jeremylee.insta_auth_service.config.user;

import com.jeremylee.insta_auth_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImp implements UserDetails {

    private Long id;
    private String userId;
    private String username;
    private String password;

    private Collection<GrantedAuthority> authorities;

    public static UserDetailsImp buildUserDetails(User user) {
//        List<GrantedAuthority> grantedAuthorities = roleNames
//                .stream()
//                .map(role -> new SimpleGrantedAuthority(user.getRole()))
//                .collect(Collectors.toUnmodifiableList());

        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));

        return new UserDetailsImp(
                user.getId(),
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

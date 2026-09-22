package org.mnuykin.mymarket.config.security;

import org.mnuykin.mymarket.advice.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class SecurityContextUtils {
    public static Mono<String> getCurrentUsername() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication == null) {
                        throw new UserNotFoundException("Пользователь не найден");
                    }
                    return authentication;
                })
                .filter(Authentication::isAuthenticated)
                .switchIfEmpty(Mono.error(new UserNotFoundException("")))
                .map(Authentication::getPrincipal)
                .cast(UserDetails.class)
                .map(UserDetails::getUsername);
    }

    public static Mono<Boolean> isAuthenticated(){
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication == null){
                        return false;
                    }
                    return authentication.isAuthenticated();
                }).defaultIfEmpty(false);
    }
}

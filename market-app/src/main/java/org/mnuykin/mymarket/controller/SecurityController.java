package org.mnuykin.mymarket.controller;

import org.mnuykin.mymarket.config.security.SecurityContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class SecurityController {
    @GetMapping("/login")
    public Mono<String> login() {
        return SecurityContextUtils.isAuthenticated()
                .map(isAuth -> isAuth ? "redirect:/" : "login" );
    }
}
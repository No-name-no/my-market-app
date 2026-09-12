package org.mnuykin.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class SecurityController {
    @GetMapping("/login")
    public Mono<String> login() {
        return Mono.just("login");
    }
}
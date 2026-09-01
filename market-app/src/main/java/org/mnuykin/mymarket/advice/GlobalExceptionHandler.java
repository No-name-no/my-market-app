package org.mnuykin.mymarket.advice;

import org.mnuykin.mymarket.advice.exception.CartEmptyException;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CartEmptyException.class})
    public Mono<Rendering> handleCartEmptyException() {
        return Mono.just(Rendering.redirectTo("/items")
                .modelAttribute("error", "Корзина пуста")
                .build()
        );
    }

    @ExceptionHandler({NoSuchElementException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Rendering> handleNotFoundException() {
        return Mono.just(Rendering.view("404").build()); // или .redirectTo("/items").build()
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Rendering> handleAllException(Exception exception) {
        exception.printStackTrace();
        return Mono.just(Rendering.view("5xx").build());
    }
}

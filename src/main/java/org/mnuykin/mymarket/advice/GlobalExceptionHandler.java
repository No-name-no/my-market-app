package org.mnuykin.mymarket.advice;

import org.mnuykin.mymarket.advice.exception.CartEmptyException;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Обработка 404
    @ExceptionHandler({NoSuchElementException.class, NotFoundException.class, CartEmptyException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIllegalArgumentException(Exception exception) {
        return exception.getMessage();
    }
}

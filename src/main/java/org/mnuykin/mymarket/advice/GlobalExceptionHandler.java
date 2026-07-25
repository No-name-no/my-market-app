package org.mnuykin.mymarket.advice;

import org.mnuykin.mymarket.advice.exception.CartEmptyException;
import org.mnuykin.mymarket.advice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CartEmptyException.class})
    public String handleCartEmptyException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Корзина пуста");
        return "redirect:/items";
    }

    @ExceptionHandler({NoSuchElementException.class, NotFoundException.class, NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception exception) {
        return "404";
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllException(Exception exception) {
        return "5xx";
    }
}

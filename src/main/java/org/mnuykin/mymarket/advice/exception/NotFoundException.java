package org.mnuykin.mymarket.advice.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Long id;

    public NotFoundException(Long id){
        super(String.format("Object with id = %s is not found", id));
        this.id = id;
    }
}

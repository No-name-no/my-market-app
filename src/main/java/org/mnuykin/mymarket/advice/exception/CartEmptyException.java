package org.mnuykin.mymarket.advice.exception;

public class CartEmptyException extends RuntimeException{
    public CartEmptyException(){
        super("Cart is empty");
    }
}

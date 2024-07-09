package com.ruwan.BookNetwork.exception;

public class OperationNotPermittedException extends RuntimeException {
    public  OperationNotPermittedException(String msg){
        super(msg);
    }
}

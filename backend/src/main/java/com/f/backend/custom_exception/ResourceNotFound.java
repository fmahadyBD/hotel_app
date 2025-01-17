package com.f.backend.custom_exception;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String message){
        super(message);
    }
}

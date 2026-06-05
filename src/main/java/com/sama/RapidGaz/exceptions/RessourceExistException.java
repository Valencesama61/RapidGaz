package com.sama.RapidGaz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RessourceExistException extends RuntimeException{
    public RessourceExistException(String message) {
        super(message);
    }
}



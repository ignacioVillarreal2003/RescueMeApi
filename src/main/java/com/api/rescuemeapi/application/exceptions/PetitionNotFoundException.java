package com.api.rescuemeapi.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PetitionNotFoundException extends RuntimeException {

    public PetitionNotFoundException() {
        super("Petition not found");
    }
}

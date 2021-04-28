package org.sharing.car.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Some constraints are violated")
public class InvalidConstraintsException extends RuntimeException {

    public InvalidConstraintsException(String message) {
        super(message);
    }

}

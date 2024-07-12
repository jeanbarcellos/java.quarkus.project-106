package com.jeanbarcellos.core.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ValidationException extends ApplicationException {

    public static final String ERRORS_PREFIX = "Erros=";

    private final List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public String getMessageToLog() {
        var message = this.getMessage();

        if (this.hasErrors()) {
            message += StringUtils.LF + ERRORS_PREFIX + this.getErrors().toString();
        }

        return message;
    }

    public static ValidationException of(String message) {
        return new ValidationException(message);
    }

    public static ValidationException of(String message, List<String> errors) {
        return new ValidationException(message, errors);
    }

}

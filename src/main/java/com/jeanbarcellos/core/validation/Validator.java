package com.jeanbarcellos.core.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import com.jeanbarcellos.core.Constants;
import com.jeanbarcellos.core.exception.ValidationException;

@ApplicationScoped
public class Validator {

    public static final String MSG_ERROR = "O campo '%s' %s";

    @Inject
    javax.validation.Validator innerValidator;

    public <T> Set<ConstraintViolation<T>> validate(T model) {
        return this.innerValidator.validate(model);
    }

    public <T> void validateWithException(T model) {
        var restricoes = this.validate(model);

        if (!restricoes.isEmpty()) {
            throw createValidateException(restricoes);
        }
    }

    public static <T> ValidationException createValidateException(Set<ConstraintViolation<T>> restricoes) {
        return ValidationException.of(Constants.MSG_ERROR_VALIDATION, createMessages(restricoes));
    }

    public static <T> List<String> createMessages(Set<ConstraintViolation<T>> restricoes) {
        return restricoes.stream()
                .map(cv -> String.format(MSG_ERROR, cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
    }

}

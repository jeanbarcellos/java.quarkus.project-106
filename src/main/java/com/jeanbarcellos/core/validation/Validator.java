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
        var constraints = this.validate(model);

        if (!constraints.isEmpty()) {
            throw createValidationException(constraints);
        }
    }

    private static <T> ValidationException createValidationException(Set<ConstraintViolation<T>> constraints) {
        return ValidationException.of(Constants.MSG_ERROR_VALIDATION, createMessages(constraints));
    }

    private static <T> List<String> createMessages(Set<ConstraintViolation<T>> constraints) {
        return constraints.stream()
                .map(constraint -> String.format(MSG_ERROR,
                        constraint.getPropertyPath().toString(), constraint.getMessage()))
                .collect(Collectors.toList());
    }

}

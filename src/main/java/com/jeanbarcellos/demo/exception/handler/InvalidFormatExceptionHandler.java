package com.jeanbarcellos.demo.exception.handler;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jeanbarcellos.core.Constants;
import com.jeanbarcellos.core.dto.ErrorResponse;
import com.jeanbarcellos.core.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class InvalidFormatExceptionHandler implements ExceptionMapper<InvalidFormatException> {

    public static final String SEPARATOR = ".";

    @Override
    public Response toResponse(InvalidFormatException exception) {
        log.error(exception.getMessage(), exception);

        var message = Constants.MSG_ERROR_REQUEST;
        var erro = String.format(Validator.MSG_ERROR, getNestedFieldName(exception.getPath()),
                Constants.ERROR_VALIDATION_JSON_INVALID_FORMAT);

        return Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(ErrorResponse.of(message, Arrays.asList(erro)))
                .build();
    }

    private static String getNestedFieldName(List<Reference> references) {
        var st = new StringBuilder("");

        var i = 0;
        for (Reference reference : references) {
            if (i != 0) {
                st.append(SEPARATOR);
            }

            st.append(ObjectUtils.isEmpty(reference.getFieldName())
                    ? reference.getIndex()
                    : reference.getFieldName());

            i++;
        }

        return st.toString();
    }

}

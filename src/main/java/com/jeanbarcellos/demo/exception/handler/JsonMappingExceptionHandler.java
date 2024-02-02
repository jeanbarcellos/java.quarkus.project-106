package com.jeanbarcellos.demo.exception.handler;

import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.jeanbarcellos.core.Constants;
import com.jeanbarcellos.core.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class JsonMappingExceptionHandler implements ExceptionMapper<JsonMappingException> {

    public static final String MSG_ERROR = "Erro de mapeamento, JSON mal formatado. Linha: %s, coluna: %s";

    @Override
    public Response toResponse(JsonMappingException exception) {
        log.error(exception.getMessage(), exception);

        var error = String.format(MSG_ERROR,
                exception.getLocation().getLineNr(),
                exception.getLocation().getColumnNr());

        return Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(ErrorResponse.of(
                        Constants.MSG_ERROR_REQUEST,
                        Arrays.asList(error)))
                .build();
    }

}

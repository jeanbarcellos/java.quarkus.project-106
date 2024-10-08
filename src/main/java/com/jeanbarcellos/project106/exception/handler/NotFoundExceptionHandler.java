package com.jeanbarcellos.project106.exception.handler;

import com.jeanbarcellos.core.Constants;
import com.jeanbarcellos.core.dto.ErrorResponse;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    private static final String RESTEASY_MESSAGE_PREFIX = "RESTEASY";

    @Override
    public Response toResponse(NotFoundException exception) {
        log.error(exception.getMessage());

        return Response
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .entity(ErrorResponse.of(prepareMessageFinal(exception)))
                .build();
    }

    private String prepareMessageFinal(NotFoundException exception) {
        return exception.getMessage().contains(RESTEASY_MESSAGE_PREFIX)
                ? Constants.MSG_ERROR_NOT_FOUND
                : exception.getMessage();
    }

}

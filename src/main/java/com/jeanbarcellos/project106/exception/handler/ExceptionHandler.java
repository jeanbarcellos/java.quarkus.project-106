package com.jeanbarcellos.project106.exception.handler;

import com.jeanbarcellos.core.Constants;
import com.jeanbarcellos.core.dto.ErrorResponse;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        log.error(exception.getMessage(), exception);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(new ErrorResponse(Constants.MSG_ERROR_SERVICE))
                .build();
    }

}

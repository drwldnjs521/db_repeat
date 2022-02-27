package de.hhu.cs.dbs.propra.application.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundException extends Exception implements ExceptionMapper<ResourceNotFoundException> {

    public ResourceNotFoundException() {
        super("No resource found with given name !!");
    }


    @Override
    public Response toResponse(ResourceNotFoundException exception)
    {
        return Response.status(404).header("message", exception.getMessage())
               .build();
    }
}

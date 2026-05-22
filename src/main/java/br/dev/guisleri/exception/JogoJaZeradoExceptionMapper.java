package br.dev.guisleri.exception;

import br.dev.guisleri.dto.RespostaApiDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JogoJaZeradoExceptionMapper implements ExceptionMapper<JogoJaZeradoException> {
    @Override
    public Response toResponse(JogoJaZeradoException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(RespostaApiDTO.semDados(exception.getMessage()))
                .build();
    }
}

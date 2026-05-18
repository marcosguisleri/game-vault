package br.dev.guisleri.exception;

import br.dev.guisleri.dto.RespostaApiDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JogoNaoEncontradoExceptionMapper implements ExceptionMapper<JogoNaoEncontradoException> {
    @Override
    public Response toResponse(JogoNaoEncontradoException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(RespostaApiDTO.semDados(exception.getMessage()))
                .build();
    }
}

package br.dev.guisleri.exception;

import br.dev.guisleri.dto.RespostaApiDTO;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ValidacaoExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        List<String> erros = exception.getConstraintViolations()
                .stream()
                .map(violacao -> {
                    String campo = violacao.getPropertyPath().toString();
                    campo = campo.substring(campo.lastIndexOf(".") + 1);

                    return campo + ": " + violacao.getMessage();
                })
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(RespostaApiDTO.comDados("Erro de validação.", erros))
                .build();
    }

}
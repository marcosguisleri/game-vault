package br.dev.guisleri.exception;

public class JogoNaoEncontradoException extends RuntimeException {
    public JogoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

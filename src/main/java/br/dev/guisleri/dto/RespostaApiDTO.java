package br.dev.guisleri.dto;

public record RespostaApiDTO<T>(String mensagem, T dados) {

    public static <T> RespostaApiDTO<T> comDados(String mensagem, T dados) {
        return new RespostaApiDTO<T>(mensagem, dados);
    }

    public static <T> RespostaApiDTO<T> semDados(String mensagem) {
        return new RespostaApiDTO<T>(mensagem, null);
    }
}

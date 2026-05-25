package br.dev.guisleri.dto;

public record ResultadoItemLoteDTO(
        String titulo,
        boolean sucesso,
        String mensagem
) {}
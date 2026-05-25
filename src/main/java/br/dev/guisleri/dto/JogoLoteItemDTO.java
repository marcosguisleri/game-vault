package br.dev.guisleri.dto;

public record JogoLoteItemDTO(
        String titulo,
        String genero,
        int anoLancamento,
        int quantHorasJogadas,
        boolean zerado
) {}

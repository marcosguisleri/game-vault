package br.dev.guisleri.dto;

import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;

public record JogoResponseDTO(
        Long id,
        String titulo,
        Genero genero,
        int anoLancamento,
        int quantHorasJogadas,
        boolean zerado

) {
    public static JogoResponseDTO from(Jogo jogo) {
        return new JogoResponseDTO(
                jogo.id,
                jogo.titulo,
                jogo.genero,
                jogo.anoLancamento,
                jogo.quantHorasJogadas,
                jogo.zerado
        );
    }
}

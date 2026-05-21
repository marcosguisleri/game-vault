package br.dev.guisleri.dto;

import br.dev.guisleri.model.Genero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record JogoRequestDTO(

        @NotBlank(message = "Título não pode ser vazio.")
        String titulo,

        @NotNull(message = "Gênero não pode ser nulo.")
        Genero genero,

        @Min(value = 1958, message = "Ano lançamento inválido.")
        int anoLancamento,

        @PositiveOrZero(message = "Quantidade de horas jogadas deve ser positiva ou zero.")
        int quantHorasJogadas,

        boolean zerado

) {
}

package br.dev.guisleri.dto;

import br.dev.guisleri.model.Genero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record JogoRequestDTO(

        @Schema(description = "Título do jogo", example = "The Witcher 3")
        @NotBlank(message = "Título não pode ser vazio.")
        String titulo,

        @Schema(description = "Gênero do jogo", example = "RPG")
        @NotNull(message = "Gênero não pode ser nulo.")
        Genero genero,

        @Schema(description = "Ano de lançamento", example = "2015")
        @Min(value = 1958, message = "Ano lançamento inválido.")
        int anoLancamento,

        @Schema(description = "Horas jogadas", example = "120")
        @PositiveOrZero(message = "Quantidade de horas jogadas deve ser positiva ou zero.")
        int quantHorasJogadas,

        @Schema(description = "Indica se o jogo foi zerado", example = "false")
        boolean zerado

) {
}

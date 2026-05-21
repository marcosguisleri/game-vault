package br.dev.guisleri.dto;

import jakarta.validation.constraints.Positive;

public record AdicionarHorasDTO(
        @Positive(message = "Horas devem ser positivas.")
        int horas
) {
}

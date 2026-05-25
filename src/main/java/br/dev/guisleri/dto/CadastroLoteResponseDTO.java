package br.dev.guisleri.dto;

import java.util.List;

public record CadastroLoteResponseDTO(
        int total,
        int cadastrados,
        int falhas,
        List<ResultadoItemLoteDTO> resultados
) {
    public static CadastroLoteResponseDTO from(List<ResultadoItemLoteDTO> resultados) {
        int cadastrados = (int) resultados.stream()
                .filter(ResultadoItemLoteDTO::sucesso)
                .count();
        return new CadastroLoteResponseDTO(
                resultados.size(),
                cadastrados,
                resultados.size() - cadastrados,
                resultados
        );
    }
}
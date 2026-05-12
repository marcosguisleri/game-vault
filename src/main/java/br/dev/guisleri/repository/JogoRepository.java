package br.dev.guisleri.repository;

import br.dev.guisleri.model.Jogo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JogoRepository {

    private static final String ARQUIVO_JOGOS = "jogos.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public void salvarJogo(List<Jogo> jogos) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(ARQUIVO_JOGOS), jogos);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar jogos no arquivo JSON", e);
        }
    }

    public List<Jogo> carregarJogos() {
        File arquivo = new File(ARQUIVO_JOGOS);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(Arrays.asList(mapper.readValue(arquivo, Jogo[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar jogos do arquivo JSON", e);
        }
    }

}

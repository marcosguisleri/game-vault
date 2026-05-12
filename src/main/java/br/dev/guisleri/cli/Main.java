package br.dev.guisleri.cli;

import br.dev.guisleri.model.Jogo;
import br.dev.guisleri.service.JogoService;

import java.util.List;
import java.util.Optional;

import static br.dev.guisleri.model.Genero.*;

public class Main {

    void main() {

        JogoService jogoService = new JogoService();

        Jogo jogo1 = new Jogo("God of War", ACAO, 2018, 47, true);
        Jogo jogo2 = new Jogo("Hollow Knight", AVENTURA, 2017, 62, true);
        Jogo jogo3 = new Jogo("Cyberpunk 2077", RPG, 2020, 103, false);
        Jogo jogo4 = new Jogo("Counter-Strike 2", FPS, 2023, 200, false);
        Jogo jogo5 = new Jogo("Mortal Kombat 11", LUTA, 2019, 28, true);
        Jogo jogo6 = new Jogo("FIFA 25", ESPORTE, 2024, 15, false);

        jogoService.adicionarJogo(jogo1);
        jogoService.adicionarJogo(jogo2);
        jogoService.adicionarJogo(jogo3);
        jogoService.adicionarJogo(jogo4);
        jogoService.adicionarJogo(jogo5);
        jogoService.adicionarJogo(jogo6);

        jogo3.zerarJogo();
        jogo3.adicionarHorasJogadas(102);

        jogo6.zerarJogo();
        jogo6.adicionarHorasJogadas(12);

        exibirCabecalho();

        exibirLista("TODOS OS JOGOS", jogoService.listarJogos());
        exibirLista("JOGOS DO GÊNERO LUTA", jogoService.listarJogosPorGenero(LUTA));
        exibirLista("JOGOS LANÇADOS EM 2024", jogoService.listarJogosPorAno(2024));
        exibirLista("JOGOS COM MAIS DE 100 HORAS", jogoService.listarJogosPorHorasJogadasMais(100));
        exibirLista("JOGOS COM MENOS DE 100 HORAS", jogoService.listarJogosPorHorasJogadasMenos(100));
        exibirLista("JOGOS NÃO ZERADOS", jogoService.listarJogosNaoZerados());
        exibirLista("JOGOS ZERADOS", jogoService.listarJogosZerados());

        exibirJogoPorTitulo("FIFA 25", jogoService.buscarJogoPorTitulo("FIFA 25"));

        exibirEstatisticas(jogoService);

        exibirLista("JOGOS ORDENADOS POR ANO DE LANÇAMENTO", jogoService.jogosOrdenadosPorAnoLancamento());
        exibirLista("JOGOS ORDENADOS POR HORAS JOGADAS", jogoService.jogosOrdenadosPorHorasJogadas());
        exibirLista("JOGOS ORDENADOS POR TÍTULO", jogoService.jogosOrdenadosPorTitulo());

        exibirRodape();
    }

    private static void exibirCabecalho() {
        IO.println("""
                
                ========================================
                         GAME VAULT CLI
                   Biblioteca pessoal de jogos
                ========================================
                """);
    }

    private static void exibirLista(String titulo, List<Jogo> jogos) {
        exibirSecao(titulo);

        if (jogos.isEmpty()) {
            IO.println("Nenhum jogo encontrado.");
            return;
        }

        for (int i = 0; i < jogos.size(); i++) {
            IO.println("[" + (i + 1) + "]");
            IO.println(jogos.get(i));
        }
    }

    private static void exibirJogoPorTitulo(String tituloBuscado, Optional<Jogo> jogoPorTitulo) {
        exibirSecao("BUSCA POR TÍTULO");

        IO.println("Título pesquisado: " + tituloBuscado);

        if (jogoPorTitulo.isPresent()) {
            IO.println("\nJogo encontrado:");
            IO.println(jogoPorTitulo.get());
        } else {
            IO.println("\nNenhum jogo encontrado com esse título.");
        }
    }

    private static void exibirEstatisticas(JogoService jogoService) {
        exibirSecao("ESTATÍSTICAS DA BIBLIOTECA");

        IO.println("Total de jogos cadastrados: " + jogoService.listarJogos().size());
        IO.println("Total de horas jogadas: " + jogoService.totalDeHorasJogadas());

        IO.println("\nJogo mais jogado:");

        Optional<Jogo> jogoMaisJogado = jogoService.jogoComMaisHorasJogadas();

        if (jogoMaisJogado.isPresent()) {
            IO.println(jogoMaisJogado.get());
        } else {
            IO.println("Nenhum jogo encontrado.");
        }
    }

    private static void exibirSecao(String titulo) {
        IO.println("\n----------------------------------------");
        IO.println(" " + titulo);
        IO.println("----------------------------------------");
    }

    private static void exibirRodape() {
        IO.println("""
                
                ========================================
                   Fim da execução do Game Vault CLI
                ========================================
                """);
    }
}
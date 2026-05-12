package br.dev.guisleri.cli;

import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;
import br.dev.guisleri.repository.JogoRepository;
import br.dev.guisleri.service.JogoService;

import java.util.List;
import java.util.Optional;

import static br.dev.guisleri.model.Genero.*;

public class Main {

    void main() {

        JogoRepository jogoRepository = new JogoRepository();
        JogoService jogoService = new JogoService();

        jogoRepository.carregarJogos().forEach(jogoService::adicionarJogo);

        if (jogoService.listarJogos().isEmpty()) {
            jogoService.adicionarJogo(new Jogo("God of War", ACAO, 2018, 47, true));
            jogoService.adicionarJogo(new Jogo("Hollow Knight", AVENTURA, 2017, 62, true));
            jogoService.adicionarJogo(new Jogo("Cyberpunk 2077", RPG, 2020, 205, true));
            jogoService.adicionarJogo(new Jogo("Counter-Strike 2", FPS, 2023, 200, false));
            jogoService.adicionarJogo(new Jogo("Mortal Kombat 11", LUTA, 2019, 28, true));
            jogoService.adicionarJogo(new Jogo("FIFA 25", ESPORTE, 2024, 27, true));
        }

        exibirCabecalho();

        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            int opcao = Integer.parseInt(IO.readln("Escolha uma opção: "));

            switch (opcao) {
                case 0 -> rodando = false;
                case 1 -> exibirLista("JOGOS CADASTRADOS", jogoService.listarJogos());
                case 2 -> {
                    Jogo novoJogo = adicionarJogo();
                    if (novoJogo != null) {
                        jogoService.adicionarJogo(novoJogo);
                        IO.println("\nJogo adicionado com sucesso!\n");
                    }
                }
                case 3 -> {
                    String titulo = IO.readln("Informe o título do jogo: ").trim();
                    exibirJogoPorTitulo(titulo, jogoService.buscarJogoPorTitulo(titulo));
                }
                case 4 -> exibirEstatisticas(jogoService);
                default -> IO.println("\nOpção inválida. Tente novamente.");
            }
        }

        jogoRepository.salvarJogo(jogoService.listarJogos());

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

    private static void exibirMenu() {
        IO.println("""
                1. Listar todos os jogos
                2. Adicionar novo jogo
                3. Buscar jogo por título
                4. Estatísticas
                0. Sair
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

    private static Jogo adicionarJogo() {
        exibirSecao("ADICIONAR JOGO");

        IO.println("Preencha as informações abaixo para cadastrar um novo jogo.\n");

        String titulo = IO.readln("Título do jogo: ").trim();

        IO.println("""
            
            Gêneros disponíveis:
            ACAO | AVENTURA | RPG | FPS | LUTA | ESPORTE | CORRIDA | ESTRATEGIA | TERROR | PLATAFORMA | SIMULACAO | PUZZLE
            """);

        String generoString = IO.readln("Gênero do jogo: ").trim().toUpperCase();

        int ano = Integer.parseInt(IO.readln("Ano de lançamento: ").trim());

        int quantHoras = Integer.parseInt(IO.readln("Quantidade de horas jogadas: ").trim());

        String zerou = IO.readln("Já zerou o jogo? [S/N]: ").trim();

        Genero genero;
        try {
            genero = Genero.valueOf(generoString);
        } catch (IllegalArgumentException e) {
            IO.println("\nGênero inválido. Tente novamente.\n");
            return null;
        }
        boolean zerado = zerou.equalsIgnoreCase("S");

        return new Jogo(titulo, genero, ano, quantHoras, zerado);
    }

    private static void exibirRodape() {
        IO.println("""
                
                ========================================
                         Dados salvos. Até mais!
                ========================================
                """);
    }
}
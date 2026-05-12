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

        carregarJogos(jogoRepository, jogoService);

        exibirCabecalho();
        executarMenuPrincipal(jogoService);

        jogoRepository.salvarJogo(jogoService.listarJogos());
        exibirRodape();
    }

    private static void carregarJogos(JogoRepository jogoRepository, JogoService jogoService) {
        jogoRepository.carregarJogos().forEach(jogoService::adicionarJogo);

        if (jogoService.listarJogos().isEmpty()) {
            carregarJogosPadrao(jogoService);
        }
    }

    private static void carregarJogosPadrao(JogoService jogoService) {
        jogoService.adicionarJogo(new Jogo("God of War", ACAO, 2018, 47, true));
        jogoService.adicionarJogo(new Jogo("Hollow Knight", AVENTURA, 2017, 62, true));
        jogoService.adicionarJogo(new Jogo("Cyberpunk 2077", RPG, 2020, 205, true));
        jogoService.adicionarJogo(new Jogo("Counter-Strike 2", FPS, 2023, 200, false));
        jogoService.adicionarJogo(new Jogo("Mortal Kombat 11", LUTA, 2019, 28, true));
        jogoService.adicionarJogo(new Jogo("FIFA 25", ESPORTE, 2024, 27, true));
    }

    private static void executarMenuPrincipal(JogoService jogoService) {
        boolean rodando = true;

        while (rodando) {
            exibirMenu();

            try {
                int opcao = Integer.parseInt(IO.readln("Escolha uma opção: "));

                switch (opcao) {
                    case 0 -> rodando = false;

                    case 1 -> exibirLista("JOGOS CADASTRADOS", jogoService.listarJogos());

                    case 2 -> cadastrarJogo(jogoService);

                    case 3 -> buscarJogoPorTitulo(jogoService);

                    case 4 -> exibirEstatisticas(jogoService);

                    case 5 -> exibirMenuFiltro(jogoService);

                    case 6 -> exibirMenuOrdenacoes(jogoService);

                    default -> IO.println("Opção inválida. Tente novamente.\n");
                }
            } catch (NumberFormatException e) {
                IO.println("\nOpção inválida. Digite apenas números.\n");
            }

        }
    }

    private static void cadastrarJogo(JogoService jogoService) {
        Jogo novoJogo = adicionarJogo();

        if (novoJogo != null) {
            jogoService.adicionarJogo(novoJogo);
            IO.println("\nJogo adicionado com sucesso!\n");
        }
    }

    private static void buscarJogoPorTitulo(JogoService jogoService) {
        String titulo = IO.readln("Informe o título do jogo: ").trim();
        Optional<Jogo> jogoEncontrado = jogoService.buscarJogoPorTitulo(titulo);

        exibirJogoPorTitulo(titulo, jogoEncontrado);
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
                5. Filtros
                6. Ordenações
                0. Sair
                ========================================
                """);
    }

    private static void exibirMenuFiltro(JogoService jogoService) {
        boolean rodando = true;

        while (rodando) {
            IO.println("""
                    
                    ========================================
                     FILTROS
                    ========================================
                    1. Por gênero
                    2. Por ano
                    3. Zerados
                    4. Não zerados
                    5. Mais de X horas
                    6. Menos de X horas
                    0. Voltar
                    ========================================
                    """);

            int opcao = Integer.parseInt(IO.readln("Escolha uma opção: "));

            switch (opcao) {
                case 0 -> rodando = false;

                case 1 -> listarPorGenero(jogoService);

                case 2 -> listarPorAno(jogoService);

                case 3 -> listarZerados(jogoService);

                case 4 -> listarNaoZerados(jogoService);

                case 5 -> listarMaisHoras(jogoService);

                case 6 -> listarMenosHoras(jogoService);

                default -> IO.println("Opção Inválida. Tente novamente.\n");
            }
        }
    }

    private static void exibirMenuOrdenacoes(JogoService jogoService) {
        boolean rodando = true;

        while (rodando) {
            IO.println("""
                    
                    ========================================
                     ORDENAÇÕES
                    ========================================
                    1. Ano lançamento
                    2. Por horas jogadas
                    3. Título
                    0. Voltar
                    ========================================
                    """);

            int opcao = Integer.parseInt(IO.readln("Escolha uma opção: "));

            switch (opcao) {
                case 0 -> rodando = false;

                case 1 -> ordenarPorAno(jogoService);

                case 2 -> ordenarPorHoras(jogoService);

                case 3 -> ordenarPorTitulo(jogoService);

                default -> IO.println("Opção Inválida. Tente novamente.\n");
            }
        }
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

    private static void listarPorGenero(JogoService jogoService) {
        exibirSecao("LISTA POR GÊNERO");

        IO.println("""
                
                Gêneros disponíveis:
                ACAO | AVENTURA | RPG | FPS | LUTA | ESPORTE | CORRIDA | ESTRATEGIA | TERROR | PLATAFORMA | SIMULACAO | PUZZLE
                """);

        String generoString = IO.readln("Gênero do jogo: ").trim().toUpperCase();

        try {
            Genero genero = Genero.valueOf(generoString);
            exibirLista("JOGOS DO GÊNERO " + genero.name(), jogoService.listarJogosPorGenero(genero));
        } catch (IllegalArgumentException e) {
            IO.println("\nGênero inválido. Tente novamente.\n");
        }
    }

    private static void listarPorAno(JogoService jogoService) {
        exibirSecao("LISTA POR ANO");

        try {
            int ano = Integer.parseInt(IO.readln("Ano: ").trim());
            exibirLista("JOGOS DO ANO " + ano, jogoService.listarJogosPorAno(ano));
        } catch (NumberFormatException e) {
            IO.println("\nAno inválido. Digite apenas números.\n");
        }
    }

    private static void listarZerados(JogoService jogoService) {
        exibirLista("JOGOS ZERADOS", jogoService.listarJogosZerados());
    }

    private static void listarNaoZerados(JogoService jogoService) {
        exibirLista("JOGOS NÃO ZERADOS", jogoService.listarJogosNaoZerados());
    }

    private static void listarMaisHoras(JogoService jogoService) {
        exibirSecao("LISTA MAIS HORAS");

        try {
            int quantHoras = Integer.parseInt(IO.readln("Quantidade de horas: ").trim());
            exibirLista("JOGOS COM " + quantHoras + " OU MAIS HORAS JOGADAS", jogoService.listarJogosPorHorasJogadasMais(quantHoras));
        } catch (NumberFormatException e) {
            IO.println("\nHoras inválida. Digite apenas números.\n");
        }

    }

    private static void listarMenosHoras(JogoService jogoService) {
        exibirSecao("LISTA MENOS HORAS");

        try {
            int quantHoras = Integer.parseInt(IO.readln("Quantidade de horas: ").trim());
            exibirLista("JOGOS COM " + quantHoras + " OU MENOS HORAS JOGADAS", jogoService.listarJogosPorHorasJogadasMenos(quantHoras));
        } catch (NumberFormatException e) {
            IO.println("\nHoras inválida. Digite apenas números.\n");
        }

    }

    private static void ordenarPorAno(JogoService jogoService) {
        exibirLista("ORDENAÇÃO POR ANO DE LANÇAMENTO", jogoService.jogosOrdenadosPorAnoLancamento());
    }

    private static void ordenarPorHoras(JogoService jogoService) {
        exibirLista("ORDENAÇÃO POR HORAS JOGADAS", jogoService.jogosOrdenadosPorHorasJogadas());
    }

    private static void ordenarPorTitulo(JogoService jogoService) {
        exibirLista("ORDENAÇÃO POR TÍTULO", jogoService.jogosOrdenadosPorTitulo());
    }

    private static void exibirJogoPorTitulo(String tituloBuscado, Optional<Jogo> jogoPorTitulo) {
        exibirSecao("BUSCA POR TÍTULO");

        IO.println("Título pesquisado: " + tituloBuscado);

        if (jogoPorTitulo.isPresent()) {
            IO.println("\nJogo encontrado:");
            IO.println(jogoPorTitulo.get());
        } else {
            IO.println("\nNenhum jogo encontrado com esse título.\n");
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
            genero = valueOf(generoString);
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
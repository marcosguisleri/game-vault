package br.dev.guisleri.resource;

import br.dev.guisleri.model.Jogo;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

@QuarkusTest
class JogoResourceTest {

    @BeforeEach
    @Transactional
    void limparBanco() {
        Jogo.deleteAll();
    }

    private Long criarJogo(String titulo) {
        return criarJogo(titulo, "RPG", 2015, 120, false);
    }

    private Long criarJogo(
            String titulo,
            String genero,
            int anoLancamento,
            int horas,
            boolean zerado
    ) {
        String json = """
                {
                    "titulo": "%s",
                    "genero": "%s",
                    "anoLancamento": %d,
                    "quantHorasJogadas": %d,
                    "zerado": %s
                }
                """.formatted(titulo, genero, anoLancamento, horas, zerado);

        Number id = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos")
                .then()
                .statusCode(201)
                .extract()
                .path("dados.id");

        return id.longValue();
    }

    // TESTES DE CADASTRO

    @Test
    void deveCadastrarJogoComSucesso() {
        String json = """
                {
                    "titulo": "Forza Horizon 6",
                    "genero": "CORRIDA",
                    "anoLancamento": 2026,
                    "quantHorasJogadas": 6,
                    "zerado": false
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos")
                .then()
                .statusCode(201)
                .body("mensagem", equalTo("Jogo cadastrado com sucesso!"))
                .body("dados.titulo", equalTo("Forza Horizon 6"))
                .body("dados.genero", equalTo("CORRIDA"))
                .body("dados.anoLancamento", equalTo(2026))
                .body("dados.quantHorasJogadas", equalTo(6))
                .body("dados.zerado", equalTo(false));
    }

    @Test
    void naoDeveCadastrarJogoComTituloRepetido() {
        criarJogo("Forza Horizon 6", "CORRIDA", 2015, 120, true);

        String json = """
                {
                    "titulo": "Forza Horizon 6",
                    "genero": "CORRIDA",
                    "anoLancamento": 2026,
                    "quantHorasJogadas": 200,
                    "zerado": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos")
                .then()
                .statusCode(409)
                .body("mensagem", equalTo("Já existe um jogo cadastrado com esse título."))
                .body("dados", nullValue());
    }

    @Test
    void naoDeveCadastrarJogoComDadosInvalidos() {
        String json = """
                {
                    "titulo": "",
                    "genero": null,
                    "anoLancamento": 1957,
                    "quantHorasJogadas": -10,
                    "zerado": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos")
                .then()
                .statusCode(400)
                .body("mensagem", equalTo("Erro de validação."))
                .body("dados", hasSize(4));
    }

    // TESTES DE BUSCAS

    @Test
    void deveBuscarJogoPorIdComSucesso() {
        Long id = criarJogo("Forza Horizon 6");

        given()
                .when()
                .get("/jogos/{id}", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo encontrado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Forza Horizon 6"));
    }

    @Test
    void naoDeveBuscarJogoPorIdInexistente() {
        given()
                .when()
                .get("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", equalTo("Jogo com ID " + 99999 + " não encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void deveBuscarJogoPorTituloComSucesso() {
        Long id = criarJogo("Forza Horizon 6");

        given()
                .when()
                .get("/jogos/titulo/{titulo}", "Forza Horizon 6")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo encontrado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Forza Horizon 6"));
    }

    @Test
    void naoDeveBuscarJogoPorTituloInexistente() {
        given()
                .when()
                .get("/jogos/titulo/{titulo}", "Jogo Inexistente")
                .then()
                .statusCode(404)
                .body("mensagem", equalTo("Jogo com título Jogo Inexistente não encontrado."))
                .body("dados", nullValue());
    }

    // TESTES DE ATUALIZAÇÃO E REMOÇÃO

    @Test
    void deveAtualizarJogoComSucesso() {
        Long id = criarJogo("Forza Horizon 6");

        String json = """
                {
                    "titulo": "Forza Horizon 5",
                    "genero": "CORRIDA",
                    "anoLancamento": 2026,
                    "quantHorasJogadas": 20,
                    "zerado": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/jogos/{id}", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo atualizado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Forza Horizon 5"))
                .body("dados.genero", equalTo("CORRIDA"))
                .body("dados.anoLancamento", equalTo(2026))
                .body("dados.quantHorasJogadas", equalTo(20))
                .body("dados.zerado", equalTo(true));
    }

    @Test
    void naoDeveAtualizarJogoInexistente() {
        String json = """
                {
                    "titulo": "Forza Horizon 5",
                    "genero": "CORRIDA",
                    "anoLancamento": 2026,
                    "quantHorasJogadas": 20,
                    "zerado": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", equalTo("Jogo com ID 99999 não encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void deveZerarJogoComSucesso() {
        Long id = criarJogo("Forza Horizon 6", "CORRIDA", 2026, 10, false);

        given()
                .when()
                .patch("/jogos/{id}/zerar", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo zerado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Forza Horizon 6"))
                .body("dados.zerado", equalTo(true));
    }

    @Test
    void naoDeveZerarJogoJaZerado() {
        Long id = criarJogo("Forza Horizon 6", "CORRIDA", 2026, 20, true);

        given()
                .when()
                .patch("/jogos/{id}/zerar", id)
                .then()
                .statusCode(409)
                .body("mensagem", equalTo("Este jogo já está zerado."))
                .body("dados", nullValue());
    }

    @Test
    void deveRemoverJogoComSucesso() {
        Long id = criarJogo("Forza Horizon 6");

        given()
                .when()
                .delete("/jogos/{id}", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo removido com sucesso."))
                .body("dados", nullValue());

        given()
                .when()
                .get("/jogos/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void deveRemoverTodosOsJogosComSucesso() {
        given()
                .queryParam("confirmar", true)  // nome e valor separados
                .when()
                .delete("/jogos")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Todos os jogos removidos com sucesso."))
                .body("dados", nullValue());
    }

    @Test
    void naoDeveRemoverTodosOsJogosSemConfirmacao() {
        given()
                .when()
                .delete("/jogos")
                .then()
                .statusCode(400);
    }

    @Test
    void naoDeveRemoverJogoInexistente() {
        given()
                .when()
                .delete("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", equalTo("Jogo com ID 99999 não encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void deveAdicionarHorasJogadasComSucesso() {
        Long id = criarJogo("Forza Horizon 6", "CORRIDA", 2026, 100, false);

        String json = """
            {
                "horas": 10
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/jogos/{id}/horas", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Horas adicionadas com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Forza Horizon 6"))
                .body("dados.quantHorasJogadas", equalTo(110));
    }

    @Test
    void naoDeveAdicionarHorasJogadasNegativas() {
        Long id = criarJogo("Forza Horizon 6");

        String json = """
            {
                "horas": -5
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/jogos/{id}/horas", id)
                .then()
                .statusCode(400)
                .body("mensagem", equalTo("Erro de validação."))
                .body("dados", hasSize(1))
                .body("dados[0]", equalTo("horas: Horas devem ser positivas."));
    }

    // TESTES DE LISTAGEM

    @Test
    void deveListarTodosOsJogosComSucesso() {
        criarJogo("Forza Horizon 6");
        criarJogo("Forza Horizon 5");

        given()
                .when()
                .get("/jogos")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos listados com sucesso."))
                .body("dados", hasSize(2));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverJogos() {
        given()
                .when()
                .get("/jogos")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Nenhum jogo encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void deveListarJogosZeradosComSucesso() {
        criarJogo("Forza Horizon 6", "CORRIDA", 2026, 10, true);
        criarJogo("Forza Horizon 5", "CORRIDA", 2026, 20, false);

        given()
                .when()
                .get("/jogos/zerados")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos zerados listados com sucesso."))
                .body("dados", hasSize(1));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverJogosZerados() {
        given()
                .when()
                .get("/jogos/zerados")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Nenhum jogo zerado encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void deveListarJogosNaoZeradosComSucesso() {
        criarJogo("Forza Horizon 6", "CORRIDA", 2026, 10, true);
        criarJogo("Forza Horizon 5", "CORRIDA", 2026, 20, false);

        given()
                .when()
                .get("/jogos/nao-zerados")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos não zerados listados com sucesso."))
                .body("dados", hasSize(1));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverJogosNaoZerados() {
        given()
                .when()
                .get("/jogos/nao-zerados")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Nenhum jogo não zerado encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void deveListarJogosPorGeneroComSucesso() {
        criarJogo("Forza Horizon 6", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 5", "CORRIDA", 2026, 20, false);

        given()
                .when()
                .get("/jogos/genero/RPG")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos do gênero RPG listados com sucesso."))
                .body("dados", hasSize(1));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverJogosDoGenero() {
        given()
                .when()
                .get("/jogos/genero/RPG")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Nenhum jogo encontrado nesse gênero."))
                .body("dados", nullValue());
    }

    @Test
    void naoDeveListarJogosPorGeneroInvalido() {
        given()
                .when()
                .get("/jogos/genero/MMORPG")
                .then()
                .statusCode(400)
                .body("mensagem", equalTo("Gênero inválido. Valores permitidos: ACAO, AVENTURA, RPG, FPS, LUTA, ESPORTE, CORRIDA, ESTRATEGIA, TERROR, PLATAFORMA, SIMULACAO, PUZZLE"))
                .body("dados", nullValue());
    }

    @Test
    void deveListarJogosPorAnoComSucesso() {
        criarJogo("Forza Horizon 6", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 5", "CORRIDA", 2026, 20, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2020, 20, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2019, 20, false);

        given()
                .when()
                .get("/jogos/ano/2026")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos do ano 2026 listados com sucesso."))
                .body("dados", hasSize(2));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverJogosDaqueleAno() {
        given()
                .when()
                .get("/jogos/ano/2020")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Nenhum jogo encontrado nesse ano."))
                .body("dados", nullValue());
    }

    @Test
    void deveListarJogosComMaisDeXHorasComSucesso() {
        criarJogo("Forza Horizon 6", "RPG", 2026, 8, false);
        criarJogo("Forza Horizon 5", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2026, 20, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2026, 30, false);

        given()
                .when()
                .get("/jogos/horas-jogadas-mais/10")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos com ou mais de 10 horas jogadas listados com sucesso."))
                .body("dados", hasSize(3));
    }

    @Test
    void deveListarJogosComMenosDeXHorasComSucesso() {
        criarJogo("Forza Horizon 6", "RPG", 2026, 8, false);
        criarJogo("Forza Horizon 5", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2026, 20, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2026, 30, false);

        given()
                .when()
                .get("/jogos/horas-jogadas-menos/10")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos com menos de 10 horas jogadas listados com sucesso."))
                .body("dados", hasSize(2));
    }

    // TESTES DE ORDENAÇÃO

    @Test
    void deveListarJogosOrdenadosPorTituloComSucesso() {
        criarJogo("Forza Horizon 5", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 6", "RPG", 2026, 8, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2026, 30, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2026, 20, false);

        given()
                .when()
                .get("/jogos/ordenar-por-titulo")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos ordenados por título."))
                .body("dados.titulo", contains("Forza Horizon 3", "Forza Horizon 4", "Forza Horizon 5", "Forza Horizon 6"));
    }

    @Test
    void deveListarJogosOrdenadosPorGeneroComSucesso() {
        criarJogo("Forza Horizon 5", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 6", "RPG", 2026, 8, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2026, 30, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2026, 20, false);
        criarJogo("Forza Horizon 7", "FPS", 2026, 10, false);
        criarJogo("Forza Horizon 8", "FPS", 2026, 15, false);

        given()
                .when()
                .get("/jogos/ordenar-por-genero")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos ordenados por gênero."))
                .body("dados.genero", contains("CORRIDA", "CORRIDA", "FPS", "FPS", "RPG", "RPG"));

    }

    @Test
    void deveListarJogosOrdenadosPorAnoComSucesso() {
        criarJogo("Forza Horizon 5", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 6", "RPG", 2024, 8, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2022, 30, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2020, 20, false);

        given()
                .when()
                .get("/jogos/ordenar-por-ano")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos ordenados por ano."))
                .body("dados.anoLancamento", contains(2020, 2022, 2024, 2026));
    }

    @Test
    void deveListarJogosOrdenadosPorHorasJogadasComSucesso() {
        criarJogo("Forza Horizon 5", "RPG", 2026, 10, false);
        criarJogo("Forza Horizon 6", "RPG", 2024, 8, false);
        criarJogo("Forza Horizon 3", "CORRIDA", 2022, 30, false);
        criarJogo("Forza Horizon 4", "CORRIDA", 2020, 20, false);

        given()
                .when()
                .get("/jogos/ordenar-por-horas-jogadas")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos ordenados por horas jogadas."))
                .body("dados.quantHorasJogadas", contains(8, 10, 20, 30));
    }

    // TESTES DE CADASTRO EM LOTE

    @Test
    void deveCadastrarTodosOsJogosDoLoteComSucesso() {
        String json = """
            [
                { "titulo": "God of War", "genero": "ACAO", "anoLancamento": 2018, "quantHorasJogadas": 40, "zerado": true },
                { "titulo": "Hollow Knight", "genero": "PLATAFORMA", "anoLancamento": 2017, "quantHorasJogadas": 30, "zerado": false }
            ]
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos/lote")
                .then()
                .statusCode(207)
                .body("mensagem", equalTo("Processamento em lote concluído."))
                .body("dados.total", equalTo(2))
                .body("dados.cadastrados", equalTo(2))
                .body("dados.falhas", equalTo(0))
                .body("dados.resultados[0].sucesso", equalTo(true))
                .body("dados.resultados[1].sucesso", equalTo(true));
    }

    @Test
    void deveCadastrarLoteComSucessosEFalhasMisturados() {
        criarJogo("God of War", "ACAO", 2018, 40, true); // já existe

        String json = """
            [
                { "titulo": "God of War", "genero": "ACAO", "anoLancamento": 2018, "quantHorasJogadas": 40, "zerado": true },
                { "titulo": "Hollow Knight", "genero": "PLATAFORMA", "anoLancamento": 2017, "quantHorasJogadas": 30, "zerado": false }
            ]
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos/lote")
                .then()
                .statusCode(207)
                .body("dados.total", equalTo(2))
                .body("dados.cadastrados", equalTo(1))
                .body("dados.falhas", equalTo(1))
                .body("dados.resultados[0].titulo", equalTo("God of War"))
                .body("dados.resultados[0].sucesso", equalTo(false))
                .body("dados.resultados[0].mensagem", equalTo("Já existe um jogo cadastrado com esse título."))
                .body("dados.resultados[1].titulo", equalTo("Hollow Knight"))
                .body("dados.resultados[1].sucesso", equalTo(true));
    }

    @Test
    void deveFalharTodosOsItensQuandoTodosJaExistem() {
        criarJogo("God of War", "ACAO", 2018, 40, true);
        criarJogo("Hollow Knight", "PLATAFORMA", 2017, 30, false);

        String json = """
            [
                { "titulo": "God of War", "genero": "ACAO", "anoLancamento": 2018, "quantHorasJogadas": 40, "zerado": true },
                { "titulo": "Hollow Knight", "genero": "PLATAFORMA", "anoLancamento": 2017, "quantHorasJogadas": 30, "zerado": false }
            ]
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos/lote")
                .then()
                .statusCode(207)
                .body("dados.total", equalTo(2))
                .body("dados.cadastrados", equalTo(0))
                .body("dados.falhas", equalTo(2))
                .body("dados.resultados[0].sucesso", equalTo(false))
                .body("dados.resultados[1].sucesso", equalTo(false));
    }

}
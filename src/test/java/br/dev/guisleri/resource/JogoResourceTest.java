package br.dev.guisleri.resource;

import br.dev.guisleri.model.Jogo;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class JogoResourceTest {

    @BeforeEach
    @Transactional
    void limparBanco() {
        Jogo.deleteAll();
    }

    private Long criarJogo(String titulo) {
        return criarJogo(titulo, "RPG", 2015, 120, true);
    }

    private Long criarJogo(String titulo, String genero, int anoLancamento, int horas, boolean zerado) {
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

    // ========================
    // Testes de cadastro
    // ========================

    @Test
    void deveCadastrarJogoComSucesso() {
        String json = """
                {
                    "titulo": "God of War",
                    "genero": "ACAO",
                    "anoLancamento": 2018,
                    "quantHorasJogadas": 40,
                    "zerado": true
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
                .body("dados.titulo", equalTo("God of War"))
                .body("dados.genero", equalTo("ACAO"))
                .body("dados.anoLancamento", equalTo(2018))
                .body("dados.quantHorasJogadas", equalTo(40))
                .body("dados.zerado", equalTo(true));
    }

    @Test
    void naoDeveCadastrarJogoComTituloRepetido() {
        criarJogo("The Last of Us");

        String json = """
                {
                    "titulo": "The Last of Us",
                    "genero": "AVENTURA",
                    "anoLancamento": 2013,
                    "quantHorasJogadas": 15,
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
                .body("mensagem", containsString("existe"));
    }

    @Test
    void naoDeveCadastrarJogoComDadosInvalidos() {
        String json = """
                {
                    "titulo": "",
                    "genero": null,
                    "anoLancamento": -1,
                    "quantHorasJogadas": -5,
                    "zerado": false
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/jogos")
                .then()
                .statusCode(400);
    }

    // ========================
    // Testes de busca
    // ========================

    @Test
    void deveBuscarJogoPorId() {
        Long id = criarJogo("Hollow Knight");

        given()
                .when()
                .get("/jogos/{id}", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo encontrado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Hollow Knight"));
    }

    @Test
    void deveRetornar404QuandoJogoNaoEncontradoPorId() {
        given()
                .when()
                .get("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("encontrado"));
    }

    @Test
    void deveBuscarJogoPorTitulo() {
        Long id = criarJogo("The Last of Us");

        given()
                .when()
                .get("/jogos/titulo/{titulo}", "The Last of Us")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo encontrado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("The Last of Us"));
    }

    @Test
    void deveRetornar404QuandoJogoNaoEncontradoPorTitulo() {
        given()
                .when()
                .get("/jogos/titulo/{titulo}", "Jogo Inexistente")
                .then()
                .statusCode(404)
                .body("mensagem", containsString("encontrado"));
    }

    // ========================
    // Testes de atualização
    // ========================

    @Test
    void deveAtualizarJogo() {
        Long id = criarJogo("Celeste");

        String jsonAtualizado = """
                {
                    "titulo": "Celeste",
                    "genero": "PLATAFORMA",
                    "anoLancamento": 2018,
                    "quantHorasJogadas": 20,
                    "zerado": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonAtualizado)
                .when()
                .put("/jogos/{id}", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo atualizado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Celeste"))
                .body("dados.genero", equalTo("PLATAFORMA"))
                .body("dados.anoLancamento", equalTo(2018))
                .body("dados.quantHorasJogadas", equalTo(20))
                .body("dados.zerado", equalTo(true));
    }

    @Test
    void naoDeveAtualizarJogoInexistente() {
        String jsonAtualizado = """
                {
                    "titulo": "Celeste",
                    "genero": "PLATAFORMA",
                    "anoLancamento": 2018,
                    "quantHorasJogadas": 20,
                    "zerado": true
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonAtualizado)
                .when()
                .put("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("encontrado"));
    }

    // ========================
    // Testes de remoção
    // ========================

    @Test
    void deveRemoverJogo() {
        Long id = criarJogo("Undertale");

        given()
                .when()
                .delete("/jogos/{id}", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo removido com sucesso."));

        given()
                .when()
                .get("/jogos/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void naoDeveRemoverJogoInexistente() {
        given()
                .when()
                .delete("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("encontrado"));
    }

    // ========================
    // Testes de PATCH
    // ========================

    @Test
    void deveZerarJogo() {
        Long id = criarJogo("Dark Souls", "RPG", 2011, 80, false);

        given()
                .when()
                .patch("/jogos/{id}/zerar", id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo zerado com sucesso."))
                .body("dados.id", equalTo(id.intValue()))
                .body("dados.titulo", equalTo("Dark Souls"))
                .body("dados.zerado", equalTo(true));
    }

    @Test
    void naoDeveZerarJogoInexistente() {
        given()
                .when()
                .patch("/jogos/{id}/zerar", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("encontrado"));
    }

    @Test
    void deveAdicionarHorasJogadas() {
        Long id = criarJogo("Skyrim", "RPG", 2011, 50, false);

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
                .body("dados.titulo", equalTo("Skyrim"))
                .body("dados.quantHorasJogadas", equalTo(60));
    }

    @Test
    void naoDeveAdicionarHorasEmJogoInexistente() {
        String json = """
                {
                    "horas": 10
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch("/jogos/{id}/horas", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", containsString("encontrado"));
    }

    // ========================
    // Testes de listagem
    // ========================

    @Test
    void deveListarJogos() {
        criarJogo("Hollow Knight", "AVENTURA", 2017, 45, true);
        criarJogo("Elden Ring", "RPG", 2022, 100, false);

        given()
                .when()
                .get("/jogos")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos listados com sucesso."))
                .body("dados", hasSize(2))
                .body("dados.titulo", containsInAnyOrder("Hollow Knight", "Elden Ring"));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverJogos() {
        given()
                .when()
                .get("/jogos")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Nenhum jogo encontrado."))
                .body("dados", hasSize(0));
    }

    @Test
    void deveListarJogosZerados() {
        criarJogo("God of War", "ACAO", 2018, 40, true);
        criarJogo("Elden Ring", "RPG", 2022, 90, false);

        given()
                .when()
                .get("/jogos/zerados")
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogos zerados listados com sucesso."))
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("God of War"))
                .body("dados[0].zerado", equalTo(true));
    }

    @Test
    void deveListarJogosNaoZerados() {
        criarJogo("God of War", "ACAO", 2018, 40, true);
        criarJogo("Elden Ring", "RPG", 2022, 90, false);

        given()
                .when()
                .get("/jogos/nao-zerados")
                .then()
                .statusCode(200)
                .body("mensagem", containsString("zerados"))
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("Elden Ring"))
                .body("dados[0].zerado", equalTo(false));
    }

    // ========================
    // Testes de filtro
    // ========================

    @Test
    void deveListarJogosPorGenero() {
        criarJogo("The Witcher 3", "RPG", 2015, 120, true);
        criarJogo("Street Fighter 6", "LUTA", 2023, 30, false);

        given()
                .when()
                .get("/jogos/genero/{genero}", "RPG")
                .then()
                .statusCode(200)
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("The Witcher 3"))
                .body("dados[0].genero", equalTo("RPG"));
    }

    @Test
    void deveAceitarGeneroComLetrasMinusculas() {
        criarJogo("The Witcher 3", "RPG", 2015, 120, true);

        given()
                .when()
                .get("/jogos/genero/{genero}", "rpg")
                .then()
                .statusCode(200)
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("The Witcher 3"))
                .body("dados[0].genero", equalTo("RPG"));
    }

    @Test
    void deveRetornar400QuandoGeneroForInvalido() {
        given()
                .when()
                .get("/jogos/genero/{genero}", "MMORPG")
                .then()
                .statusCode(400)
                .body("mensagem", containsString("Valores permitidos"));
    }

    @Test
    void deveListarJogosPorAno() {
        criarJogo("Portal 2", "PUZZLE", 2011, 12, true);
        criarJogo("Elden Ring", "RPG", 2022, 100, false);

        given()
                .when()
                .get("/jogos/ano/{ano}", 2011)
                .then()
                .statusCode(200)
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("Portal 2"))
                .body("dados[0].anoLancamento", equalTo(2011));
    }

    @Test
    void deveListarJogosComMaisDeDeterminadaQuantidadeDeHoras() {
        criarJogo("Celeste", "PLATAFORMA", 2018, 20, true);
        criarJogo("The Witcher 3", "RPG", 2015, 120, true);

        given()
                .when()
                .get("/jogos/horas-jogadas-mais/{horas}", 50)
                .then()
                .statusCode(200)
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("The Witcher 3"))
                .body("dados[0].quantHorasJogadas", equalTo(120));
    }

    @Test
    void deveListarJogosComMenosDeDeterminadaQuantidadeDeHoras() {
        criarJogo("Celeste", "PLATAFORMA", 2018, 20, true);
        criarJogo("The Witcher 3", "RPG", 2015, 120, true);

        given()
                .when()
                .get("/jogos/horas-jogadas-menos/{horas}", 50)
                .then()
                .statusCode(200)
                .body("dados", hasSize(1))
                .body("dados[0].titulo", equalTo("Celeste"))
                .body("dados[0].quantHorasJogadas", equalTo(20));
    }

    // ========================
    // Testes de ordenação
    // ========================

    @Test
    void deveListarJogosOrdenadosPorTitulo() {
        criarJogo("Zelda", "AVENTURA", 2017, 80, true);
        criarJogo("Animal Crossing", "SIMULACAO", 2020, 200, false);
        criarJogo("Celeste", "PLATAFORMA", 2018, 25, true);

        given()
                .when()
                .get("/jogos/ordenar-por-titulo")
                .then()
                .statusCode(200)
                .body("dados", hasSize(3))
                .body("dados.titulo", contains("Animal Crossing", "Celeste", "Zelda"));
    }

    @Test
    void deveListarJogosOrdenadosPorGenero() {
        criarJogo("The Witcher 3", "RPG", 2015, 120, true);
        criarJogo("God of War", "ACAO", 2018, 40, true);
        criarJogo("Portal 2", "PUZZLE", 2011, 12, true);

        given()
                .when()
                .get("/jogos/ordenar-por-genero")
                .then()
                .statusCode(200)
                .body("dados", hasSize(3))
                .body("dados.genero", contains("ACAO", "PUZZLE", "RPG"))
                .body("dados.titulo", contains("God of War", "Portal 2", "The Witcher 3"));
    }

    @Test
    void deveListarJogosOrdenadosPorAno() {
        criarJogo("Elden Ring", "RPG", 2022, 100, false);
        criarJogo("Portal 2", "PUZZLE", 2011, 12, true);
        criarJogo("God of War", "ACAO", 2018, 40, true);

        given()
                .when()
                .get("/jogos/ordenar-por-ano")
                .then()
                .statusCode(200)
                .body("dados", hasSize(3))
                .body("dados.anoLancamento", contains(2011, 2018, 2022))
                .body("dados.titulo", contains("Portal 2", "God of War", "Elden Ring"));
    }

    @Test
    void deveListarJogosOrdenadosPorHorasJogadas() {
        criarJogo("Jogo A", "RPG", 2015, 100, true);
        criarJogo("Jogo B", "ACAO", 2018, 20, false);
        criarJogo("Jogo C", "PUZZLE", 2011, 50, true);

        given()
                .when()
                .get("/jogos/ordenar-por-horas-jogadas")
                .then()
                .statusCode(200)
                .body("dados", hasSize(3))
                .body("dados.quantHorasJogadas", contains(20, 50, 100))
                .body("dados.titulo", contains("Jogo B", "Jogo C", "Jogo A"));
    }
}
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
        String json = """
            {
                "titulo": "%s",
                "genero": "RPG",
                "anoLancamento": 2015,
                "quantHorasJogadas": 120,
                "zerado": true
            }
            """.formatted(titulo);

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
                .body("dados.genero", equalTo("ACAO"));
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
                .body("mensagem", equalTo("Já existe um jogo cadastrado com esse título."));
    }

    @Test
    void naoDeveCadastrarJogoComDadosInvalidos() {
        String json = """
                {
                    "titulo": "",
                    "genero": null,
                    "anoLancamento": -1,
                    "quantHorasJogadas": -5,
                    "zerado": null
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
                .get("/jogos/" + id)
                .then()
                .statusCode(200)
                .body("dados.titulo", equalTo("Hollow Knight"))
                .body("dados.id", equalTo(id.intValue()));
    }

    @Test
    void deveRetornar404QuandoJogoNaoEncontrado() {
        given()
                .when()
                .get("/jogos/99999")
                .then()
                .statusCode(404)
                .body("mensagem", containsString("não encontrado"));
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
                .put("/jogos/" + id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo atualizado com sucesso."))
                .body("dados.zerado", equalTo(true))
                .body("dados.genero", equalTo("PLATAFORMA"));
    }

    // ========================
    // Testes de remoção
    // ========================

    @Test
    void deveRemoverJogo() {
        Long id = criarJogo("Undertale");

        given()
                .when()
                .delete("/jogos/" + id)
                .then()
                .statusCode(200)
                .body("mensagem", equalTo("Jogo removido com sucesso."));

        given()
                .when()
                .get("/jogos/" + id)
                .then()
                .statusCode(404);
    }
}
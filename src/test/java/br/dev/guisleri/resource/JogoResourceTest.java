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
    void deveCadastrarJogo() {
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
    void naoDeveCadastrarComDadosInvalidos() {
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
    void buscarPorIdComSucesso() {
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
    void buscarPorIdInexistente() {
        given()
                .when()
                .get("/jogos/{id}", 99999)
                .then()
                .statusCode(404)
                .body("mensagem", equalTo("Jogo com ID " + 99999 + " não encontrado."))
                .body("dados", nullValue());
    }

    @Test
    void buscarPorTituloComSucesso() {
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
    void buscarPorTituloInexistente() {
        given()
                .when()
                .get("/jogos/{titulo}", "Jogo Inexistente")
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
                .body("dados", notNullValue());
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
    void deveAdicionarHorasJogadasComSucesso() {
        Long id = criarJogo("Forza Horizon 6");

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
                .body("dados.quantHorasJogadas", equalTo(130));
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

}
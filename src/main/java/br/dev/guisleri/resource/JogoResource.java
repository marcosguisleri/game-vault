package br.dev.guisleri.resource;

import br.dev.guisleri.dto.RespostaApiDTO;
import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;
import br.dev.guisleri.service.JogoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/jogos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JogoResource {

    @Inject
    JogoService jogoService;

    @POST
    public Response adicionarJogo(@Valid Jogo jogo) {
        jogoService.adicionarJogo(jogo);
        return Response.status(Response.Status.CREATED)
                .entity(RespostaApiDTO.comDados("Jogo cadastrado com sucesso!", jogo))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarJogo(@PathParam("id") Long id, @Valid Jogo dadosAtualizados) {
        Jogo jogoAtualizado = jogoService.atualizarJogo(id, dadosAtualizados);
        return Response.ok(RespostaApiDTO.comDados("Jogo atualizado com sucesso.", jogoAtualizado)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removerJogo(@PathParam("id") Long id) {
        jogoService.removerJogo(id);
        return Response.ok(RespostaApiDTO.semDados("Jogo removido com sucesso.")).build();
    }

    // Buscas
    @GET
    @Path("/{id}")
    public Response buscarJogoPorId(@PathParam("id") Long id) {
        Jogo jogo = jogoService.buscarJogoPorId(id);
        return Response.ok(RespostaApiDTO.comDados("Jogo encontrado com sucesso.", jogo)).build();
    }

    @GET
    @Path("/titulo/{titulo}")
    public Response buscarJogoPorTitulo(@PathParam("titulo") String titulo) {
        Jogo jogo = jogoService.buscarJogoPorTitulo(titulo);
        return Response.ok(RespostaApiDTO.comDados("Jogo encontrado com sucesso.", jogo)).build();
    }

    // Listagens
    @GET
    public Response listarJogos() {
        List<Jogo> jogos = jogoService.listarJogos();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos listados com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/zerados")
    public Response listarJogosZerados() {
        List<Jogo> jogos = jogoService.listarJogosZerados();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo zerado encontrado.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos zerados listados com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/nao-zerados")
    public Response listarJogosNaoZerados() {
        List<Jogo> jogos = jogoService.listarJogosNaoZerados();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo não zerado encontrado.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos não zerados listados com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/genero/{genero}")
    public Response listarJogosPorGenero(@PathParam("genero") String generoTexto) {
        Genero genero;

        try {
            genero = Genero.valueOf(generoTexto.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespostaApiDTO.semDados(
                    "Gênero inválido. Valores permitidos: ACAO, AVENTURA, RPG, FPS, LUTA, ESPORTE, CORRIDA, ESTRATEGIA, TERROR, PLATAFORMA, SIMULACAO, PUZZLE"))
                    .build();
        }

        List<Jogo> jogos = jogoService.listarJogosPorGenero(genero);

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado nesse gênero.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos do gênero " + genero + " listados com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/ano/{ano}")
    public Response listarJogosPorAno(@PathParam("ano") int ano) {
        List<Jogo> jogos = jogoService.listarJogosPorAno(ano);

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado para o ano informado.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos lançados em " + ano + " listados com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/horas-jogadas-mais/{horas}")
    public Response listarJogosPorHorasJogadasMais(@PathParam("horas") int horas) {
        List<Jogo> jogos = jogoService.listarJogosPorHorasJogadasMais(horas);

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado com pelo menos " + horas + " horas jogadas.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos com pelo menos " + horas + " horas jogadas listados com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/horas-jogadas-menos/{horas}")
    public Response listarJogosPorHorasJogadasMenos(@PathParam("horas") int horas) {
        List<Jogo> jogos = jogoService.listarJogosPorHorasJogadasMenos(horas);

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado com até " + horas + " horas jogadas.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos com até " + horas + " horas jogadas listados com sucesso.", jogos)
        ).build();
    }

    // Ordenações
    @GET
    @Path("/ordenar-por-titulo")
    public Response listarJogosOrdenadosPorTitulo() {
        List<Jogo> jogos = jogoService.listarJogosOrdenadosPorTitulo();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado para ordenar por título.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos ordenados por título com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/ordenar-por-genero")
    public Response listarJogosOrdenadosPorGenero() {
        List<Jogo> jogos = jogoService.listarJogosOrdenadosPorGenero();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado para ordenar por gênero.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos ordenados por gênero com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/ordenar-por-ano")
    public Response listarJogosOrdenadosPorAno() {
        List<Jogo> jogos = jogoService.listarJogosOrdenadosPorAno();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado para ordenar por ano.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos ordenados por ano de lançamento com sucesso.", jogos)
        ).build();
    }

    @GET
    @Path("/ordenar-por-horas-jogadas")
    public Response listarJogosOrdenadosPorHorasJogadas() {
        List<Jogo> jogos = jogoService.listarJogosOrdenadosPorHorasJogadas();

        if (jogos.isEmpty()) {
            return Response.ok(
                    RespostaApiDTO.comDados("Nenhum jogo encontrado para ordenar por horas jogadas.", jogos)
            ).build();
        }

        return Response.ok(
                RespostaApiDTO.comDados("Jogos ordenados por horas jogadas com sucesso.", jogos)
        ).build();
    }

}

package br.dev.guisleri.resource;

import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;
import br.dev.guisleri.service.JogoService;
import jakarta.inject.Inject;
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
    public Response adicionarJogo(Jogo jogo) {
        jogoService.adicionarJogo(jogo);
        return Response.status(Response.Status.CREATED).entity(jogo).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarJogo(@PathParam("id") Long id, Jogo dadosAtualizados) {
        Jogo jogo = jogoService.buscarJogoPorId(id);
        if (jogo == null) return Response.status(Response.Status.NOT_FOUND).build();
        jogoService.atualizarJogo(id, dadosAtualizados);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response removerJogo(@PathParam("id") Long id) {
        Jogo jogo = jogoService.buscarJogoPorId(id);
        if (jogo == null) return Response.status(Response.Status.NOT_FOUND).build();
        jogoService.removerJogo(id);
        return Response.ok().build();
    }

    // Buscas
    @GET
    @Path("/{id}")
    public Jogo buscarJogoPorId(@PathParam("id") Long id) {
        return jogoService.buscarJogoPorId(id);
    }

    @GET
    @Path("/titulo/{titulo}")
    public Jogo buscarJogoPorTitulo(@PathParam("titulo") String titulo) {
        return jogoService.buscarJogoPorTitulo(titulo);
    }

    // Listagens
    @GET
    public List<Jogo> listarJogos() {
        return jogoService.listarJogos();
    }

    @GET
    @Path("/zerados")
    public List<Jogo> listarJogosZerados() {
        return jogoService.listarJogosZerados();
    }

    @GET
    @Path("/nao-zerados")
    public List<Jogo> listarJogosNaoZerados() {
        return jogoService.listarJogosNaoZerados();
    }

    @GET
    @Path("/genero/{genero}")
    public List<Jogo> listarJogosPorGenero(@PathParam("genero") Genero genero) {
        return jogoService.listarJogosPorGenero(genero);
    }

    @GET
    @Path("/ano/{ano}")
    public List<Jogo> listarJogosPorAno(@PathParam("ano") int ano) {
        return jogoService.listarJogosPorAno(ano);
    }

    @GET
    @Path("/horas-jogadas-mais/{horas}")
    public List<Jogo> listarJogosPorHorasJogadasMais(@PathParam("horas") int horas) {
        return jogoService.listarJogosPorHorasJogadasMais(horas);
    }

    @GET
    @Path("/horas-jogadas-menos/{horas}")
    public List<Jogo> listarJogosPorHorasJogadasMenos(@PathParam("horas") int horas) {
        return jogoService.listarJogosPorHorasJogadasMenos(horas);
    }

    // Ordenações
    @GET
    @Path("/ordenar-por-titulo")
    public List<Jogo> listarJogosOrdenadosPorTitulo() {
        return jogoService.listarJogosOrdenadosPorTitulo();
    }

    @GET
    @Path("/ordenar-por-genero")
    public List<Jogo> listarJogosOrdenadosPorGenero() {
        return jogoService.listarJogosOrdenadosPorGenero();
    }

    @GET
    @Path("/ordenar-por-ano")
    public List<Jogo> listarJogosOrdenadosPorAno() {
        return jogoService.listarJogosOrdenadosPorAno();
    }

    @GET
    @Path("/ordenar-por-horas-jogadas")
    public List<Jogo> listarJogosOrdenadosPorHorasJogadas() {
        return jogoService.listarJogosOrdenadosPorHorasJogadas();
    }


}

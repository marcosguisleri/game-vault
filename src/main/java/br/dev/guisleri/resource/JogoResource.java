package br.dev.guisleri.resource;

import br.dev.guisleri.dto.AdicionarHorasDTO;
import br.dev.guisleri.dto.JogoRequestDTO;
import br.dev.guisleri.dto.JogoResponseDTO;
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
    public Response adicionarJogo(@Valid JogoRequestDTO dto) {
        Jogo jogo = new Jogo(dto.titulo(), dto.genero(), dto.anoLancamento(), dto.quantHorasJogadas(), dto.zerado());
        jogoService.adicionarJogo(jogo);
        return Response.status(Response.Status.CREATED)
                .entity(RespostaApiDTO.comDados("Jogo cadastrado com sucesso!", JogoResponseDTO.from(jogo)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizarJogo(@PathParam("id") Long id, @Valid JogoRequestDTO dto) {
        Jogo dadosAtualizados = new Jogo(dto.titulo(), dto.genero(), dto.anoLancamento(), dto.quantHorasJogadas(), dto.zerado());
        Jogo jogoAtualizado = jogoService.atualizarJogo(id, dadosAtualizados);
        return Response.ok(RespostaApiDTO.comDados("Jogo atualizado com sucesso.", JogoResponseDTO.from(jogoAtualizado))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removerJogo(@PathParam("id") Long id) {
        jogoService.removerJogo(id);
        return Response.ok(RespostaApiDTO.semDados("Jogo removido com sucesso.")).build();
    }

    @PATCH
    @Path("/{id}/zerar")
    public Response zerarJogo(@PathParam("id") Long id) {
        Jogo jogo = jogoService.zerarJogo(id);
        return Response.ok(RespostaApiDTO.comDados("Jogo zerado com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    @PATCH
    @Path("/{id}/horas")
    public Response adicionarHorasJogadas(@PathParam("id") Long id, @Valid AdicionarHorasDTO dto) {
        Jogo jogo = jogoService.adicionarHorasJogadas(id, dto.horas());
        return Response.ok(RespostaApiDTO.comDados("Horas adicionadas com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    // Buscas
    @GET
    @Path("/{id}")
    public Response buscarJogoPorId(@PathParam("id") Long id) {
        Jogo jogo = jogoService.buscarJogoPorId(id);
        return Response.ok(RespostaApiDTO.comDados("Jogo encontrado com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    @GET
    @Path("/titulo/{titulo}")
    public Response buscarJogoPorTitulo(@PathParam("titulo") String titulo) {
        Jogo jogo = jogoService.buscarJogoPorTitulo(titulo);
        return Response.ok(RespostaApiDTO.comDados("Jogo encontrado com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    // Listagens
    private Response listarComMensagem(List<Jogo> jogos, String msgSucesso, String msgVazia) {
        List<JogoResponseDTO> dtos = jogos.stream().map(JogoResponseDTO::from).toList();
        String mensagem = jogos.isEmpty() ? msgVazia : msgSucesso;
        return Response.ok(RespostaApiDTO.comDados(mensagem, dtos)).build();
    }

    @GET
    public Response listarJogos() {
        return listarComMensagem(jogoService.listarJogos(), "Jogos listados com sucesso.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/zerados")
    public Response listarJogosZerados() {
        return listarComMensagem(jogoService.listarJogosZerados(), "Jogos zerados listados com sucesso.", "Nenhum jogo zerado encontrado.");
    }

    @GET
    @Path("/nao-zerados")
    public Response listarJogosNaoZerados() {
        return listarComMensagem(jogoService.listarJogosNaoZerados(), "Jogos não zerados listados com sucesso.", "Nenhum jogo não zerado encontrado.");
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

        return listarComMensagem(jogoService.listarJogosPorGenero(genero), "Jogos do gênero " + genero + " listados com sucesso.", "Nenhum jogo encontrado nesse gênero.");
    }

    @GET
    @Path("/ano/{ano}")
    public Response listarJogosPorAno(@PathParam("ano") int ano) {
        return listarComMensagem(jogoService.listarJogosPorAno(ano), "Jogos do ano " + ano + " listados com sucesso.", "Nenhum jogo encontrado nesse ano.");
    }

    @GET
    @Path("/horas-jogadas-mais/{horas}")
    public Response listarJogosPorHorasJogadasMais(@PathParam("horas") int horas) {
        return listarComMensagem(jogoService.listarJogosPorHorasJogadasMais(horas), "Jogos com mais de " + horas + " horas jogadas listados com sucesso.", "Nenhum jogo encontrado com mais de " + horas + " horas jogadas.");
    }

    @GET
    @Path("/horas-jogadas-menos/{horas}")
    public Response listarJogosPorHorasJogadasMenos(@PathParam("horas") int horas) {
        return listarComMensagem(jogoService.listarJogosPorHorasJogadasMenos(horas), "Jogos com menos de " + horas + " horas jogadas listados com sucesso.", "Nenhum jogo encontrado com menos de " + horas + " horas jogadas.");
    }

    // Ordenações
    @GET
    @Path("/ordenar-por-titulo")
    public Response listarJogosOrdenadosPorTitulo() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorTitulo(), "Jogos ordenados por título.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/ordenar-por-genero")
    public Response listarJogosOrdenadosPorGenero() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorGenero(), "Jogos ordenados por gênero.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/ordenar-por-ano")
    public Response listarJogosOrdenadosPorAno() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorAno(), "Jogos ordenados por ano.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/ordenar-por-horas-jogadas")
    public Response listarJogosOrdenadosPorHorasJogadas() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorHorasJogadas(), "Jogos ordenados por horas jogadas.", "Nenhum jogo encontrado.");
    }

}

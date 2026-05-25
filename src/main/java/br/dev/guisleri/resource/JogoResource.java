package br.dev.guisleri.resource;

import br.dev.guisleri.dto.*;
import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;
import br.dev.guisleri.service.JogoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/jogos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Jogos", description = "Gerenciamento da biblioteca de jogos")
public class JogoResource {

    @Inject
    JogoService jogoService;

    @POST
    @Operation(summary = "Cadastrar jogo", description = "Cadastra um novo jogo na biblioteca")
    @APIResponse(responseCode = "201", description = "Jogo cadastrado com sucesso")
    @APIResponse(responseCode = "409", description = "Jogo já cadastrado")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    public Response adicionarJogo(@Valid JogoRequestDTO dto) {
        Jogo jogo = new Jogo(dto.titulo(), dto.genero(), dto.anoLancamento(), dto.quantHorasJogadas(), dto.zerado());
        jogoService.adicionarJogo(jogo);
        return Response.status(Response.Status.CREATED)
                .entity(RespostaApiDTO.comDados("Jogo cadastrado com sucesso!", JogoResponseDTO.from(jogo)))
                .build();
    }

    @POST
    @Path("/lote")
    @Operation(summary = "Cadastrar em lote", description = "Cadastra múltiplos jogos e retorna relatório de sucessos e falhas")
    @APIResponse(responseCode = "207", description = "Relatório do processamento em lote")
    public Response adicionarJogosEmLote(List<JogoLoteItemDTO> dtos) {
        CadastroLoteResponseDTO resultado = jogoService.adicionarJogosEmLote(dtos);
        return Response.status(207)
                .entity(RespostaApiDTO.comDados("Processamento em lote concluído.", resultado))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar jogo", description = "Atualiza os dados de um jogo existente")
    @APIResponse(responseCode = "200", description = "Jogo atualizado com sucesso")
    @APIResponse(responseCode = "404", description = "Jogo não encontrado")
    public Response atualizarJogo(@PathParam("id") Long id, @Valid JogoRequestDTO dto) {
        Jogo dadosAtualizados = new Jogo(dto.titulo(), dto.genero(), dto.anoLancamento(), dto.quantHorasJogadas(), dto.zerado());
        Jogo jogoAtualizado = jogoService.atualizarJogo(id, dadosAtualizados);
        return Response.ok(RespostaApiDTO.comDados("Jogo atualizado com sucesso.", JogoResponseDTO.from(jogoAtualizado))).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remover jogo", description = "Remove um jogo pelo ID")
    @APIResponse(responseCode = "200", description = "Jogo removido com sucesso")
    @APIResponse(responseCode = "404", description = "Jogo não encontrado")
    public Response removerJogo(@PathParam("id") Long id) {
        jogoService.removerJogo(id);
        return Response.ok(RespostaApiDTO.semDados("Jogo removido com sucesso.")).build();
    }

    @DELETE
    @Operation(summary = "Remover todos os jogos", description = "Remove todos os jogos. Requer ?confirmar=true")
    @APIResponse(responseCode = "200", description = "Todos os jogos removidos com sucesso")
    @APIResponse(responseCode = "400", description = "Confirmação não enviada")
    public Response removerTodosJogos(@QueryParam("confirmar") boolean confirmar) {
        if (!confirmar) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespostaApiDTO.semDados("Envie ?confirmar=true para confirmar a operação."))
                    .build();
        }
        jogoService.removerTodosJogos();
        return Response.ok(RespostaApiDTO.semDados("Todos os jogos removidos com sucesso.")).build();
    }

    @PATCH
    @Path("/{id}/zerar")
    @Operation(summary = "Zerar jogo", description = "Marca um jogo como zerado")
    @APIResponse(responseCode = "200", description = "Jogo zerado com sucesso")
    @APIResponse(responseCode = "409", description = "Jogo já zerado")
    @APIResponse(responseCode = "404", description = "Jogo não encontrado")
    public Response zerarJogo(@PathParam("id") Long id) {
        Jogo jogo = jogoService.zerarJogo(id);
        return Response.ok(RespostaApiDTO.comDados("Jogo zerado com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    @PATCH
    @Path("/{id}/horas")
    @Operation(summary = "Adicionar horas jogadas", description = "Incrementa as horas jogadas de um jogo")
    @APIResponse(responseCode = "200", description = "Horas adicionadas com sucesso")
    @APIResponse(responseCode = "404", description = "Jogo não encontrado")
    public Response adicionarHorasJogadas(@PathParam("id") Long id, @Valid AdicionarHorasDTO dto) {
        Jogo jogo = jogoService.adicionarHorasJogadas(id, dto.horas());
        return Response.ok(RespostaApiDTO.comDados("Horas adicionadas com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    // Buscas
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar por ID")
    @APIResponse(responseCode = "200", description = "Jogo encontrado")
    @APIResponse(responseCode = "404", description = "Jogo não encontrado")
    public Response buscarJogoPorId(@PathParam("id") Long id) {
        Jogo jogo = jogoService.buscarJogoPorId(id);
        return Response.ok(RespostaApiDTO.comDados("Jogo encontrado com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    @GET
    @Path("/titulo/{titulo}")
    @Operation(summary = "Buscar por título")
    @APIResponse(responseCode = "200", description = "Jogo encontrado")
    @APIResponse(responseCode = "404", description = "Jogo não encontrado")
    public Response buscarJogoPorTitulo(@PathParam("titulo") String titulo) {
        Jogo jogo = jogoService.buscarJogoPorTitulo(titulo);
        return Response.ok(RespostaApiDTO.comDados("Jogo encontrado com sucesso.", JogoResponseDTO.from(jogo))).build();
    }

    // Listagens
    private Response listarComMensagem(List<Jogo> jogos, String msgSucesso, String msgVazia) {
        if (jogos.isEmpty()) {
            return Response.ok(RespostaApiDTO.semDados(msgVazia)).build();
        }
        List<JogoResponseDTO> dtos = jogos.stream().map(JogoResponseDTO::from).toList();
        return Response.ok(RespostaApiDTO.comDados(msgSucesso, dtos)).build();
    }

    @GET
    @Operation(summary = "Listar todos os jogos")
    public Response listarJogos() {
        return listarComMensagem(jogoService.listarJogos(), "Jogos listados com sucesso.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/zerados")
    @Operation(summary = "Listar jogos zerados")
    public Response listarJogosZerados() {
        return listarComMensagem(jogoService.listarJogosZerados(), "Jogos zerados listados com sucesso.", "Nenhum jogo zerado encontrado.");
    }

    @GET
    @Path("/nao-zerados")
    @Operation(summary = "Listar jogos não zerados")
    public Response listarJogosNaoZerados() {
        return listarComMensagem(jogoService.listarJogosNaoZerados(), "Jogos não zerados listados com sucesso.", "Nenhum jogo não zerado encontrado.");
    }

    @GET
    @Path("/genero/{genero}")
    @Operation(summary = "Listar por gênero")
    @APIResponse(responseCode = "400", description = "Gênero inválido")
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
    @Operation(summary = "Listar por ano de lançamento")
    public Response listarJogosPorAno(@PathParam("ano") int ano) {
        return listarComMensagem(jogoService.listarJogosPorAno(ano), "Jogos do ano " + ano + " listados com sucesso.", "Nenhum jogo encontrado nesse ano.");
    }

    @GET
    @Path("/horas-jogadas-mais/{horas}")
    @Operation(summary = "Listar com X horas ou mais")
    public Response listarJogosPorHorasJogadasMais(@PathParam("horas") int horas) {
        return listarComMensagem(jogoService.listarJogosPorHorasJogadasMais(horas), "Jogos com ou mais de " + horas + " horas jogadas listados com sucesso.", "Nenhum jogo encontrado com mais de " + horas + " horas jogadas.");
    }

    @GET
    @Path("/horas-jogadas-menos/{horas}")
    @Operation(summary = "Listar com menos de X horas")
    public Response listarJogosPorHorasJogadasMenos(@PathParam("horas") int horas) {
        return listarComMensagem(jogoService.listarJogosPorHorasJogadasMenos(horas), "Jogos com menos de " + horas + " horas jogadas listados com sucesso.", "Nenhum jogo encontrado com menos de " + horas + " horas jogadas.");
    }

    // Ordenações
    @GET
    @Path("/ordenar-por-titulo")
    @Operation(summary = "Listar ordenado por título")
    public Response ordernarJogosOrdenadosPorTitulo() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorTitulo(), "Jogos ordenados por título.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/ordenar-por-genero")
    @Operation(summary = "Listar ordenado por gênero")
    public Response ordernarJogosOrdenadosPorGenero() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorGenero(), "Jogos ordenados por gênero.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/ordenar-por-ano")
    @Operation(summary = "Listar ordenado por ano")
    public Response ordernarJogosOrdenadosPorAno() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorAno(), "Jogos ordenados por ano.", "Nenhum jogo encontrado.");
    }

    @GET
    @Path("/ordenar-por-horas-jogadas")
    @Operation(summary = "Listar ordenado por horas jogadas")
    public Response ordernarJogosOrdenadosPorHorasJogadas() {
        return listarComMensagem(jogoService.listarJogosOrdenadosPorHorasJogadas(), "Jogos ordenados por horas jogadas.", "Nenhum jogo encontrado.");
    }

}

package br.dev.guisleri.service;

import br.dev.guisleri.dto.CadastroLoteResponseDTO;
import br.dev.guisleri.dto.JogoRequestDTO;
import br.dev.guisleri.dto.ResultadoItemLoteDTO;
import br.dev.guisleri.exception.JogoJaCadastradoException;
import br.dev.guisleri.exception.JogoJaZeradoException;
import br.dev.guisleri.exception.JogoNaoEncontradoException;
import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class JogoService {

    // CRUD
    @Transactional
    public void adicionarJogo(Jogo jogo) {
        boolean jaExiste = Jogo.find("LOWER(titulo) = LOWER(?1)", jogo.titulo)
                .firstResultOptional()
                .isPresent();

        if (jaExiste) {
            throw new JogoJaCadastradoException("Já existe um jogo cadastrado com esse título.");
        }

        jogo.persist();
    }

    @Transactional
    public CadastroLoteResponseDTO adicionarJogosEmLote(List<JogoRequestDTO> dtos) {
        List<ResultadoItemLoteDTO> resultados = new ArrayList<>();

        for (JogoRequestDTO dto : dtos) {
            try {
                boolean jaExiste = Jogo.find("LOWER(titulo) = LOWER(?1)", dto.titulo())
                        .firstResultOptional()
                        .isPresent();

                if (jaExiste) {
                    throw new JogoJaCadastradoException("Já existe um jogo cadastrado com esse título.");
                }

                Jogo jogo = new Jogo(dto.titulo(), dto.genero(), dto.anoLancamento(), dto.quantHorasJogadas(), dto.zerado());
                jogo.persist();

                resultados.add(new ResultadoItemLoteDTO(dto.titulo(), true, "Cadastrado com sucesso."));

            } catch (JogoJaCadastradoException e) {
                resultados.add(new ResultadoItemLoteDTO(dto.titulo(), false, e.getMessage()));
            }
        }

        return CadastroLoteResponseDTO.from(resultados);
    }

    @Transactional
    public Jogo atualizarJogo(Long id, Jogo dadosAtualizados) {
        Jogo jogo = buscarJogoPorId(id);

        jogo.titulo = dadosAtualizados.titulo;
        jogo.genero = dadosAtualizados.genero;
        jogo.anoLancamento = dadosAtualizados.anoLancamento;
        jogo.quantHorasJogadas = dadosAtualizados.quantHorasJogadas;
        jogo.zerado = dadosAtualizados.zerado;

        return jogo;
    }

    @Transactional
    public void removerJogo(Long id) {
        buscarJogoPorId(id);
        Jogo.deleteById(id);
    }

    @Transactional
    public void removerTodosJogos() {
        Jogo.deleteAll();
    }

    @Transactional
    public Jogo zerarJogo(Long id) {
        Jogo jogo = buscarJogoPorId(id);

        if (jogo.isZerado()) {
            throw new JogoJaZeradoException("Este jogo já está zerado.");
        }

        jogo.zerarJogo();
        return jogo;
    }

    @Transactional
    public Jogo adicionarHorasJogadas(Long id, int horas) {
        Jogo jogo = buscarJogoPorId(id);
        jogo.adicionarHorasJogadas(horas);
        return jogo;
    }

    // Buscas
    public Jogo buscarJogoPorId(Long id) {
        return Jogo.<Jogo>findByIdOptional(id)
                .orElseThrow(() -> new JogoNaoEncontradoException("Jogo com ID " + id + " não encontrado."));
    }

    public Jogo buscarJogoPorTitulo(String titulo) {
        return Jogo.<Jogo>find("LOWER(titulo) = LOWER(?1)", titulo)
                .firstResultOptional()
                .orElseThrow(() -> new JogoNaoEncontradoException("Jogo com título " + titulo + " não encontrado."));
    }

    // Listagens
    public List<Jogo> listarJogos() { return Jogo.listAll(); }
    public List<Jogo> listarJogosNaoZerados() { return Jogo.list("zerado", false); }
    public List<Jogo> listarJogosZerados() { return Jogo.list("zerado", true); }
    public List<Jogo> listarJogosPorGenero(Genero genero) { return Jogo.list("genero", genero); }
    public List<Jogo> listarJogosPorAno(int ano) { return Jogo.list("anoLancamento", ano); }
    public List<Jogo> listarJogosPorHorasJogadasMais(int horas) { return Jogo.list("quantHorasJogadas >= ?1", horas); }
    public List<Jogo> listarJogosPorHorasJogadasMenos(int horas) { return Jogo.list("quantHorasJogadas <= ?1", horas); }

    // Ordenações
    public List<Jogo> listarJogosOrdenadosPorTitulo() { return Jogo.listAll(Sort.by("titulo")); }
    public List<Jogo> listarJogosOrdenadosPorGenero() { return Jogo.listAll(Sort.by("genero")); }
    public List<Jogo> listarJogosOrdenadosPorAno() { return Jogo.listAll(Sort.by("anoLancamento")); }
    public List<Jogo> listarJogosOrdenadosPorHorasJogadas() { return Jogo.listAll(Sort.by("quantHorasJogadas")); }
}
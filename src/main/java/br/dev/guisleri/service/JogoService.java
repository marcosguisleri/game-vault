package br.dev.guisleri.service;

import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class JogoService {

    // CRUD
    @Transactional
    public void adicionarJogo(Jogo jogo) {
        jogo.persist();
    }

    @Transactional
    public void atualizarJogo(Long id, Jogo dadosAtualizados) {
        Jogo jogo = Jogo.findById(id);

        if (jogo != null) {
            jogo.titulo = dadosAtualizados.titulo;
            jogo.genero = dadosAtualizados.genero;
            jogo.anoLancamento = dadosAtualizados.anoLancamento;
            jogo.quantHorasJogadas = dadosAtualizados.quantHorasJogadas;
            jogo.zerado = dadosAtualizados.zerado;
        }
    }

    @Transactional
    public void removerJogo(Long id) {
        Jogo.deleteById(id);
    }

    // Buscas
    public Jogo buscarJogoPorId(Long id) {
        return Jogo.findById(id);
    }

    public Jogo buscarJogoPorTitulo(String titulo) {
        return Jogo.find("LOWER(titulo) = LOWER(?1)", titulo).firstResult();
    }

    // Listagens
    public List<Jogo> listarJogos() {
        return Jogo.listAll();
    }

    public List<Jogo> listarJogosNaoZerados() {
        return Jogo.list("zerado", false);
    }

    public List<Jogo> listarJogosZerados() {
        return Jogo.list("zerado", true);
    }

    public List<Jogo> listarJogosPorGenero(Genero genero) {
        return Jogo.list("genero", genero);
    }

    public List<Jogo> listarJogosPorAno(int ano) {
        return Jogo.list("anoLancamento", ano);
    }

    public List<Jogo> listarJogosPorHorasJogadasMais(int horas) {
        return Jogo.list("quantHorasJogadas >= ?1", horas);
    }

    public List<Jogo> listarJogosPorHorasJogadasMenos(int horas) {
        return Jogo.list("quantHorasJogadas <= ?1", horas);
    }

    // Ordenações
    public List<Jogo> listarJogosOrdenadosPorTitulo() {
        return Jogo.listAll(Sort.by("titulo"));
    }

    public List<Jogo> listarJogosOrdenadosPorGenero() {
        return Jogo.listAll(Sort.by("genero"));
    }

    public List<Jogo> listarJogosOrdenadosPorAno() {
        return Jogo.listAll(Sort.by("anoLancamento"));
    }

    public List<Jogo> listarJogosOrdenadosPorHorasJogadas() {
        return Jogo.listAll(Sort.by("quantHorasJogadas"));
    }

}

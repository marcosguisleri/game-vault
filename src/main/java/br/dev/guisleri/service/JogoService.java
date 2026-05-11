package br.dev.guisleri.service;

import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JogoService {

    private List<Jogo> jogos = new ArrayList<>();

    public void adicionarJogo(Jogo jogo) {
        this.jogos.add(jogo);
    }

    public List<Jogo> listarJogos() {
        return Collections.unmodifiableList(this.jogos);
    }

    public List<Jogo> listarJogosNaoZerados() {
        validarListaDeJogosVazia();
        return this.jogos.stream().filter(jogo -> !jogo.isZerado()).toList();
    }

    public List<Jogo> listarJogosZerados() {
        validarListaDeJogosVazia();
        return this.jogos.stream().filter(Jogo::isZerado).toList();
    }

    public List<Jogo> listarJogosPorGenero(Genero genero) {
        validarListaDeJogosVazia();
        return this.jogos.stream().filter(jogo -> jogo.getGenero().equals(genero)).toList();
    }

    public List<Jogo> listarJogosPorAno(int ano) {
        validarListaDeJogosVazia();
        return this.jogos.stream().filter(jogo -> jogo.getAnoLancamento() == ano).toList();
    }

    public List<Jogo> listarJogosPorHorasJogadasMais(int quantHorasJogadas) {
        validarListaDeJogosVazia();
        return this.jogos.stream().filter(jogo -> jogo.getQuantHorasJogadas() >= quantHorasJogadas).toList();
    }

    public List<Jogo> listarJogosPorHorasJogadasMenos(int quantHorasJogadas) {
        validarListaDeJogosVazia();
        return this.jogos.stream().filter(jogo -> jogo.getQuantHorasJogadas() <= quantHorasJogadas).toList();
    }

    public Optional<Jogo> buscarJogoPorTitulo(String titulo) {
        validarTituloVazio(titulo);
        return this.jogos.stream().filter(jogo -> jogo.getTitulo().equalsIgnoreCase(titulo)).findFirst();
    }

    private void validarListaDeJogosVazia() {
        if (this.jogos.isEmpty()) {
            throw new RuntimeException("Lista de jogos vazia!");
        }
    }

    private void validarTituloVazio(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
    }

}

package br.dev.guisleri.service;

import br.dev.guisleri.model.Genero;
import br.dev.guisleri.model.Jogo;

import java.util.*;

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

    public int totalDeHorasJogadas() {
        validarListaDeJogosVazia();
        return this.jogos.stream().mapToInt(Jogo::getQuantHorasJogadas).sum();
    }

    public Optional<Jogo> jogoComMaisHorasJogadas() {
        validarListaDeJogosVazia();
        return jogos.stream().max(Comparator.comparing(Jogo::getQuantHorasJogadas));
    }

    public List<Jogo> jogosOrdenadosPorAnoLancamento() {
        validarListaDeJogosVazia();
        return this.jogos.stream().sorted(Comparator.comparing(Jogo::getAnoLancamento)).toList();
    }

    public List<Jogo> jogosOrdenadosPorHorasJogadas() {
        validarListaDeJogosVazia();
        return this.jogos.stream().sorted(Comparator.comparing(Jogo::getQuantHorasJogadas)).toList();
    }

    public List<Jogo> jogosOrdenadosPorTitulo() {
        validarListaDeJogosVazia();
        return this.jogos.stream().sorted(Comparator.comparing(Jogo::getTitulo)).toList();
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

package br.dev.guisleri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "jogos")
public class Jogo extends PanacheEntity {

    @Column(nullable = false)
    public String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Genero genero;

    @Column(name = "ano_lancamento", nullable = false)
    public int anoLancamento;

    @Column(name = "quant_horas_jogadas", nullable = false)
    public int quantHorasJogadas;

    @Column(nullable = false)
    public boolean zerado;

    public Jogo() {}

    public Jogo(String titulo, Genero genero, int anoLancamento, int quantHorasJogadas, boolean zerado) {
        this.titulo = titulo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.quantHorasJogadas = quantHorasJogadas;
        this.zerado = zerado;
    }

    public void zerarJogo() {
        this.zerado = true;
    }

    public boolean isZerado() {
        return zerado;
    }

    public void adicionarHorasJogadas(int horas) {
        if (horas <= 0) throw new IllegalArgumentException("Horas devem ser positivas.");
        this.quantHorasJogadas += horas;
    }

}

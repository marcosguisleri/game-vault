package br.dev.guisleri.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "jogos")
public class Jogo extends PanacheEntity {

    @Column(nullable = false)
    @NotBlank(message = "Título não pode ser vazio")
    public String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Gênero não pode ser nulo")
    public Genero genero;

    @Column(name = "ano_lancamento", nullable = false)
    @Min(value = 1958, message = "Ano lançamento inválido")
    public int anoLancamento;

    @Column(name = "quant_horas_jogadas", nullable = false)
    @PositiveOrZero(message = "Quantidade de horas jogadas deve ser positiva ou zero")
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

    public void adicionarHorasJogadas(int horas) {
        if (horas <= 0) throw new IllegalArgumentException("Horas devem ser positivas.");
        this.quantHorasJogadas += horas;
    }

}

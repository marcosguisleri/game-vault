package br.dev.guisleri.model;

public class Jogo {

    private String titulo;
    private Genero genero;
    private int anoLancamento;
    private int quantHorasJogadas;
    private boolean zerado;

    public Jogo(String titulo, Genero genero, int anoLancamento, int quantHorasJogadas, boolean zerado) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio.");
        }
        if (genero == null) {
            throw new IllegalArgumentException("Gênero não pode ser nulo.");
        }
        if (anoLancamento < 1958) {
            throw new IllegalArgumentException("Ano de lançamento inválido.");
        }
        if (quantHorasJogadas < 0) {
            throw new IllegalArgumentException("Horas jogadas não pode ser negativo.");
        }

        this.titulo = titulo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.quantHorasJogadas = quantHorasJogadas;
        this.zerado = zerado;
    }

    public void zerarJogo() {
        this.zerado = true;
    }

    public void adicionarHorasJogadas(int quantHorasJogadas) {
        if (quantHorasJogadas <= 0) {
            throw new IllegalArgumentException("Quantidade de horas deve ser positiva.");
        }
        this.quantHorasJogadas += quantHorasJogadas;
    }

    @Override
    public String toString() {
        return String.format(
                """
                
                === JOGO ===
                TÍTULO: %s
                GÊNERO: %s
                ANO LANÇAMENTO: %d
                QUANTIDADE HORAS: %d
                ZERADO? %s
                """,
                titulo,
                genero.name(),
                anoLancamento,
                quantHorasJogadas,
                zerado ? "Sim" : "Não"
        );
    }

    public String getTitulo() { return titulo; }
    public Genero getGenero() { return genero; }
    public int getAnoLancamento() { return anoLancamento; }
    public int getQuantHorasJogadas() { return quantHorasJogadas; }
    public boolean isZerado() { return zerado; }

}

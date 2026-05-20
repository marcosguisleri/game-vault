CREATE SEQUENCE jogos_SEQ START WITH 1 INCREMENT BY 50;

CREATE TABLE jogos
(
    id                  BIGINT PRIMARY KEY,
    titulo              VARCHAR(255) NOT NULL,
    genero              VARCHAR(50)  NOT NULL,
    ano_lancamento      INTEGER      NOT NULL,
    quant_horas_jogadas INTEGER      NOT NULL,
    zerado              BOOLEAN      NOT NULL DEFAULT FALSE
);
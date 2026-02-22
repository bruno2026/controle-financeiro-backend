CREATE TABLE categorias (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL UNIQUE,
    tipo       VARCHAR(20)  NOT NULL,
    criado_em  TIMESTAMP    NOT NULL DEFAULT NOW()
);


CREATE TABLE transacoes (
    id           BIGSERIAL       PRIMARY KEY,
    descricao    VARCHAR(255)    NOT NULL,
    valor        NUMERIC(15, 2)  NOT NULL,
    tipo         VARCHAR(20)     NOT NULL,
    status       VARCHAR(20)     NOT NULL DEFAULT 'PENDENTE',
    data         DATE            NOT NULL,
    categoria_id BIGINT          REFERENCES categorias(id) ON DELETE SET NULL,
    criado_em    TIMESTAMP       NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP      NOT NULL DEFAULT NOW()
);


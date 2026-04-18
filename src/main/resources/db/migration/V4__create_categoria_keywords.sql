CREATE TABLE categoria_keywords (
    id          BIGSERIAL PRIMARY KEY,
    keyword     VARCHAR(100) NOT NULL,
    categoria_id BIGINT NOT NULL REFERENCES categorias(id) ON DELETE CASCADE
);

CREATE INDEX idx_categoria_keywords_keyword ON categoria_keywords (LOWER(keyword));


CREATE TABLE setores (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(255),
    tipo VARCHAR(50),
    tamanho VARCHAR(50),
    id_estabelecimento BIGINT,
    CONSTRAINT fk_setores_estabelecimento FOREIGN KEY (id_estabelecimento) REFERENCES estabelecimento(id)
);

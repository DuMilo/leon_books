CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE livros (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE emprestimos (
    id BIGSERIAL PRIMARY KEY,
    livro_id BIGINT NOT NULL REFERENCES livros(id),
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE NOT NULL,
    devolvido BOOLEAN NOT NULL DEFAULT FALSE,
    renovado BOOLEAN NOT NULL DEFAULT FALSE
);
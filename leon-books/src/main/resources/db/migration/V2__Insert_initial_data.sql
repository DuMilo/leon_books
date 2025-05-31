INSERT INTO cliente (nome, email, telefone, endereco) VALUES
('João Silva', 'joao.silva@example.com', '11987654321', 'Rua das Palmeiras, 123'),
('Maria Oliveira', 'maria.oliveira@example.com', '21912345678', 'Avenida Central, 456');

INSERT INTO livro (titulo, autor, isbn, ano_publicacao, disponivel) VALUES
('O Senhor dos Anéis', 'J.R.R. Tolkien', '978-0618260274', 1954, TRUE),
('1984', 'George Orwell', '978-0451524935', 1949, TRUE),
('Dom Quixote', 'Miguel de Cervantes', '978-8535904908', 1605, FALSE);

INSERT INTO emprestimo (cliente_id, livro_id, data_emprestimo, data_devolucao_prevista, devolvido, renovado) VALUES
(1, 1, '2025-05-10', '2025-05-24', FALSE, FALSE);

INSERT INTO emprestimo (cliente_id, livro_id, data_emprestimo, data_devolucao_prevista, data_devolucao_real, devolvido, renovado) VALUES
(2, 2, '2025-05-01', '2025-05-15', '2025-05-14', TRUE, FALSE);
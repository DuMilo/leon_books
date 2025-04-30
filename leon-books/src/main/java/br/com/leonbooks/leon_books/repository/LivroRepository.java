package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Livro;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LivroRepository {
    private final List<Livro> livros = new ArrayList<>();

    public List<Livro> buscarTodos() {
        return new ArrayList<>(livros);
    }

    // Mudança: Integer -> int (e ajuste na comparação)
    public Optional<Livro> buscarPorId(int id) {
        return livros.stream().filter(l -> l.getId() == id).findFirst();
    }

    public Livro salvar(Livro livro) {
        if (livro.getId() == 0) {
            Livro novoLivro = new Livro(livro.getTitulo(), livro.getAutor());
            livros.add(novoLivro);
            return novoLivro;
        } else {
            livros.removeIf(l -> l.getId() == livro.getId());
            livros.add(livro);
            return livro;
        }
    }

    public List<Livro> buscarDisponiveis() {
        return livros.stream().filter(Livro::isDisponivel).toList();
    }
}
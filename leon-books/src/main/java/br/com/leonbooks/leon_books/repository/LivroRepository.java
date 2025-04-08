package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Livro;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LivroRepository {
    private final List<Livro> livros = new ArrayList<>();
    private int proximoId = 1;

    public List<Livro> buscarTodos(){
        return new ArrayList<>(livros);
    }

    public Optional<Livro> buscarPorId(Integer id) {
        return livros.stream().filter(l -> l.getId().equals(id)).findFirst();
    }

    public Livro salvar(Livro livro) {
        if (livro.getId() == null) {
            Livro novoLivro = new Livro(proximoId++, livro.getTitulo(), livro.getAutor(), livro.getIsbn());
            livros.add(novoLivro);
            return novoLivro;
        } else {
            livros.removeIf(l -> l.getId().equals(livro.getId()));
            livros.add(livro);
            return livro;
        }
    }

    public List<Livro> buscarDisponiveis(){
        return livros.stream().filter(Livro::isDisponivel).toList();
    }
}

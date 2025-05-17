package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    @Query("SELECT l FROM Livro l WHERE l.disponivel = true")
    List<Livro> findLivrosDisponiveis();
}
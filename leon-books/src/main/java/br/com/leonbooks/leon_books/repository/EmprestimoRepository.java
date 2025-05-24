package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByClienteId(Long clienteId);
    List<Emprestimo> findByLivroId(Long livroId);
    List<Emprestimo> findByDevolvidoFalseAndDataDevolucaoBefore(LocalDate now);
}
package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByClienteId(Long clienteId);
    List<Emprestimo> findByLivroId(Long livroId);
    List<Emprestimo> findByDataDevolucaoRealIsNullAndDataDevolucaoPrevistaBefore(LocalDate data);
    long countByLivroIdAndDevolvidoFalse(Long livroId);
}
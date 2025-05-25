package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultaRepository extends JpaRepository<Multa, Long> {
    List<Multa> findByEmprestimoId(Long emprestimoId);
    List<Multa> findByPagaFalse();
    List<Multa> findByEmprestimoClienteIdAndPagaFalse(Long clienteId);
}
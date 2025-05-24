package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    boolean existsByEmail(String email);

    Optional<Cliente> findByNomeIgnoreCase(String nome);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findByEmailContainingIgnoreCase(String email);
}
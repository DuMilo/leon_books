package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Emprestimo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmprestimoRepository {
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    public Emprestimo salvar(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
        return emprestimo;
    }

    public List<Emprestimo> buscarTodos() {
        return new ArrayList<>(emprestimos);
    }

    public List<Emprestimo> buscarPorCliente(int clienteId) {
        return emprestimos.stream().filter(e -> e.getCliente().getId() == clienteId).toList();
    }

    public List<Emprestimo> buscarPorLivro(int livroId) {
        return emprestimos.stream().filter(e -> e.getLivro().getId() == livroId).toList();
    }
}
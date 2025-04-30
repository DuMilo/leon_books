package br.com.leonbooks.leon_books.repository;

import br.com.leonbooks.leon_books.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {
    private final List<Cliente> clientes = new ArrayList<>();

    public Cliente salvar(Cliente cliente) {
        clientes.add(cliente);
        return cliente;
    }

    public Optional<Cliente> buscarPorId(int clienteId) {
        return clientes.stream().filter(c -> c.getId() == clienteId).findFirst();
    }

    public Optional<Cliente> buscarPorNome(String nome) {
        return clientes.stream().filter(c -> c.getNome().equalsIgnoreCase(nome)).findFirst();
    }

    public List<Cliente> buscarTodos() {
        return new ArrayList<>(clientes);
    }
}

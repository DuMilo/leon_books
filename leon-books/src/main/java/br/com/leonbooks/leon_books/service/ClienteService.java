package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrarCliente(Cliente cliente){
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("E-mail do cliente é obrigatório.");
        }
        return clienteRepository.salvar(cliente);
    }

    public Optional<Cliente> buscarPorId(int clienteId) {
        return clienteRepository.buscarPorId(clienteId);
    }

    public Optional<Cliente> buscarPorId(String clienteId) {
        try {
            return buscarPorId(Integer.parseInt(clienteId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<Cliente> buscarPorNome(String nome){
        return clienteRepository.buscarPorNome(nome);
    }

    public List<Cliente> buscarTodos(){
        return clienteRepository.buscarTodos();
    }
}

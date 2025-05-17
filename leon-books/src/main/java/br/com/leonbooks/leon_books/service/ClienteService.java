package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente cadastrarCliente(Cliente cliente){
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("E-mail do cliente é obrigatório.");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())){
            throw new IllegalStateException("Já existe um cliente com esse e-mail.");
        }
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public Optional<Cliente> buscarPorNome(String nome){
        return clienteRepository.findByNomeIgnoreCase(nome);
    }

    public List<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    @Transactional
    public void removerCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(clienteId);
    }
}

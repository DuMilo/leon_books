package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrarCliente(Cliente cliente) {
        if (!StringUtils.hasText(cliente.getNome())) {
            throw new IllegalArgumentException("O nome do cliente não pode ser vazio.");
        }
        if (!StringUtils.hasText(cliente.getEmail())) {
            throw new IllegalArgumentException("O email do cliente não pode ser vazio.");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalStateException("Já existe um cliente cadastrado com este email.");
        }
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmailContainingIgnoreCase(email);
    }

    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    public void removerCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado para remoção.");
        }
        clienteRepository.deleteById(id);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteDetails) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) {
            return null;
        }

        Cliente clienteExistente = clienteOptional.get();

        if (!StringUtils.hasText(clienteDetails.getNome())) {
            throw new IllegalArgumentException("O nome do cliente não pode ser vazio.");
        }
        if (!StringUtils.hasText(clienteDetails.getEmail())) {
            throw new IllegalArgumentException("O email do cliente não pode ser vazio.");
        }

        if (!clienteExistente.getEmail().equals(clienteDetails.getEmail()) &&
            clienteRepository.existsByEmail(clienteDetails.getEmail())) {
            throw new IllegalStateException("Já existe outro cliente cadastrado com este email.");
        }

        clienteExistente.setNome(clienteDetails.getNome());
        clienteExistente.setEmail(clienteDetails.getEmail());
        clienteExistente.setTelefone(clienteDetails.getTelefone());
        clienteExistente.setEndereco(clienteDetails.getEndereco());

        return clienteRepository.save(clienteExistente);
    }
}

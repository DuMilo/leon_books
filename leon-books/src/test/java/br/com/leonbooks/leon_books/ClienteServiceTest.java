package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.repository.ClienteRepository;
import br.com.leonbooks.leon_books.service.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteServiceTest {
    private ClienteService clienteService;
    private ClienteRepository clienteRepository;

    @BeforeEach
    void init(){
        clienteRepository = new ClienteRepository();
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void testCadastrarCliente() {
        Cliente cliente = new Cliente("João Silva", "joao@gmail.com");
        Cliente resultado = clienteService.cadastrarCliente(cliente);
        
        assertNotNull(resultado.getId());
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    void testLancarExcecaoNomeVazio() {
        Cliente cliente = new Cliente("", "gmail@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> clienteService.cadastrarCliente(cliente));
    }

    @Test
    void testLancarExcecaoEmailVazio() {
        Cliente cliente = new Cliente("nome", "");
        assertThrows(IllegalArgumentException.class, () -> clienteService.cadastrarCliente(cliente));
    }
}

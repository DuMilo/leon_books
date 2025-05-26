package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.repository.ClienteRepository;
import br.com.leonbooks.leon_books.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("Sofia Travassos", "sofia@email.com");
        cliente.setId(1L);
    }

    @Test
    void deveCadastrarClienteComSucesso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);

        Cliente resultado = clienteService.cadastrarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Sofia Travassos", resultado.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoNomeVazio() {
        Cliente clienteInvalido = new Cliente("", "email@teste.com");
        
        assertThrows(IllegalArgumentException.class, 
            () -> clienteService.cadastrarCliente(clienteInvalido));
    }

    @Test
    void deveLancarExcecaoQuandoEmailVazio() {
        Cliente clienteInvalido = new Cliente("Nome", "");
        
        assertThrows(IllegalArgumentException.class, 
            () -> clienteService.cadastrarCliente(clienteInvalido));
    }

    @Test
    void deveLancarExcecaoQuandoEmailExistente() {
        when(clienteRepository.existsByEmail("sofia@email.com")).thenReturn(true);

        assertThrows(IllegalStateException.class, 
            () -> clienteService.cadastrarCliente(cliente));
    }

    @Test
    void deveBuscarClientePorIdExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Sofia Travassos", resultado.get().getNome());
    }

    @Test
    void deveRetornarVazioParaClienteInexistente() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarClientesPorNomeParcial() {
        when(clienteRepository.findByNomeContainingIgnoreCase("sofia"))
            .thenReturn(List.of(cliente));
        
        List<Cliente> resultado = clienteService.buscarPorNome("sofia");
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNome().contains("Sofia"));
    }

    @Test
    void deveRetornarListaVaziaParaNomeNaoEncontrado() {
        when(clienteRepository.findByNomeContainingIgnoreCase(anyString()))
            .thenReturn(Collections.emptyList());
        
        List<Cliente> resultado = clienteService.buscarPorNome("inexistente");
        
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarClientesPorEmailParcial() {
        when(clienteRepository.findByEmailContainingIgnoreCase("email"))
            .thenReturn(List.of(cliente));
        
        List<Cliente> resultado = clienteService.buscarPorEmail("email");
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getEmail().contains("email"));
    }

    @Test
    void deveRetornarTodosClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        
        List<Cliente> resultado = clienteService.buscarTodos();
        
        assertEquals(1, resultado.size());
    }

    @Test
    void deveRemoverClienteExistente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        
        clienteService.removerCliente(1L);
        
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoRemoverClienteInexistente() {
        when(clienteRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, 
            () -> clienteService.removerCliente(999L));
    }
}
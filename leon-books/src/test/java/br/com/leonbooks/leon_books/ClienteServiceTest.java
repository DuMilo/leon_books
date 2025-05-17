package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.repository.ClienteRepository;
import br.com.leonbooks.leon_books.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarClienteComSucesso() {
        Cliente cliente = new Cliente("Sofia Travassos", "sofia@email.com");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);

        Cliente resultado = clienteService.cadastrarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Sofia Travassos", resultado.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoEmailExistente() {
        Cliente cliente = new Cliente("Sofia Travassos", "sofia@email.com");
        when(clienteRepository.existsByEmail("sofia@email.com")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> clienteService.cadastrarCliente(cliente));
    }

    @Test
    void deveBuscarClientePorId() {
        Long clienteId = 1L;
        Cliente cliente = new Cliente("Sofia Travassos", "sofia@email.com");
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(clienteId);

        assertTrue(resultado.isPresent());
        assertEquals("Sofia Travassos", resultado.get().getNome());
    }

    @Test
    void deveRetornarVazioQuandoClienteNaoExiste() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isEmpty());
    }
}
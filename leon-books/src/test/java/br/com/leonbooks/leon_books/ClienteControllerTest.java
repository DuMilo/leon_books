package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.controller.ClienteController;
import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("Sofia Travassos", "sofia@email.com");
        cliente.setId(1L);
    }

    @Test
    void deveCadastrarClienteComSucesso() {
        when(clienteService.cadastrarCliente(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.cadastrar(cliente);
        Cliente responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Sofia Travassos", responseBody.getNome());
    }

    @Test
    void deveBuscarClientePorIdExistente() {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        ResponseEntity<Cliente> response = clienteController.buscarPorId(1L);
        Cliente responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, responseBody.getId());
    }

    @Test
    void deveRetornarNotFoundParaClienteInexistente() {
        when(clienteService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Cliente> response = clienteController.buscarPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveBuscarClientesPorNome() {
        when(clienteService.buscarPorNome(anyString())).thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = clienteController.buscarPorNome("sofia");
        List<Cliente> responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(responseBody.isEmpty());
        assertEquals(1, responseBody.size());
    }

    @Test
    void deveRetornarListaVaziaParaNomeNaoEncontrado() {
        when(clienteService.buscarPorNome(anyString())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Cliente>> response = clienteController.buscarPorNome("inexistente");
        List<Cliente> responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.isEmpty());
    }

    @Test
    void deveBuscarClientesPorEmail() {
        when(clienteService.buscarPorEmail(anyString())).thenReturn(List.of(cliente));

        ResponseEntity<List<Cliente>> response = clienteController.buscarPorEmail("email");
        List<Cliente> responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(responseBody.isEmpty());
    }

    @Test
    void deveRetornarTodosClientes() {
        when(clienteService.buscarTodos()).thenReturn(List.of(cliente));

        List<Cliente> response = clienteController.buscarTodos();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void deveRemoverClienteComSucesso() {
        doNothing().when(clienteService).removerCliente(1L);

        ResponseEntity<Void> response = clienteController.remover(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).removerCliente(1L);
    }
}
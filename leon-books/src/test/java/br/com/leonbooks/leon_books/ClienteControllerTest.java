package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.controller.ClienteController;
import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        cliente = new Cliente("Sofia Travassos", "sofia@email.com", "123456789", "Rua Teste");
        cliente.setId(1L);
    }

    @Test
    void deveCadastrarClienteComSucesso() throws Exception {
        when(clienteService.cadastrarCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Sofia Travassos")));
    }

    @Test
    void deveBuscarClientePorIdExistente() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deveRetornarNotFoundParaClienteInexistente() throws Exception {
        when(clienteService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBuscarClientesPorNome() throws Exception {
        when(clienteService.buscarPorNome(anyString())).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes/buscar-por-nome").param("nome", "sofia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Sofia Travassos")));
    }
    
    @Test
    void deveRetornarListaVaziaQuandoBuscarPorNomeNaoEncontrado() throws Exception {
        when(clienteService.buscarPorNome(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clientes/buscar-por-nome").param("nome", "inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void deveBuscarClientesPorEmail() throws Exception {
        when(clienteService.buscarPorEmail(anyString())).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes/buscar-por-email").param("email", "sofia@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("sofia@email.com")));
    }
    
    @Test
    void deveRetornarListaVaziaQuandoBuscarPorEmailNaoEncontrado() throws Exception {
        when(clienteService.buscarPorEmail(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clientes/buscar-por-email").param("email", "inexistente@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void deveRetornarTodosClientes() throws Exception {
        when(clienteService.buscarTodos()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deveRemoverClienteComSucesso() throws Exception {
        doNothing().when(clienteService).removerCliente(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).removerCliente(1L);
    }
    
    @Test
    void deveRetornarBadRequestAoCadastrarClienteComDadosInvalidos() throws Exception {
        Cliente clienteInvalido = new Cliente("", "", "", ""); 
        when(clienteService.cadastrarCliente(any(Cliente.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveRetornarConflictAoCadastrarClienteComEmailExistente() throws Exception {
        when(clienteService.cadastrarCliente(any(Cliente.class))).thenThrow(new IllegalStateException("Email já existe"));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isConflict());
    }

}
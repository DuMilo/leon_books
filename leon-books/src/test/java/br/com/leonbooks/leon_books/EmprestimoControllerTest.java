package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.controller.EmprestimoController;
import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.EmprestimoService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmprestimoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmprestimoService emprestimoService;

    @InjectMocks
    private EmprestimoController emprestimoController;

    private Emprestimo emprestimo;
    private Livro livro;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emprestimoController).build();

        livro = new Livro("Livro Teste", "Autor Teste", "12345", 2020);
        livro.setId(1L);

        cliente = new Cliente("Cliente Teste", "cliente@teste.com", "11111", "Rua Teste");
        cliente.setId(1L);

        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setLivro(livro);
        emprestimo.setCliente(cliente);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
    }

    @Test
    void deveCriarEmprestimoComSucesso() throws Exception {
        when(emprestimoService.realizarEmprestimo(anyLong(), anyLong())).thenReturn(emprestimo);

        mockMvc.perform(post("/api/emprestimos")
                .param("livroId", "1")
                .param("clienteId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deveRetornarBadRequestAoCriarEmprestimoComLivroInexistente() throws Exception {
        when(emprestimoService.realizarEmprestimo(anyLong(), anyLong())).thenThrow(new IllegalArgumentException("Livro não encontrado"));

        mockMvc.perform(post("/api/emprestimos")
                .param("livroId", "999")
                .param("clienteId", "1"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveRetornarBadRequestAoCriarEmprestimoComLivroIndisponivel() throws Exception {
        when(emprestimoService.realizarEmprestimo(anyLong(), anyLong())).thenThrow(new IllegalStateException("Livro indisponível"));

        mockMvc.perform(post("/api/emprestimos")
                .param("livroId", "1")
                .param("clienteId", "1"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveDevolverLivroComSucesso() throws Exception {
        emprestimo.setDevolvido(true);
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        when(emprestimoService.devolverLivro(anyLong())).thenReturn(emprestimo);

        mockMvc.perform(put("/api/emprestimos/1/devolucao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.devolvido", is(true)));
    }
    
    @Test
    void deveRetornarNotFoundAoTentarDevolverEmprestimoInexistente() throws Exception {
        when(emprestimoService.devolverLivro(anyLong())).thenThrow(new IllegalArgumentException("Empréstimo não encontrado"));

        mockMvc.perform(put("/api/emprestimos/999/devolucao"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveRetornarBadRequestAoTentarDevolverLivroJaDevolvido() throws Exception {
        when(emprestimoService.devolverLivro(anyLong())).thenThrow(new IllegalStateException("Livro já devolvido"));

        mockMvc.perform(put("/api/emprestimos/1/devolucao"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveRenovarEmprestimoComSucesso() throws Exception {
        emprestimo.setRenovado(true);
        emprestimo.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista().plusDays(7));
        when(emprestimoService.renovarEmprestimoEretornar(anyLong())).thenReturn(emprestimo);

        mockMvc.perform(put("/api/emprestimos/1/renovar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renovado", is(true)));
    }
    
    @Test
    void deveRetornarBadRequestAoTentarRenovarEmprestimoInexistente() throws Exception {
        when(emprestimoService.renovarEmprestimoEretornar(anyLong())).thenThrow(new IllegalArgumentException("Empréstimo não encontrado"));

        mockMvc.perform(put("/api/emprestimos/999/renovar"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void deveRetornarBadRequestAoTentarRenovarEmprestimoComCondicaoInvalida() throws Exception {
        when(emprestimoService.renovarEmprestimoEretornar(anyLong())).thenThrow(new IllegalStateException("Condição inválida para renovação"));

        mockMvc.perform(put("/api/emprestimos/1/renovar"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveListarTodosEmprestimos() throws Exception {
        when(emprestimoService.listarTodos()).thenReturn(List.of(emprestimo));

        mockMvc.perform(get("/api/emprestimos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deveListarEmprestimosAtrasados() throws Exception {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));
        emprestimo.setDevolvido(false);
        when(emprestimoService.listarEmprestimosAtrasados()).thenReturn(List.of(emprestimo));

        mockMvc.perform(get("/api/emprestimos/atrasados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
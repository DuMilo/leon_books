package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.controller.LivroController;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LivroControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LivroService livroService;

    @InjectMocks
    private LivroController livroController;

    private Livro livro;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(livroController).build();
        livro = new Livro("Dom Casmurro", "Machado de Assis", "978-8535902775", 1899);
        livro.setId(1L);
        livro.setDisponivel(true);
    }

    @Test
    void deveListarTodosLivros() throws Exception {
        when(livroService.buscarTodosLivros()).thenReturn(List.of(livro));

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is("Dom Casmurro")));
    }

    @Test
    void deveBuscarLivroPorIdExistente() throws Exception {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));

        mockMvc.perform(get("/api/livros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void deveRetornarNotFoundAoBuscarLivroPorIdInexistente() throws Exception {
        when(livroService.buscarLivroPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/livros/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCadastrarLivroComSucesso() throws Exception {
        when(livroService.cadastraLivro(any(Livro.class))).thenReturn(livro);

        mockMvc.perform(post("/api/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is("Dom Casmurro")));
    }
    
    @Test
    void deveRetornarBadRequestAoCadastrarLivroComErro() throws Exception {
        when(livroService.cadastraLivro(any(Livro.class))).thenThrow(new RuntimeException("Erro ao cadastrar"));

        mockMvc.perform(post("/api/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveAtualizarLivroExistente() throws Exception {
        Livro livroAtualizado = new Livro("Dom Casmurro Atualizado", "Machado de Assis", "123", 2000);
        livroAtualizado.setId(1L);
        when(livroService.atualizarLivro(eq(1L), any(Livro.class))).thenReturn(livroAtualizado);

        mockMvc.perform(put("/api/livros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livroAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Dom Casmurro Atualizado")));
    }
    
    @Test
    void deveRetornarNotFoundAoAtualizarLivroInexistente() throws Exception {
        Livro livroAtualizado = new Livro("Dom Casmurro Atualizado", "Machado de Assis", "123", 2000);
        when(livroService.atualizarLivro(eq(999L), any(Livro.class))).thenReturn(null);

        mockMvc.perform(put("/api/livros/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livroAtualizado)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deveRetornarBadRequestAoAtualizarLivroComErro() throws Exception {
        Livro livroAtualizado = new Livro("Dom Casmurro Atualizado", "Machado de Assis", "123", 2000);
        when(livroService.atualizarLivro(eq(1L), any(Livro.class))).thenThrow(new RuntimeException("Erro ao atualizar"));

        mockMvc.perform(put("/api/livros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(livroAtualizado)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deveDeletarLivroComSucesso() throws Exception {
        when(livroService.deletarLivro(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isNoContent());
        verify(livroService, times(1)).deletarLivro(1L);
    }

    @Test
    void deveRetornarNotFoundAoTentarDeletarLivroInexistente() throws Exception {
        when(livroService.deletarLivro(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/livros/999"))
                .andExpect(status().isNotFound());
        verify(livroService, times(1)).deletarLivro(999L);
    }
    
    @Test
    void deveRetornarConflictAoTentarDeletarLivroComEmprestimoAtivo() throws Exception {
        when(livroService.deletarLivro(1L)).thenThrow(new IllegalStateException("Livro com empréstimo ativo"));

        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornarInternalServerErrorAoTentarDeletarLivroComErroInesperado() throws Exception {
        when(livroService.deletarLivro(1L)).thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(delete("/api/livros/1"))
                .andExpect(status().isInternalServerError());
    }

}
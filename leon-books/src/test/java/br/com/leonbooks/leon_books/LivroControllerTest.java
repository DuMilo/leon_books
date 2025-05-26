package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.controller.LivroController;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.LivroService;
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

class LivroControllerTest {

    @Mock
    private LivroService livroService;

    @InjectMocks
    private LivroController livroController;

    private Livro livro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        livro = new Livro("Dom Casmurro", "Machado de Assis");
        livro.setId(1L);
        livro.setDisponivel(true);
    }

    @Test
    void deveCadastrarLivroComSucesso() {
        when(livroService.cadastraLivro(any(Livro.class))).thenReturn(livro);

        ResponseEntity<Livro> response = livroController.cadastrar(livro);
        Livro responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Dom Casmurro", responseBody.getTitulo());
    }

    @Test
    void deveBuscarLivroPorIdExistente() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));

        ResponseEntity<Livro> response = livroController.buscarPorId(1L);
        Livro responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, responseBody.getId());
    }

    @Test
    void deveRetornarNotFoundParaLivroInexistente() {
        when(livroService.buscarLivroPorId(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Livro> response = livroController.buscarPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveBuscarLivrosPorTitulo() {
        when(livroService.buscarPorTitulo(anyString())).thenReturn(List.of(livro));

        ResponseEntity<List<Livro>> response = livroController.buscarPorTitulo("casmurro");
        List<Livro> responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(responseBody.isEmpty());
        assertEquals("Dom Casmurro", responseBody.get(0).getTitulo());
    }

    @Test
    void deveRetornarListaVaziaParaTituloNaoEncontrado() {
        when(livroService.buscarPorTitulo(anyString())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Livro>> response = livroController.buscarPorTitulo("inexistente");
        List<Livro> responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.isEmpty());
    }

    @Test
    void deveBuscarLivrosPorAutor() {
        when(livroService.buscarPorAutor(anyString())).thenReturn(List.of(livro));

        ResponseEntity<List<Livro>> response = livroController.buscarPorAutor("Machado");
        List<Livro> responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(responseBody.isEmpty());
        assertEquals("Machado de Assis", responseBody.get(0).getAutor());
    }

    @Test
    void deveListarTodosLivros() {
        when(livroService.buscarTodosLivros()).thenReturn(List.of(livro));

        List<Livro> response = livroController.listarTodos();

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void deveListarLivrosDisponiveis() {
        when(livroService.buscarLivrosDisponiveis()).thenReturn(List.of(livro));

        List<Livro> response = livroController.listarDisponiveis();

        assertFalse(response.isEmpty());
        assertTrue(response.get(0).isDisponivel());
    }

    @Test
    void deveAtualizarLivroExistente() {
        when(livroService.cadastraLivro(any(Livro.class))).thenReturn(livro);

        ResponseEntity<Livro> response = livroController.atualizar(1L, livro);
        Livro responseBody = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, responseBody.getId());
    }

    @Test
    void deveRemoverLivroComSucesso() {
        doNothing().when(livroService).removerLivro(1L);

        ResponseEntity<Void> response = livroController.remover(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(livroService, times(1)).removerLivro(1L);
    }
}
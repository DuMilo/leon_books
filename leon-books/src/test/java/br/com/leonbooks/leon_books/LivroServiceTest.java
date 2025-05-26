package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import br.com.leonbooks.leon_books.service.LivroService;
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

class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

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
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        Livro resultado = livroService.cadastraLivro(livro);

        assertNotNull(resultado);
        assertEquals("Dom Casmurro", resultado.getTitulo());
        verify(livroRepository).save(livro);
    }

    @Test
    void deveLancarExcecaoQuandoTituloVazio() {
        livro.setTitulo("");

        assertThrows(IllegalArgumentException.class, 
            () -> livroService.cadastraLivro(livro));
    }

    @Test
    void deveLancarExcecaoQuandoAutorVazio() {
        livro.setAutor("");

        assertThrows(IllegalArgumentException.class, 
            () -> livroService.cadastraLivro(livro));
    }

    @Test
    void deveBuscarLivroPorIdExistente() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        Optional<Livro> resultado = livroService.buscarLivroPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Machado de Assis", resultado.get().getAutor());
    }

    @Test
    void deveRetornarVazioParaLivroInexistente() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Livro> resultado = livroService.buscarLivroPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarTodosLivros() {
        when(livroRepository.findAll()).thenReturn(List.of(livro));

        List<Livro> resultado = livroService.buscarTodosLivros();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveListarLivrosDisponiveis() {
        when(livroRepository.findLivrosDisponiveis()).thenReturn(List.of(livro));

        List<Livro> resultado = livroService.buscarLivrosDisponiveis();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).isDisponivel());
    }

    @Test
    void deveRetornarListaVaziaParaLivrosDisponiveis() {
        when(livroRepository.findLivrosDisponiveis()).thenReturn(Collections.emptyList());

        List<Livro> resultado = livroService.buscarLivrosDisponiveis();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarLivrosPorTitulo() {
        when(livroRepository.findByTituloContainingIgnoreCase("dom")).thenReturn(List.of(livro));

        List<Livro> resultado = livroService.buscarPorTitulo("dom");

        assertFalse(resultado.isEmpty());
        assertEquals("Dom Casmurro", resultado.get(0).getTitulo());
    }

    @Test
    void deveBuscarLivrosPorAutor() {
        when(livroRepository.findByAutorContainingIgnoreCase("machado")).thenReturn(List.of(livro));

        List<Livro> resultado = livroService.buscarPorAutor("machado");

        assertFalse(resultado.isEmpty());
        assertEquals("Machado de Assis", resultado.get(0).getAutor());
    }

    @Test
    void deveAtualizarLivroExistente() {
        when(livroRepository.existsById(1L)).thenReturn(true);
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        livroService.atualizarLivro(livro);

        verify(livroRepository).save(livro);
    }

    @Test
    void deveLancarExcecaoAoAtualizarLivroInexistente() {
        livro.setId(999L);
        when(livroRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, 
            () -> livroService.atualizarLivro(livro));
    }

    @Test
    void deveRemoverLivroExistente() {
        when(livroRepository.existsById(1L)).thenReturn(true);

        livroService.removerLivro(1L);

        verify(livroRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoRemoverLivroInexistente() {
        when(livroRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, 
            () -> livroService.removerLivro(999L));
    }
}
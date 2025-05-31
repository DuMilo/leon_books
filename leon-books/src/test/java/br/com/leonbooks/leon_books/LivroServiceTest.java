package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
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

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        livro = new Livro("Dom Casmurro", "Machado de Assis", "978-8535902775", 1899);
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
        when(livroRepository.findByDisponivelTrue()).thenReturn(List.of(livro));

        List<Livro> resultado = livroService.buscarLivrosDisponiveis();

        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).isDisponivel());
    }

    @Test
    void deveRetornarListaVaziaParaLivrosDisponiveisQuandoNaoHaLivrosDisponiveis() {
        when(livroRepository.findByDisponivelTrue()).thenReturn(Collections.emptyList());

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
        Livro livroAtualizado = new Livro("Dom Casmurro 2", "Machado de Assis Jr.", "123", 2000);
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroRepository.save(any(Livro.class))).thenReturn(livroAtualizado);

        Livro resultado = livroService.atualizarLivro(1L, livroAtualizado);

        assertNotNull(resultado);
        assertEquals("Dom Casmurro 2", resultado.getTitulo());
        verify(livroRepository).save(livro);
    }

    @Test
    void deveRetornarNullAoTentarAtualizarLivroInexistente() {
         Livro livroAtualizado = new Livro("Dom Casmurro 2", "Machado de Assis Jr.", "123", 2000);
        when(livroRepository.findById(999L)).thenReturn(Optional.empty());

        Livro resultado = livroService.atualizarLivro(999L, livroAtualizado);
        
        assertNull(resultado);
        verify(livroRepository, never()).save(any(Livro.class));
    }


    @Test
    void deveDeletarLivroExistenteComSucesso() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.countByLivroIdAndDevolvidoFalse(1L)).thenReturn(0L);
        doNothing().when(livroRepository).deleteById(1L);

        boolean deletado = livroService.deletarLivro(1L);

        assertTrue(deletado);
        verify(livroRepository).deleteById(1L);
    }
    
    @Test
    void deveLancarExcecaoAoTentarDeletarLivroComEmprestimoAtivo() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.countByLivroIdAndDevolvidoFalse(1L)).thenReturn(1L);

        assertThrows(IllegalStateException.class, () -> livroService.deletarLivro(1L));
        verify(livroRepository, never()).deleteById(1L);
    }


    @Test
    void deveRetornarFalseAoTentarDeletarLivroInexistente() {
        when(livroRepository.findById(999L)).thenReturn(Optional.empty());

        boolean deletado = livroService.deletarLivro(999L);

        assertFalse(deletado);
        verify(livroRepository, never()).deleteById(anyLong());
    }
}
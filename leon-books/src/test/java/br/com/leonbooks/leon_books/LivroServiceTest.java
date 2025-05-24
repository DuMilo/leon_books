package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import br.com.leonbooks.leon_books.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarLivroComSucesso() {
        Livro livro = new Livro("Dom Casmurro", "Machado de Assis");
        livro.setDisponivel(true); // Garante estado inicial
        
        when(livroRepository.save(any(Livro.class))).thenAnswer(invocation -> {
            Livro l = invocation.getArgument(0);
            l.setId(1L); // Simula a geração do ID
            return l;
        });

        Livro resultado = livroService.cadastraLivro(livro);

        assertNotNull(resultado);
        assertEquals("Dom Casmurro", resultado.getTitulo());
        assertTrue(resultado.isDisponivel());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    void deveBuscarLivroPorId() {
        Long livroId = 1L;
        Livro livro = new Livro("A Hora da Estrela", "Clarice Lispector");
        when(livroRepository.findById(livroId)).thenReturn(Optional.of(livro));

        Optional<Livro> resultado = livroService.buscarLivroPorId(livroId);

        assertTrue(resultado.isPresent());
        assertEquals("Clarice Lispector", resultado.get().getAutor());
    }

    @Test
    void deveListarLivrosDisponiveis() {
        Livro livro1 = new Livro("Livro 1", "Autor 1");
        Livro livro2 = new Livro("Livro 2", "Autor 2");
        livro2.setDisponivel(false);

        when(livroRepository.findLivrosDisponiveis()).thenReturn(List.of(livro1));

        List<Livro> disponiveis = livroService.buscarLivrosDisponiveis();

        assertEquals(1, disponiveis.size());
        assertEquals("Livro 1", disponiveis.get(0).getTitulo());
    }

    @Test
    void deveRemoverLivroExistente() {
        Long livroId = 1L;
        when(livroRepository.existsById(livroId)).thenReturn(true);

        livroService.removerLivro(livroId);

        verify(livroRepository, times(1)).deleteById(livroId);
    }

    @Test
    void deveBuscarLivrosPorTitulo() {
        Livro livro = new Livro("Dom Casmurro", "Machado de Assis");
        when(livroRepository.findByTituloContainingIgnoreCase("dom")).thenReturn(List.of(livro));
        
        List<Livro> resultado = livroService.buscarPorTitulo("dom");
        
        assertEquals(1, resultado.size());
        assertEquals("Dom Casmurro", resultado.get(0).getTitulo());
    }

    @Test
    void deveBuscarLivrosPorAutor() {
        Livro livro1 = new Livro("Livro 1", "Machado de Assis");
        Livro livro2 = new Livro("Livro 2", "Machado de Assis");
        when(livroRepository.findByAutorContainingIgnoreCase("machado")).thenReturn(List.of(livro1, livro2));
        
        List<Livro> resultado = livroService.buscarPorAutor("machado");
        
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(l -> l.getAutor().contains("Machado")));
    }
}
package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import br.com.leonbooks.leon_books.service.LivroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class LivroServiceTest {
    private LivroService livroService;
    private LivroRepository livroRepository;
    
    @BeforeEach
    void init() {
        livroRepository = new LivroRepository();
        livroService = new LivroService(livroRepository);
    }

    @Test
    void testCadastrarLivro(){
        Livro livro = new Livro("A Metamorfose", "Franz Kafka");
        Livro livroSalvo = livroService.cadastraLivro(livro);

        assertNotNull(livroSalvo.getId());
        assertEquals("A Metamorfose", livroSalvo.getTitulo());
    }

    @Test
    void testBuscarLivroPorId() {
        Livro livro = new Livro("O Processo", "Franz Kafka");
        livroService.cadastraLivro(livro);
        
        Livro encontrado = livroService.buscarLivroPorId(livro.getId()).orElse(null);
        
        assertNotNull(encontrado);
        assertEquals(livro.getId(), encontrado.getId());
    }

    @Test
    void testRetornarListaLivrosDisponiveis() {
        Livro livro1 = new Livro("Livro 1", "Autor 1");
        Livro livro2 = new Livro("Livro 2", "Autor 2");
        livro1.setDisponivel(true);
        livro2.setDisponivel(false);

        livroService.cadastraLivro(livro1);
        livroService.cadastraLivro(livro2);

        assertTrue(livro1.isDisponivel());
        assertFalse(livro2.isDisponivel());
        
        List<Livro> disponiveis = livroService.buscarLivrosDisponiveis();
        
        assertEquals(1, disponiveis.size());
        assertEquals(livro1.getId(), disponiveis.get(0).getId());
    }
}

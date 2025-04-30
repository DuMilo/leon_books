package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.*;
import br.com.leonbooks.leon_books.service.*;
import br.com.leonbooks.leon_books.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoServiceTest {
    private EmprestimoService emprestimoService;
    private LivroService livroService;
    private ClienteService clienteService;
    
    @BeforeEach
    void init() {
        livroService = new LivroService(new LivroRepository());
        clienteService = new ClienteService(new ClienteRepository());
        emprestimoService = new EmprestimoService(new EmprestimoRepository(), livroService, clienteService);
    }

    @Test
    void deveRealizarEmprestimoComSucesso() {
        Livro livro = new Livro("Assassinato no Expresso Oriente", "Agatha Christie");
        livroService.cadastraLivro(livro);
        
        Cliente cliente = new Cliente("cliente", "cliente@gmail.com");
        clienteService.cadastrarCliente(cliente);
        
        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(livro.getId(), cliente.getId());
        
        assertNotNull(emprestimo);
        assertFalse(livro.isDisponivel());
        assertEquals(cliente.getId(), emprestimo.getCliente().getId());
    }

    @Test
    void deveLancarExcecaoSeLivroNaoDisponivel() {
        Livro livro = new Livro("Drácula", "Bram Stoker");
        livro.setDisponivel(false);
        livroService.cadastraLivro(livro);
        
        Cliente cliente = new Cliente("Jonathan Harker", "jonathan@gmail.com");
        clienteService.cadastrarCliente(cliente);
        
        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.realizarEmprestimo(livro.getId(), cliente.getId()));
    }

}

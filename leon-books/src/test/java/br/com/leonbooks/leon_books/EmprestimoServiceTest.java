package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.*;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
import br.com.leonbooks.leon_books.service.ClienteService;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import br.com.leonbooks.leon_books.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroService livroService;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private EmprestimoService emprestimoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void deveRealizarEmprestimoComSucesso() {
    Long livroId = 1L;
    Long clienteId = 1L;
    
    Livro livro = new Livro("Livro Teste", "Autor Teste");
    livro.setDisponivel(true);
    
    Cliente cliente = new Cliente("Cliente Teste", "cliente@teste.com");
    
    when(livroService.buscarLivroPorId(livroId))
        .thenReturn(Optional.of(livro));
    
    when(clienteService.buscarPorId(clienteId))
        .thenReturn(Optional.of(cliente));
    
    when(emprestimoRepository.save(any(Emprestimo.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Emprestimo emprestimo = emprestimoService.realizarEmprestimo(livroId, clienteId);

    assertNotNull(emprestimo);
    assertEquals(cliente, emprestimo.getCliente());
    assertEquals(LocalDate.now().plusDays(14), emprestimo.getDataDevolucao());
    
    verify(livroService).cadastraLivro(argThat(l -> !l.isDisponivel()));
}

    @Test
    void deveLancarExcecaoQuandoLivroNaoDisponivel() {
        Long livroId = 1L;
        Long clienteId = 1L;
        
        Livro livro = new Livro("Livro Teste", "Autor Teste");
        livro.setDisponivel(false);
        
        when(livroService.buscarLivroPorId(livroId)).thenReturn(Optional.of(livro));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.realizarEmprestimo(livroId, clienteId));
    }

    @Test
    void deveRegistrarDevolucaoCorretamente() {
        Long emprestimoId = 1L;
        Livro livro = new Livro("Livro Teste", "Autor Teste");
        Emprestimo emprestimo = new Emprestimo(livro, new Cliente("Cliente", "cliente@teste.com"));
        
        when(emprestimoRepository.findById(emprestimoId)).thenReturn(Optional.of(emprestimo));

        emprestimoService.devolverLivro(emprestimoId);

        assertTrue(emprestimo.isDevolvido());
        assertTrue(livro.isDisponivel());
        verify(emprestimoRepository, times(1)).save(emprestimo);
    }

    @Test
    void deveRenovarEmprestimoCorretamente() {
        Long emprestimoId = 1L;
        Emprestimo emprestimo = new Emprestimo(
            new Livro("Livro", "Autor"), 
            new Cliente("Cliente", "email")
        );
        LocalDate dataOriginal = emprestimo.getDataDevolucao();
        
        when(emprestimoRepository.findById(emprestimoId)).thenReturn(Optional.of(emprestimo));

        emprestimoService.renovarEmprestimo(emprestimoId);

        assertEquals(dataOriginal.plusDays(14), emprestimo.getDataDevolucao());
        assertTrue(emprestimo.isRenovado());
    }
}
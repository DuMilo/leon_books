package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.model.*;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
import br.com.leonbooks.leon_books.repository.MultaRepository;
import br.com.leonbooks.leon_books.service.ClienteService;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import br.com.leonbooks.leon_books.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private LivroService livroService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private Livro livro;
    private Cliente cliente;
    private Emprestimo emprestimo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        livro = new Livro("Dom Casmurro", "Machado de Assis");
        livro.setId(1L);
        livro.setDisponivel(true);
        
        cliente = new Cliente("João Silva", "joao@email.com");
        cliente.setId(1L);
        
        emprestimo = new Emprestimo(livro, cliente);
        emprestimo.setId(1L);
    }

    @Test
    void deveRealizarEmprestimoComSucesso() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(Collections.emptyList());

        Emprestimo resultado = emprestimoService.realizarEmprestimo(1L, 1L);

        assertNotNull(resultado);
        assertFalse(livro.isDisponivel());
        assertEquals(LocalDate.now().plusDays(14), resultado.getDataDevolucao());
        verify(emprestimoRepository).save(any(Emprestimo.class));
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoEncontrado() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoDisponivel() {
        livro.setDisponivel(false);
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoClienteTemMultasNaoPagas() {
        Multa multa = new Multa();
        multa.setPaga(false);
        
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(List.of(multa));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveRegistrarDevolucaoComSucesso() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        emprestimoService.devolverLivro(1L);

        assertTrue(emprestimo.isDevolvido());
        assertTrue(livro.isDisponivel());
        verify(emprestimoRepository).save(emprestimo);
    }

    @Test
    void deveAplicarMultaQuandoDevolucaoAtrasada() {
        emprestimo.setDataDevolucao(LocalDate.now().minusDays(5));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        
        Multa multaSimulada = new Multa(emprestimo, BigDecimal.valueOf(10.00));
        when(multaRepository.save(any(Multa.class))).thenReturn(multaSimulada);
        when(multaRepository.findByEmprestimoId(1L)).thenReturn(List.of(multaSimulada));

        emprestimoService.devolverLivro(1L);

        verify(multaRepository).save(any(Multa.class));
        
        BigDecimal multaCalculada = emprestimoService.calcularMultaEmprestimo(1L);
        assertEquals(BigDecimal.valueOf(10.00), multaCalculada);
    }

    @Test
    void deveLancarExcecaoQuandoDevolverLivroJaDevolvido() {
        emprestimo.setDevolvido(true);
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.devolverLivro(1L));
    }

    @Test
    void deveRenovarEmprestimoComSucesso() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        emprestimoService.renovarEmprestimo(1L);

        assertTrue(emprestimo.isRenovado());
        assertEquals(LocalDate.now().plusDays(28), emprestimo.getDataDevolucao());
        verify(emprestimoRepository).save(emprestimo);
    }

    @Test
    void deveLancarExcecaoQuandoRenovarEmprestimoJaDevolvido() {
        emprestimo.setDevolvido(true);
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.renovarEmprestimo(1L));
    }

    @Test
    void deveLancarExcecaoQuandoRenovarEmprestimoJaRenovado() {
        emprestimo.setRenovado(true);
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.renovarEmprestimo(1L));
    }

    @Test
    void deveLancarExcecaoQuandoRenovarEmprestimoAtrasado() {
        emprestimo.setDataDevolucao(LocalDate.now().minusDays(1));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.renovarEmprestimo(1L));
    }

    @Test
    void deveBuscarEmprestimosPorCliente() {
        when(emprestimoRepository.findByClienteId(1L)).thenReturn(List.of(emprestimo));

        List<Emprestimo> resultado = emprestimoService.buscarPorCliente(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveBuscarEmprestimosPorLivro() {
        when(emprestimoRepository.findByLivroId(1L)).thenReturn(List.of(emprestimo));

        List<Emprestimo> resultado = emprestimoService.buscarPorLivro(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveCalcularMultaParaEmprestimoAtrasado() {
        emprestimo.setDataDevolucao(LocalDate.now().minusDays(5));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        BigDecimal multa = emprestimoService.calcularMultaEmprestimo(1L);

        assertEquals(BigDecimal.valueOf(10.00), multa);
    }

    @Test
    void deveRetornarZeroParaEmprestimoNaoAtrasado() {
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

        BigDecimal multa = emprestimoService.calcularMultaEmprestimo(1L);

        assertEquals(BigDecimal.ZERO, multa);
    }

    @Test
    void devePagarMultaComSucesso() {
        Multa multa = new Multa();
        multa.setId(1L);
        multa.setPaga(false);
        
        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));

        emprestimoService.pagarMulta(1L);

        assertTrue(multa.isPaga());
        assertNotNull(multa.getDataPagamento());
        verify(multaRepository).save(multa);
    }
}
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
        
        livro = new Livro("Dom Casmurro", "Machado de Assis", "123456789", 2000);
        livro.setId(1L);
        livro.setDisponivel(true);
        
        cliente = new Cliente("João Silva", "joao@email.com", "987654321", "Rua Exemplo");
        cliente.setId(1L);
        
        emprestimo = new Emprestimo(livro, cliente);
        emprestimo.setId(1L);
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
    }

    @Test
    void deveRealizarEmprestimoComSucesso() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(Collections.emptyList());
        when(emprestimoRepository.findByClienteId(1L)).thenReturn(Collections.emptyList());


        Emprestimo resultado = emprestimoService.realizarEmprestimo(1L, 1L);

        assertNotNull(resultado);
        assertFalse(livro.isDisponivel());
        assertEquals(LocalDate.now().plusDays(7), resultado.getDataDevolucaoPrevista());
        verify(livroService).cadastraLivro(livro);
        verify(emprestimoRepository).save(any(Emprestimo.class));
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoEncontradoAoRealizarEmprestimo() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoDisponivelAoRealizarEmprestimo() {
        livro.setDisponivel(false);
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));

        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontradoAoRealizarEmprestimo() {
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }

    @Test
    void deveLancarExcecaoQuandoClienteTemMultasNaoPagasAoRealizarEmprestimo() {
        Multa multa = new Multa();
        multa.setPaga(false);
        
        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(List.of(multa));
        when(emprestimoRepository.findByClienteId(1L)).thenReturn(Collections.emptyList());


        assertThrows(IllegalStateException.class, 
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }
    
    @Test
    void deveLancarExcecaoQuandoClienteTemEmprestimosAtrasadosAoRealizarEmprestimo() {
        Emprestimo emprestimoAtrasado = new Emprestimo(new Livro(), cliente);
        emprestimoAtrasado.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));
        emprestimoAtrasado.setDevolvido(false);

        when(livroService.buscarLivroPorId(1L)).thenReturn(Optional.of(livro));
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(Collections.emptyList());
        when(emprestimoRepository.findByClienteId(1L)).thenReturn(List.of(emprestimoAtrasado));

        assertThrows(IllegalStateException.class,
            () -> emprestimoService.realizarEmprestimo(1L, 1L));
    }


    @Test
    void deveRegistrarDevolucaoComSucessoSemMulta() {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(1));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);

        Emprestimo resultado = emprestimoService.devolverLivro(1L);

        assertTrue(resultado.isDevolvido());
        assertTrue(livro.isDisponivel());
        assertEquals(LocalDate.now(), resultado.getDataDevolucaoReal());
        verify(livroService).cadastraLivro(livro);
        verify(emprestimoRepository).save(emprestimo);
        verify(multaRepository, never()).save(any(Multa.class));
    }

    @Test
    void deveAplicarMultaQuandoDevolucaoAtrasada() {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().minusDays(5));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(multaRepository.findByEmprestimoId(1L)).thenReturn(Collections.emptyList());
        
        Multa multaSimulada = new Multa(emprestimo, EmprestimoService.VALOR_MULTA_DIARIA.multiply(BigDecimal.valueOf(5)));
        when(multaRepository.save(any(Multa.class))).thenReturn(multaSimulada);

        Emprestimo resultado = emprestimoService.devolverLivro(1L);
        
        assertTrue(resultado.isDevolvido());
        assertTrue(livro.isDisponivel());
        assertEquals(LocalDate.now(), resultado.getDataDevolucaoReal());
        verify(livroService).cadastraLivro(livro);
        verify(emprestimoRepository).save(emprestimo);
        verify(multaRepository).save(argThat(multa -> 
            multa.getEmprestimo().equals(emprestimo) &&
            multa.getValor().compareTo(EmprestimoService.VALOR_MULTA_DIARIA.multiply(BigDecimal.valueOf(5))) == 0 &&
            !multa.isPaga()
        ));
    }
    
    @Test
    void naoDeveAplicarMultaSeJaExistirMultaNaoPagaParaOEmprestimo() {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().minusDays(5));
        Multa multaExistente = new Multa(emprestimo, BigDecimal.TEN);
        multaExistente.setPaga(false);

        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(multaRepository.findByEmprestimoId(1L)).thenReturn(List.of(multaExistente));
        
        emprestimoService.devolverLivro(1L);

        verify(multaRepository, never()).save(any(Multa.class));
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
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(1));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);


        emprestimoService.renovarEmprestimo(1L);

        assertTrue(emprestimo.isRenovado());
        assertEquals(LocalDate.now().plusDays(1).plusDays(7), emprestimo.getDataDevolucaoPrevista());
        verify(emprestimoRepository).save(emprestimo);
    }
    
    @Test
    void deveRetornarEmprestimoRenovadoComSucesso() {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(1));
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);

        Emprestimo emprestimoRenovado = emprestimoService.renovarEmprestimoEretornar(1L);

        assertNotNull(emprestimoRenovado);
        assertTrue(emprestimoRenovado.isRenovado());
        assertEquals(LocalDate.now().plusDays(1).plusDays(7), emprestimoRenovado.getDataDevolucaoPrevista());
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
    void deveRetornarZeroParaEmprestimoNaoAtrasadoAoCalcularMulta() {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(1));
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
        
        emprestimoService.devolverLivro(1L);

        verify(multaRepository, never()).save(any(Multa.class));
    }


    @Test
    void devePagarMultaComSucesso() {
        Multa multa = new Multa();
        multa.setId(1L);
        multa.setPaga(false);
        
        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));
        when(multaRepository.save(any(Multa.class))).thenReturn(multa);


        Multa multaPaga = emprestimoService.pagarMulta(1L);

        assertTrue(multaPaga.isPaga());
        assertNotNull(multaPaga.getDataPagamento());
        verify(multaRepository).save(multa);
    }

    @Test
    void deveLancarExcecaoAoPagarMultaInexistente() {
        when(multaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> emprestimoService.pagarMulta(1L));
    }

    @Test
    void deveLancarExcecaoAoPagarMultaJaPaga() {
        Multa multa = new Multa();
        multa.setId(1L);
        multa.setPaga(true);
        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));
        assertThrows(IllegalStateException.class, () -> emprestimoService.pagarMulta(1L));
    }
    
    @Test
    void deveListarTodosEmprestimos() {
        when(emprestimoRepository.findAll()).thenReturn(List.of(emprestimo));
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();
        assertFalse(emprestimos.isEmpty());
        assertEquals(1, emprestimos.size());
    }

    @Test
    void deveListarEmprestimosAtrasados() {
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));
        emprestimo.setDevolvido(false);
        when(emprestimoRepository.findByDataDevolucaoRealIsNullAndDataDevolucaoPrevistaBefore(any(LocalDate.class)))
            .thenReturn(List.of(emprestimo));
        
        List<Emprestimo> emprestimosAtrasados = emprestimoService.listarEmprestimosAtrasados();
        
        assertFalse(emprestimosAtrasados.isEmpty());
        assertEquals(1, emprestimosAtrasados.size());
        assertTrue(emprestimosAtrasados.get(0).estaAtrasado());
    }
    
    @Test
    void deveBuscarMultasPendentesPorCliente() {
        Multa multaPendente = new Multa(emprestimo, BigDecimal.TEN);
        multaPendente.setPaga(false);
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(List.of(multaPendente));

        List<Multa> multas = emprestimoService.buscarMultasPorCliente(1L);

        assertFalse(multas.isEmpty());
        assertEquals(1, multas.size());
        assertFalse(multas.get(0).isPaga());
    }

    @Test
    void deveCalcularTotalMultasPendentesCliente() {
        Multa multa1 = new Multa(emprestimo, BigDecimal.TEN);
        multa1.setPaga(false);
        Emprestimo outroEmprestimo = new Emprestimo(livro, cliente);
        outroEmprestimo.setId(2L);
        Multa multa2 = new Multa(outroEmprestimo, BigDecimal.valueOf(5));
        multa2.setPaga(false);
        
        when(multaRepository.findByEmprestimoClienteIdAndPagaFalse(1L)).thenReturn(List.of(multa1, multa2));

        BigDecimal total = emprestimoService.calcularTotalMultasCliente(1L);

        assertEquals(BigDecimal.valueOf(15), total);
    }
}
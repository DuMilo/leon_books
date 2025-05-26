package br.com.leonbooks.leon_books;

import br.com.leonbooks.leon_books.controller.EmprestimoController;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmprestimoControllerTest {

    @Mock
    private EmprestimoService emprestimoService;

    @InjectMocks
    private EmprestimoController emprestimoController;

    private Emprestimo emprestimo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
    }

    @Test
    void deveCriarEmprestimoComSucesso() {
        when(emprestimoService.realizarEmprestimo(anyLong(), anyLong())).thenReturn(emprestimo);

        ResponseEntity<Emprestimo> response = emprestimoController.criarEmprestimo(1L, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveRegistrarDevolucaoComSucesso() {
        doNothing().when(emprestimoService).devolverLivro(anyLong());

        ResponseEntity<Void> response = emprestimoController.devolverLivro(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(emprestimoService, times(1)).devolverLivro(1L);
    }

    @Test
    void deveRenovarEmprestimoComSucesso() {
        doNothing().when(emprestimoService).renovarEmprestimo(anyLong());

        ResponseEntity<Void> response = emprestimoController.renovarEmprestimo(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(emprestimoService, times(1)).renovarEmprestimo(1L);
    }

    @Test
    void deveListarEmprestimosPorCliente() {
        when(emprestimoService.buscarPorCliente(anyLong())).thenReturn(List.of(emprestimo));

        List<Emprestimo> response = emprestimoController.listarPorCliente(1L);

        assertFalse(response.isEmpty());
    }

    @Test
    void deveListarEmprestimosPorLivro() {
        when(emprestimoService.buscarPorLivro(anyLong())).thenReturn(List.of(emprestimo));

        List<Emprestimo> response = emprestimoController.listarPorLivro(1L);

        assertFalse(response.isEmpty());
    }

    @Test
    void deveCalcularMultaParaEmprestimo() {
        when(emprestimoService.calcularMultaEmprestimo(anyLong())).thenReturn(BigDecimal.TEN);

        ResponseEntity<BigDecimal> response = emprestimoController.verificarMulta(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.TEN, response.getBody());
    }
}
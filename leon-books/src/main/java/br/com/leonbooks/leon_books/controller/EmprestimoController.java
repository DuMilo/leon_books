package br.com.leonbooks.leon_books.controller;

import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("/novo-emprestimo")
    public ResponseEntity<Emprestimo> criarEmprestimo(
            @RequestParam Long clienteId,
            @RequestParam Long livroId) {
        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(livroId, clienteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverLivro(@PathVariable Long id) {
        emprestimoService.devolverLivro(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<Void> renovarEmprestimo(@PathVariable Long id) {
        emprestimoService.renovarEmprestimo(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/por-cliente/{clienteId}")
    public List<Emprestimo> listarPorCliente(@PathVariable Long clienteId) {
        return emprestimoService.buscarPorCliente(clienteId);
    }

    @GetMapping("/por-livro/{livroId}")
    public List<Emprestimo> listarPorLivro(@PathVariable Long livroId) {
        return emprestimoService.buscarPorLivro(livroId);
    }

    @GetMapping("/{id}/multa")
    public ResponseEntity<BigDecimal> verificarMulta(@PathVariable Long id) {
        BigDecimal valorMulta = emprestimoService.calcularMultaEmprestimo(id);
        return ResponseEntity.ok(valorMulta);
    }
}
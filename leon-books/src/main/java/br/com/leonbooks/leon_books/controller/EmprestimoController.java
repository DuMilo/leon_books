package br.com.leonbooks.leon_books.controller;

import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Multa;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<?> realizarEmprestimo(@RequestParam Long livroId, @RequestParam Long clienteId) {
        try {
            Emprestimo emprestimo = emprestimoService.realizarEmprestimo(livroId, clienteId);
            return new ResponseEntity<>(emprestimo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<?> devolverLivro(@PathVariable Long id) {
        try {
            Emprestimo emprestimo = emprestimoService.devolverLivro(id);
            return ResponseEntity.ok(emprestimo);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<?> renovarEmprestimo(@PathVariable Long id) {
        try {
            Emprestimo emprestimo = emprestimoService.renovarEmprestimoEretornar(id);
            return ResponseEntity.ok(emprestimo);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodosEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<Emprestimo>> listarEmprestimosAtrasados() {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAtrasados();
        return ResponseEntity.ok(emprestimos);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

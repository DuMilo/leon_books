package br.com.leonbooks.leon_books.controller;

import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<Emprestimo> realizarEmprestimo(@RequestParam Long livroId, @RequestParam Long clienteId) {
        Emprestimo novoEmprestimo = emprestimoService.realizarEmprestimo(livroId, clienteId);
        return new ResponseEntity<>(novoEmprestimo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimos);
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Emprestimo> devolverLivro(@PathVariable Long id) {
        Emprestimo emprestimoDevolvido = emprestimoService.devolverLivro(id);
        if (emprestimoDevolvido != null) {
            return ResponseEntity.ok(emprestimoDevolvido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<Emprestimo>> listarAtrasados() {
        List<Emprestimo> emprestimosAtrasados = emprestimoService.listarEmprestimosAtrasados();
        return ResponseEntity.ok(emprestimosAtrasados);
    }

    @PutMapping("/{id}/renovar")
    public ResponseEntity<Emprestimo> renovarEmprestimo(@PathVariable Long id) {
        try {
            Emprestimo emprestimoRenovado = emprestimoService.renovarEmprestimoEretornar(id);
            return ResponseEntity.ok(emprestimoRenovado);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

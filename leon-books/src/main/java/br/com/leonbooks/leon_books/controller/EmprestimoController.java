package br.com.leonbooks.leon_books.controller;

import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.service.EmprestimoService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public Emprestimo realizarEmprestimo(
        @RequestParam int livroId,   // Mudança: String -> int
        @RequestParam int clienteId  // Mudança: String -> int
    ) {
        return emprestimoService.realizarEmprestimo(livroId, clienteId);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Emprestimo> buscarPorCliente(@PathVariable int clienteId) {  // Mudança: String -> int
        return emprestimoService.buscarPorCliente(clienteId);
    }

    @GetMapping("/livro/{livroId}")
    public List<Emprestimo> buscarPorLivro(@PathVariable int livroId) {  // Mudança: String -> int
        return emprestimoService.buscarPorLivro(livroId);
    }
}
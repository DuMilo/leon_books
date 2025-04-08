package br.com.leonbooks.leon_books.controller;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.LivroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService){
        this.livroService = livroService;
    }

    @GetMapping
    public List<Livro> buscarTodosLivros(){
        return livroService.buscarTodosLivros();
    }

    @GetMapping("/disponiveis")
    public List<Livro> buscarLivrosDisponiveis(){
        return livroService.buscarLivrosDisponiveis();
    }

    @PostMapping
    public Livro cadastraLivro(@RequestBody Livro livro){
        return livroService.cadastraLivro(livro);
    }

    @GetMapping("/{id}")
    public Livro buscarLivroPorId(@PathVariable Integer id){
        return livroService.buscarLivroPorId(id).orElse(null);
    }
}

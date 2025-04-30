package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository){
        this.livroRepository = livroRepository;
    }

    public List<Livro> buscarTodosLivros(){
        return livroRepository.buscarTodos();
    }

    public List<Livro> buscarLivrosDisponiveis(){
        return livroRepository.buscarDisponiveis();
    }

    public Livro cadastraLivro(Livro livro){
        return livroRepository.salvar(livro);
    }

    public Optional<Livro> buscarLivroPorId(int id){
        return livroRepository.buscarPorId(id);
    }

    public Optional<Livro> buscarLivroPorId(String id) {
        try {
            return buscarLivroPorId(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}

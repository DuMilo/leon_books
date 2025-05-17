package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository){
        this.livroRepository = livroRepository;
    }

    @Transactional
    public Livro cadastraLivro(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("Título do livro é obrigatório.");
        }
        if (livro.getAutor() == null || livro.getAutor().isEmpty()) {
            throw new IllegalArgumentException("Autor do livro é obrigatório.");
        }
        return livroRepository.save(livro);
    }

    public List<Livro> buscarTodosLivros(){
        return livroRepository.findAll();
    }

    public List<Livro> buscarLivrosDisponiveis(){
        return livroRepository.findLivrosDisponiveis();
    }

    public Optional<Livro> buscarLivroPorId(Long id){
        return livroRepository.findById(id);
    }

    @Transactional
    public void atualizarLivro(Livro livro) {
        if (!livroRepository.existsById(livro.getId())) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }
        livroRepository.save(livro);
    }

    @Transactional
    public void removerLivro(Long livroId) {
        if (!livroRepository.existsById(livroId)) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }
        livroRepository.deleteById(livroId);
    }
}

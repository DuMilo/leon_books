package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    @Autowired
    public LivroService(LivroRepository livroRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public Livro cadastraLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public List<Livro> buscarTodosLivros() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarLivroPorId(Long id) {
        return livroRepository.findById(id);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<Livro> buscarLivrosDisponiveis() {
        return livroRepository.findByDisponivelTrue();
    }

    @Transactional
    public Livro atualizarLivro(Long id, Livro livroDetails) { 
        Optional<Livro> livroExistenteOptional = livroRepository.findById(id);
        if (livroExistenteOptional.isPresent()) {
            Livro livroExistente = livroExistenteOptional.get();
            livroExistente.setTitulo(livroDetails.getTitulo());
            livroExistente.setAutor(livroDetails.getAutor());
            livroExistente.setIsbn(livroDetails.getIsbn());
            livroExistente.setAnoPublicacao(livroDetails.getAnoPublicacao());
            livroExistente.setDisponivel(livroDetails.isDisponivel());
            return livroRepository.save(livroExistente);
        } else {
            // throw new RuntimeException("Livro com ID " + id + " não encontrado para atualização.");
            return null;
        }
    }

    @Transactional
    public boolean deletarLivro(Long id) { 
        Optional<Livro> livroOptional = livroRepository.findById(id);
        if (livroOptional.isPresent()) {
            // if (emprestimoRepository.countByLivroIdAndDevolvidoFalse(id) > 0) {
            //     throw new IllegalStateException("Não é possível deletar o livro ID " + id + " pois ele possui empréstimos ativos.");
            // }
            livroRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

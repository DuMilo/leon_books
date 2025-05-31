package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
import br.com.leonbooks.leon_books.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public Livro cadastraLivro(Livro livro) {
        if (!StringUtils.hasText(livro.getTitulo())) {
            throw new IllegalArgumentException("O título do livro não pode ser vazio.");
        }
        if (!StringUtils.hasText(livro.getAutor())) {
            throw new IllegalArgumentException("O autor do livro não pode ser vazio.");
        }
        return livroRepository.save(livro);
    }

    public Optional<Livro> buscarLivroPorId(Long id) {
        return livroRepository.findById(id);
    }

    public List<Livro> buscarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<Livro> buscarLivrosDisponiveis() {
        return livroRepository.findByDisponivelTrue();
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public Livro atualizarLivro(Long id, Livro livroDetails) {
        Optional<Livro> livroOptional = livroRepository.findById(id);
        if (livroOptional.isEmpty()) {
            return null; 
        }

        Livro livroExistente = livroOptional.get();

        if (!StringUtils.hasText(livroDetails.getTitulo())) {
            throw new IllegalArgumentException("O título do livro não pode ser vazio.");
        }
        if (!StringUtils.hasText(livroDetails.getAutor())) {
            throw new IllegalArgumentException("O autor do livro não pode ser vazio.");
        }
        
        livroExistente.setTitulo(livroDetails.getTitulo());
        livroExistente.setAutor(livroDetails.getAutor());
        livroExistente.setIsbn(livroDetails.getIsbn());
        livroExistente.setAnoPublicacao(livroDetails.getAnoPublicacao());
        livroExistente.setDisponivel(livroDetails.isDisponivel());

        return livroRepository.save(livroExistente);
    }

    public boolean deletarLivro(Long livroId) {
        Optional<Livro> livroOptional = livroRepository.findById(livroId);
        if (livroOptional.isEmpty()) {
            return false; // Livro não encontrado
        }

        // Verifica se existem empréstimos não devolvidos para este livro
        long countEmprestimosAtivos = emprestimoRepository.countByLivroIdAndDevolvidoFalse(livroId);
        if (countEmprestimosAtivos > 0) {
            throw new IllegalStateException("Não é possível excluir o livro pois existem empréstimos ativos associados a ele.");
        }

        livroRepository.deleteById(livroId);
        return true;
    }
}

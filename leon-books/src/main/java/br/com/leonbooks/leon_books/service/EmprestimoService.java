package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final ClienteService clienteService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroService livroService, ClienteService clienteService) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
        this.clienteService = clienteService;
    }

    @Transactional
    public Emprestimo realizarEmprestimo(Long livroId, Long clienteId) {
        Livro livro = livroService.buscarLivroPorId(livroId)
            .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado!"));

        if (!livro.isDisponivel()) {
            throw new IllegalStateException("Livro já emprestado!");
        }

        Cliente cliente = clienteService.buscarPorId(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado!"));

        if (clienteTemAtrasos(clienteId)) {
            throw new IllegalStateException("Cliente com empréstimos atrasados!");
        }

        Emprestimo emprestimo = new Emprestimo(livro, cliente);
        
        cliente.adicionarEmprestimo(emprestimo);
        
        livro.setDisponivel(false);
        livroService.cadastraLivro(livro);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public void devolverLivro(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado!"));

        if (emprestimo.isDevolvido()) {
            throw new IllegalStateException("Livro já foi devolvido anteriormente.");
        }

        emprestimo.setDevolvido(true);
        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);

        livroService.cadastraLivro(livro);
        emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public void renovarEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado!"));

        if (emprestimo.isDevolvido()) {
            throw new IllegalStateException("Não é possível renovar um empréstimo já devolvido.");
        }

        if (emprestimo.isRenovado()) {
            throw new IllegalStateException("Este empréstimo já foi renovado anteriormente.");
        }

        if (emprestimo.estaAtrasado()) {
            throw new IllegalStateException("Não é possível renovar empréstimo atrasado.");
        }

        emprestimo.renovarEmprestimo();
        emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> buscarPorCliente(Long clienteId) {
        return emprestimoRepository.findByClienteId(clienteId);
    }

    public List<Emprestimo> buscarPorLivro(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }

    public List<Emprestimo> buscarTodosEmprestimos() {
        return emprestimoRepository.findAll();
    }

    private boolean clienteTemAtrasos(Long clienteId) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByClienteId(clienteId);
        LocalDate hoje = LocalDate.now();
        
        return emprestimos.stream()
            .anyMatch(e -> !e.isDevolvido() && e.getDataDevolucao().isBefore(hoje));
    }
}
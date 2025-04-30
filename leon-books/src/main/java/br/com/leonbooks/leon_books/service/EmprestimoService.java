package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final ClienteService clienteService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroService livroService, ClienteService clienteService){
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
        this.clienteService = clienteService;
    }

    public Emprestimo realizarEmprestimo(int livroId, int clienteId) {
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

        livro.setDisponivel(false);
        livroService.cadastraLivro(livro);

        cliente.adicionarEmprestimo(emprestimo);

        return emprestimoRepository.salvar(emprestimo);
    }

    public List<Emprestimo> buscarPorCliente(int clienteId) {
        return emprestimoRepository.buscarPorCliente(clienteId);
    }

    public List<Emprestimo> buscarPorLivro(int livroId) {
        return emprestimoRepository.buscarPorLivro(livroId);
    }

    private boolean clienteTemAtrasos(int clienteId) {
        List<Emprestimo> emprestimos = emprestimoRepository.buscarPorCliente(clienteId);
        LocalDate hoje = LocalDate.now();
        return emprestimos.stream()
            .anyMatch(e -> !e.isDevolvido() && e.getDataDevolucao().isBefore(hoje));
    }

    public Emprestimo realizarEmprestimo(String livroId, String clienteId) {
        try {
            return realizarEmprestimo(
                Integer.parseInt(livroId),
                Integer.parseInt(clienteId)
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("IDs devem ser números válidos!");
        }
    }
}
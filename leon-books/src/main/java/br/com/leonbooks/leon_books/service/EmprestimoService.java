package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.model.Multa;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
import br.com.leonbooks.leon_books.repository.MultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final ClienteService clienteService;
    private final MultaRepository multaRepository;

    public static final BigDecimal VALOR_MULTA_DIARIA = BigDecimal.valueOf(2.00);

    public EmprestimoService(EmprestimoRepository emprestimoRepository, 
                           LivroService livroService,
                           ClienteService clienteService,
                           MultaRepository multaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
        this.clienteService = clienteService;
        this.multaRepository = multaRepository;
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

        if (emprestimo.estaAtrasado()) {
            aplicarMulta(emprestimo);
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
        List<Multa> multasNaoPagas = multaRepository.findByEmprestimoClienteIdAndPagaFalse(clienteId);
        return !multasNaoPagas.isEmpty();
    }

    private void aplicarMulta(Emprestimo emprestimo) {
        long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucao(), LocalDate.now());
        BigDecimal valorMulta = VALOR_MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso));
        
        Multa multa = new Multa(emprestimo, valorMulta);
        multaRepository.save(multa);
    }

    public List<Multa> buscarMultasPorCliente(Long clienteId) {
        return multaRepository.findByEmprestimoClienteIdAndPagaFalse(clienteId);
    }

    public BigDecimal calcularTotalMultasCliente(Long clienteId) {
        List<Multa> multas = buscarMultasPorCliente(clienteId);
        return multas.stream()
                .map(Multa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularMultaEmprestimo(Long emprestimoId) {
        List<Multa> multas = multaRepository.findByEmprestimoId(emprestimoId);
        Optional<Multa> multaNaoPaga = multas.stream()
                .filter(m -> !m.isPaga())
                .findFirst();

        if (multaNaoPaga.isPresent()) {
            return multaNaoPaga.get().getValor();
        }

        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));

        if (emprestimo.estaAtrasado() && !emprestimo.isDevolvido()) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucao(), LocalDate.now());
            return VALOR_MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso));
        }

        return BigDecimal.ZERO;
    }

    @Transactional
    public void pagarMulta(Long multaId) {
        Multa multa = multaRepository.findById(multaId)
                .orElseThrow(() -> new IllegalArgumentException("Multa não encontrada!"));
        
        if (multa.isPaga()) {
            throw new IllegalStateException("Esta multa já foi paga anteriormente.");
        }
        
        multa.setPaga(true);
        multa.setDataPagamento(LocalDate.now());
        multaRepository.save(multa);
    }
}
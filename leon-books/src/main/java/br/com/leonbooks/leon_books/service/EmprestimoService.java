package br.com.leonbooks.leon_books.service;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.model.Multa;
//import br.com.leonbooks.leon_books.repository.ClienteRepository;
import br.com.leonbooks.leon_books.repository.EmprestimoRepository;
//import br.com.leonbooks.leon_books.repository.LivroRepository;
import br.com.leonbooks.leon_books.repository.MultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
                .orElseThrow(() -> new IllegalArgumentException("Livro com ID " + livroId + " não encontrado!"));

        if (!livro.isDisponivel()) {
            throw new IllegalStateException("Livro '" + livro.getTitulo() + "' já está emprestado!");
        }

        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado!"));

        if (clienteTemAtrasosOuMultasPendentes(clienteId)) {
            throw new IllegalStateException("Cliente com ID " + clienteId + " possui empréstimos atrasados ou multas pendentes!");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setCliente(cliente);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));
        emprestimo.setDevolvido(false);
        emprestimo.setRenovado(false);

        livro.setDisponivel(false);
        livroService.cadastraLivro(livro);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo devolverLivro(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo com ID " + emprestimoId + " não encontrado!"));

        if (emprestimo.isDevolvido()) {
            throw new IllegalStateException("Livro do empréstimo ID " + emprestimoId + " já foi devolvido.");
        }

        emprestimo.setDevolvido(true);
        emprestimo.setDataDevolucaoReal(LocalDate.now());

        if (emprestimo.getDataDevolucaoReal().isAfter(emprestimo.getDataDevolucaoPrevista())) {
            aplicarMulta(emprestimo);
        }

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroService.cadastraLivro(livro);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo renovarEmprestimoEretornar(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo ID " + emprestimoId + " não encontrado para renovação."));
        emprestimo.renovarEmprestimo();
        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public void renovarEmprestimo(Long emprestimoId) { 
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo ID " + emprestimoId + " não encontrado para renovação."));
        emprestimo.renovarEmprestimo();
        emprestimoRepository.save(emprestimo);
    }

    public Optional<Emprestimo> buscarPorId(Long emprestimoId) {
        return emprestimoRepository.findById(emprestimoId);
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public List<Emprestimo> buscarPorCliente(Long clienteId) {
        return emprestimoRepository.findByClienteId(clienteId);
    }

    public List<Emprestimo> buscarPorLivro(Long livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }

    public List<Emprestimo> listarEmprestimosAtrasados() {
        return emprestimoRepository.findByDataDevolucaoRealIsNullAndDataDevolucaoPrevistaBefore(LocalDate.now());
    }

    private boolean clienteTemAtrasosOuMultasPendentes(Long clienteId) {
        List<Emprestimo> emprestimosDoCliente = emprestimoRepository.findByClienteId(clienteId);
        for (Emprestimo emp : emprestimosDoCliente) {
            if (!emp.isDevolvido() && emp.getDataDevolucaoPrevista().isBefore(LocalDate.now())) {
                return true;
            }
        }
        List<Multa> multasNaoPagas = multaRepository.findByEmprestimoClienteIdAndPagaFalse(clienteId);
        return !multasNaoPagas.isEmpty();
    }

    private void aplicarMulta(Emprestimo emprestimo) {
        if (emprestimo.getDataDevolucaoReal() != null && emprestimo.getDataDevolucaoReal().isAfter(emprestimo.getDataDevolucaoPrevista())) {
            boolean jaTemMultaNaoPaga = multaRepository.findByEmprestimoId(emprestimo.getId())
                                        .stream().anyMatch(m -> !m.isPaga());
            if (jaTemMultaNaoPaga) {
                return;
            }

            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), emprestimo.getDataDevolucaoReal());
            if (diasAtraso > 0) {
                BigDecimal valorMulta = VALOR_MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso));
                Multa multa = new Multa();
                multa.setEmprestimo(emprestimo);
                multa.setValor(valorMulta);
                multa.setDataAplicacao(LocalDate.now());
                multa.setPaga(false);
                multaRepository.save(multa);
            }
        }
    }

    public List<Multa> buscarMultasPorCliente(Long clienteId) {
        return multaRepository.findByEmprestimoClienteIdAndPagaFalse(clienteId);
    }

    public BigDecimal calcularTotalMultasCliente(Long clienteId) {
        List<Multa> multasNaoPagas = buscarMultasPorCliente(clienteId);
        return multasNaoPagas.stream()
                .map(Multa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Multa pagarMulta(Long multaId) {
        Multa multa = multaRepository.findById(multaId)
                .orElseThrow(() -> new IllegalArgumentException("Multa com ID " + multaId + " não encontrada!"));

        if (multa.isPaga()) {
            throw new IllegalStateException("Multa com ID " + multaId + " já foi paga.");
        }

        multa.setPaga(true);
        multa.setDataPagamento(LocalDate.now());
        return multaRepository.save(multa);
    }
}

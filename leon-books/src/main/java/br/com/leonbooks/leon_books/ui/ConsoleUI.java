package br.com.leonbooks.leon_books.ui;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.ClienteService;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import br.com.leonbooks.leon_books.service.LivroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {

    private final LivroService livroService;
    private final ClienteService clienteService;
    private final EmprestimoService emprestimoService;
    private final Scanner scanner;

    public ConsoleUI(LivroService livroService,
                   ClienteService clienteService,
                   EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.clienteService = clienteService;
        this.emprestimoService = emprestimoService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== LEON BOOKS - SISTEMA DE BIBLIOTECA (Modo Console) ===");
       
        while (true) {
            exibirMenuPrincipal();
            String input = scanner.nextLine();
           
            try {
                int opcao = Integer.parseInt(input);
               
                switch (opcao) {
                    case 1 -> listarLivrosDisponiveis();
                    case 2 -> listarTodosOsLivros();
                    case 3 -> buscarLivrosPorTitulo();
                    case 4 -> buscarLivrosPorAutor();
                    case 5 -> listarClientes();
                    case 6 -> buscarClientesPorNome();
                    case 7 -> buscarClientesPorEmail();
                    case 8 -> realizarEmprestimo();
                    case 9 -> registrarDevolucao();
                    case 10 -> cadastrarLivro();
                    case 11 -> cadastrarCliente();
                    case 12 -> renovarEmprestimo();
                    case 13 -> listarEmprestimos();
                    case 0 -> {
                        System.out.println("Saindo do sistema...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\nMENU PRINCIPAL");
        System.out.println("1. Listar livros disponíveis");
        System.out.println("2. Listar todos os livros");
        System.out.println("3. Buscar livros por título");
        System.out.println("4. Buscar livros por autor");
        System.out.println("5. Listar clientes");
        System.out.println("6. Buscar clientes por nome");
        System.out.println("7. Buscar clientes por e-mail");
        System.out.println("8. Realizar empréstimo");
        System.out.println("9. Registrar devolução");
        System.out.println("10. Cadastrar novo livro");
        System.out.println("11. Cadastrar cliente");
        System.out.println("12. Renovar empréstimo");
        System.out.println("13. Listar empréstimos");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // ========== MÉTODOS DE LIVROS ==========
    private void listarLivrosDisponiveis() {
        System.out.println("\nLIVROS DISPONÍVEIS:");
        livroService.buscarLivrosDisponiveis().forEach(livro -> {
            System.out.println("ID: " + livro.getId());
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor());
            System.out.println("-----------------------");
        });
    }

    private void listarTodosOsLivros() {
        System.out.println("\nTODOS OS LIVROS:");
        livroService.buscarTodosLivros().forEach(this::imprimirDetalhesLivro);
    }

    private void buscarLivrosPorTitulo() {
        System.out.print("\nDigite parte do título: ");
        String titulo = scanner.nextLine();
        
        List<Livro> livros = livroService.buscarPorTitulo(titulo);
        
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com este título.");
            return;
        }
        
        System.out.println("\nRESULTADOS:");
        livros.forEach(this::imprimirDetalhesLivro);
    }

    private void buscarLivrosPorAutor() {
        System.out.print("\nDigite parte do nome do autor: ");
        String autor = scanner.nextLine();
        
        List<Livro> livros = livroService.buscarPorAutor(autor);
        
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado deste autor.");
            return;
        }
        
        System.out.println("\nRESULTADOS:");
        livros.forEach(this::imprimirDetalhesLivro);
    }

    private void imprimirDetalhesLivro(Livro livro) {
        System.out.println("ID: " + livro.getId());
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor());
        System.out.println("Disponível: " + (livro.isDisponivel() ? "Sim" : "Não"));
        
        if (!livro.isDisponivel()) {
            List<Emprestimo> emprestimos = emprestimoService.buscarPorLivro(livro.getId());
            emprestimos.stream()
                .filter(e -> !e.isDevolvido())
                .findFirst()
                .ifPresent(emprestimo -> {
                    System.out.println("  Empréstimo ID: " + emprestimo.getId());
                    System.out.println("  Cliente: ID " + emprestimo.getCliente().getId() + 
                                     " - " + emprestimo.getCliente().getNome());
                    System.out.println("  Data devolução: " + emprestimo.getDataDevolucao());
                });
        }
        System.out.println("-----------------------");
    }

    // ========== MÉTODOS DE CLIENTES ==========
    private void listarClientes() {
        System.out.println("\nCLIENTES CADASTRADOS:");
        clienteService.buscarTodos().forEach(this::imprimirDetalhesCliente);
    }

    private void buscarClientesPorNome() {
        System.out.print("\nDigite parte do nome: ");
        String nome = scanner.nextLine();
        
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
            return;
        }
        
        System.out.println("\nRESULTADOS:");
        clientes.forEach(this::imprimirDetalhesCliente);
    }

    private void buscarClientesPorEmail() {
        System.out.print("\nDigite parte do e-mail: ");
        String email = scanner.nextLine();
        
        List<Cliente> clientes = clienteService.buscarPorEmail(email);
        
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
            return;
        }
        
        System.out.println("\nRESULTADOS:");
        clientes.forEach(this::imprimirDetalhesCliente);
    }

    private void imprimirDetalhesCliente(Cliente cliente) {
        System.out.println("ID: " + cliente.getId());
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("E-mail: " + cliente.getEmail());
        
        List<Emprestimo> emprestimos = emprestimoService.buscarPorCliente(cliente.getId());
        if (!emprestimos.isEmpty()) {
            System.out.println("Empréstimos ativos:");
            emprestimos.stream()
                .filter(e -> !e.isDevolvido())
                .forEach(emprestimo -> {
                    System.out.println("  Empréstimo ID: " + emprestimo.getId());
                    System.out.println("  Livro: ID " + emprestimo.getLivro().getId() + 
                                     " - " + emprestimo.getLivro().getTitulo());
                    System.out.println("  Data devolução: " + emprestimo.getDataDevolucao());
                });
        }
        System.out.println("-----------------------");
    }

    // ========== MÉTODOS DE EMPRÉSTIMOS ==========
    private void listarEmprestimos() {
        System.out.println("\nOPÇÕES:");
        System.out.println("1. Listar todos");
        System.out.println("2. Por cliente");
        System.out.println("3. Por livro");
        System.out.print("Escolha: ");
        
        int opcao = Integer.parseInt(scanner.nextLine());
        
        switch (opcao) {
            case 1 -> listarTodosEmprestimos();
            case 2 -> listarEmprestimosPorCliente();
            case 3 -> listarEmprestimosPorLivro();
            default -> System.out.println("Inválido!");
        }
    }

    private void listarTodosEmprestimos() {
        System.out.println("\nTODOS OS EMPRÉSTIMOS:");
        emprestimoService.buscarTodosEmprestimos().forEach(this::imprimirDetalhesEmprestimo);
    }

    private void listarEmprestimosPorCliente() {
        System.out.print("\nID do cliente: ");
        Long clienteId = Long.parseLong(scanner.nextLine());
        
        List<Emprestimo> emprestimos = emprestimoService.buscarPorCliente(clienteId);
        
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado.");
            return;
        }
        
        System.out.println("\nEMPRÉSTIMOS DO CLIENTE:");
        emprestimos.forEach(this::imprimirDetalhesEmprestimo);
    }

    private void listarEmprestimosPorLivro() {
        System.out.print("\nID do livro: ");
        Long livroId = Long.parseLong(scanner.nextLine());
        
        List<Emprestimo> emprestimos = emprestimoService.buscarPorLivro(livroId);
        
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado.");
            return;
        }
        
        System.out.println("\nEMPRÉSTIMOS DO LIVRO:");
        emprestimos.forEach(this::imprimirDetalhesEmprestimo);
    }

    private void imprimirDetalhesEmprestimo(Emprestimo emprestimo) {
        System.out.println("ID: " + emprestimo.getId());
        System.out.println("Livro: ID " + emprestimo.getLivro().getId() + 
                         " - " + emprestimo.getLivro().getTitulo());
        System.out.println("Cliente: ID " + emprestimo.getCliente().getId() + 
                         " - " + emprestimo.getCliente().getNome());
        System.out.println("Data empréstimo: " + emprestimo.getDataEmprestimo());
        System.out.println("Data devolução: " + emprestimo.getDataDevolucao());
        System.out.println("Status: " + 
                         (emprestimo.isDevolvido() ? "Devolvido" : "Em andamento"));
        System.out.println("-----------------------");
    }

    // ========== OPERAÇÕES ==========
    private void cadastrarLivro() {
        System.out.println("\nCADASTRAR LIVRO:");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        
        Livro livro = new Livro(titulo, autor);
        livroService.cadastraLivro(livro);
        System.out.println("Livro cadastrado! ID: " + livro.getId());
    }

    private void cadastrarCliente() {
        System.out.println("\nCADASTRAR CLIENTE:");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        Cliente cliente = new Cliente(nome, email);
        clienteService.cadastrarCliente(cliente);
        System.out.println("Cliente cadastrado! ID: " + cliente.getId());
    }

    private void realizarEmprestimo() {
        System.out.println("\nNOVO EMPRÉSTIMO:");
        System.out.print("ID do livro: ");
        Long livroId = Long.parseLong(scanner.nextLine());
        
        System.out.print("ID do cliente: ");
        Long clienteId = Long.parseLong(scanner.nextLine());
        
        try {
            Emprestimo emprestimo = emprestimoService.realizarEmprestimo(livroId, clienteId);
            System.out.println("Empréstimo realizado! ID: " + emprestimo.getId());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registrarDevolucao() {
        System.out.print("\nID do empréstimo: ");
        Long emprestimoId = Long.parseLong(scanner.nextLine());
        
        try {
            emprestimoService.devolverLivro(emprestimoId);
            System.out.println("Devolução registrada!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void renovarEmprestimo() {
        System.out.print("\nID do empréstimo: ");
        Long emprestimoId = Long.parseLong(scanner.nextLine());
        
        try {
            emprestimoService.renovarEmprestimo(emprestimoId);
            System.out.println("Empréstimo renovado!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
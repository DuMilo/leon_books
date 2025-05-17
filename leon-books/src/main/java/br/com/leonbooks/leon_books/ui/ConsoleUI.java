package br.com.leonbooks.leon_books.ui;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.ClienteService;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import br.com.leonbooks.leon_books.service.LivroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        System.out.println("\n=== LEON BOOKS - SISTEMA DE BIBLIOTECA ===");
        
        while (true) {
            exibirMenuPrincipal();
            String input = scanner.nextLine();
            
            try {
                int opcao = Integer.parseInt(input);
                
                switch (opcao) {
                    case 1 -> listarLivrosDisponiveis();
                    case 2 -> listarTodosOsLivros();
                    case 3 -> listarClientes();
                    case 4 -> realizarEmprestimo();
                    case 5 -> registrarDevolucao();
                    case 6 -> cadastrarLivro();
                    case 7 -> cadastrarCliente();
                    case 8 -> renovarEmprestimo();
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
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\nMENU PRINCIPAL");
        System.out.println("1. Listar livros disponíveis");
        System.out.println("2. Listar todos os livros");
        System.out.println("3. Listar clientes");
        System.out.println("4. Realizar empréstimo");
        System.out.println("5. Registrar devolução");
        System.out.println("6. Cadastrar novo livro");
        System.out.println("7. Cadastrar cliente");
        System.out.println("8. Renovar empréstimo");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

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
        livroService.buscarTodosLivros().forEach(livro -> {
            System.out.println("ID: " + livro.getId());
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor());
            System.out.println("Disponível: " + (livro.isDisponivel() ? "Sim" : "Não"));
            System.out.println("-----------------------");
        });
    }

    private void cadastrarLivro() {
        System.out.println("\nCADASTRAR NOVO LIVRO");
        
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Autor: ");
        String autor = scanner.nextLine();

        Livro novoLivro = new Livro(titulo, autor);
        livroService.cadastraLivro(novoLivro);
        
        System.out.println("Livro cadastrado com sucesso! ID: " + novoLivro.getId());
    }

    private void cadastrarCliente() {
        System.out.println("\nCADASTRAR NOVO CLIENTE");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        Cliente cliente = new Cliente(nome, email);
        clienteService.cadastrarCliente(cliente);
        System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
    }

    private void listarClientes() {
        System.out.println("\nCLIENTES CADASTRADOS:");
        clienteService.buscarTodos().forEach(cliente -> {
            System.out.println("ID: " + cliente.getId());
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("E-mail: " + cliente.getEmail());
            System.out.println("-----------------------");
        });
    }

    private void realizarEmprestimo() {
        System.out.println("\nREALIZAR EMPRÉSTIMO");
        
        System.out.print("ID do Livro: ");
        Long livroId = Long.parseLong(scanner.nextLine());

        System.out.print("ID do Cliente: ");
        Long clienteId = Long.parseLong(scanner.nextLine());

        try {
            emprestimoService.realizarEmprestimo(livroId, clienteId);
            System.out.println("Empréstimo realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void registrarDevolucao() {
        System.out.println("\nREGISTRAR DEVOLUÇÃO");
        
        System.out.print("ID do Empréstimo: ");
        Long emprestimoId = Long.parseLong(scanner.nextLine());

        try {
            emprestimoService.devolverLivro(emprestimoId);
            System.out.println("Devolução registrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void renovarEmprestimo() {
        System.out.println("\nRENOVAR EMPRÉSTIMO");
        
        System.out.print("ID do Empréstimo: ");
        Long emprestimoId = Long.parseLong(scanner.nextLine());

        try {
            emprestimoService.renovarEmprestimo(emprestimoId);
            System.out.println("Empréstimo renovado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
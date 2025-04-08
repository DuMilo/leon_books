package br.com.leonbooks.leon_books.ui;

import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.service.LivroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {

    private final LivroService livroService;
    private final Scanner scanner;

    public ConsoleUI(LivroService livroService) {
        this.livroService = livroService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== LEON BOOKS - SISTEMA DE BIBLIOTECA ===");
        
        while (true) {
            exibirMenu();
            String input = scanner.nextLine(); 
            
            try {
                int opcao = Integer.parseInt(input);
                
                switch (opcao) {
                    case 1:
                        listarLivrosDisponiveis();
                        break;
                    case 2:
                        cadastrarLivro();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\nMENU PRINCIPAL");
        System.out.println("1. Listar livros disponíveis");
        System.out.println("2. Cadastrar novo livro");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void listarLivrosDisponiveis() {
        System.out.println("\nLIVROS DISPONÍVEIS:");
        livroService.buscarLivrosDisponiveis().forEach(livro -> {
            System.out.println("ID: " + livro.getId());
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor());
            System.out.println("ISBN: " + livro.getIsbn());
            System.out.println("-----------------------");
        });
    }

    private void cadastrarLivro() {
        System.out.println("\nCADASTRAR NOVO LIVRO");
        
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        Livro novoLivro = new Livro(null, titulo, autor, isbn);
        livroService.cadastraLivro(novoLivro);
        
        System.out.println("Livro cadastrado com sucesso!");
    }
}
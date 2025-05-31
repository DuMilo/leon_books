package br.com.leonbooks.leon_books.ui;

import br.com.leonbooks.leon_books.model.Cliente;
import br.com.leonbooks.leon_books.model.Emprestimo;
import br.com.leonbooks.leon_books.model.Livro;
import br.com.leonbooks.leon_books.model.Multa;
import br.com.leonbooks.leon_books.service.ClienteService;
import br.com.leonbooks.leon_books.service.EmprestimoService;
import br.com.leonbooks.leon_books.service.LivroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;

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
                    case 12 -> renovarEmprestimoUI();
                    case 13 -> menuListarEmprestimos();
                    case 14 -> menuMultas(); 
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
                // e.printStackTrace();
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
        System.out.println("13. Listar empréstimos (submenu)");
        System.out.println("14. Gerenciar Multas (submenu)");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void listarLivrosDisponiveis() {
        System.out.println("\nLIVROS DISPONÍVEIS:");
        List<Livro> livros = livroService.buscarLivrosDisponiveis();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro disponível no momento.");
            return;
        }
        livros.forEach(this::imprimirDetalhesLivroSimples);
    }

    private void listarTodosOsLivros() {
        System.out.println("\nTODOS OS LIVROS:");
        List<Livro> livros = livroService.buscarTodosLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }
        livros.forEach(this::imprimirDetalhesLivroCompleto);
    }

    private void buscarLivrosPorTitulo() {
        System.out.print("\nDigite parte do título: ");
        String titulo = scanner.nextLine();
        List<Livro> livros = livroService.buscarPorTitulo(titulo);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com este título.");
            return;
        }
        System.out.println("\nRESULTADOS DA BUSCA POR TÍTULO:");
        livros.forEach(this::imprimirDetalhesLivroCompleto);
    }

    private void buscarLivrosPorAutor() {
        System.out.print("\nDigite parte do nome do autor: ");
        String autor = scanner.nextLine();
        List<Livro> livros = livroService.buscarPorAutor(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado deste autor.");
            return;
        }
        System.out.println("\nRESULTADOS DA BUSCA POR AUTOR:");
        livros.forEach(this::imprimirDetalhesLivroCompleto);
    }

    private void imprimirDetalhesLivroSimples(Livro livro) {
        System.out.println("ID: " + livro.getId() + " | Título: " + livro.getTitulo() + " | Autor: " + livro.getAutor());
         System.out.println("--------------------------------------------------");
    }

    private void imprimirDetalhesLivroCompleto(Livro livro) {
        System.out.println("--------------------------------------------------");
        System.out.println("ID: " + livro.getId());
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor());
        System.out.println("ISBN: " + (livro.getIsbn() != null ? livro.getIsbn() : "N/A"));
        System.out.println("Ano: " + (livro.getAnoPublicacao() != null ? livro.getAnoPublicacao() : "N/A"));
        System.out.println("Disponível: " + (livro.isDisponivel() ? "Sim" : "Não"));

        if (!livro.isDisponivel()) {
            List<Emprestimo> emprestimos = emprestimoService.buscarPorLivro(livro.getId());
            emprestimos.stream()
                .filter(e -> !e.isDevolvido())
                .findFirst()
                .ifPresent(emprestimo -> {
                    System.out.println("  Status: Emprestado para Cliente ID " + emprestimo.getCliente().getId() +
                                       " (" + emprestimo.getCliente().getNome() + ")");
                    System.out.println("  Devolução Prevista: " + emprestimo.getDataDevolucaoPrevista());
                });
        }
        System.out.println("--------------------------------------------------");
    }

    private void listarClientes() {
        System.out.println("\nCLIENTES CADASTRADOS:");
        List<Cliente> clientes = clienteService.buscarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        clientes.forEach(this::imprimirDetalhesCliente);
    }

    private void buscarClientesPorNome() {
        System.out.print("\nDigite parte do nome do cliente: ");
        String nome = scanner.nextLine();
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado com este nome.");
            return;
        }
        System.out.println("\nRESULTADOS DA BUSCA POR NOME:");
        clientes.forEach(this::imprimirDetalhesCliente);
    }

    private void buscarClientesPorEmail() {
        System.out.print("\nDigite parte do e-mail do cliente: ");
        String email = scanner.nextLine();
         List<Cliente> clientes = clienteService.buscarPorEmail(email);
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado com este e-mail.");
            return;
        }
        System.out.println("\nRESULTADOS DA BUSCA POR E-MAIL:");
        clientes.forEach(this::imprimirDetalhesCliente);
    }

    private void imprimirDetalhesCliente(Cliente cliente) {
        System.out.println("--------------------------------------------------");
        System.out.println("ID: " + cliente.getId());
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("E-mail: " + cliente.getEmail());
        System.out.println("Telefone: " + (cliente.getTelefone() != null ? cliente.getTelefone() : "N/A"));
        System.out.println("Endereço: " + (cliente.getEndereco() != null ? cliente.getEndereco() : "N/A"));

        List<Emprestimo> emprestimos = emprestimoService.buscarPorCliente(cliente.getId());
        if (!emprestimos.isEmpty()) {
            System.out.println("  Histórico de Empréstimos:");
            emprestimos.forEach(e -> {
                System.out.println("    ID Empréstimo: " + e.getId() +
                                   " | Livro: " + e.getLivro().getTitulo() +
                                   " | Devolvido: " + (e.isDevolvido() ? "Sim (Em " + e.getDataDevolucaoReal() + ")" : "Não (Previsto: " + e.getDataDevolucaoPrevista() + ")") +
                                   " | Renovado: " + (e.isRenovado() ? "Sim" : "Não"));
            });
        }
        System.out.println("--------------------------------------------------");
    }

    private void menuListarEmprestimos() {
        System.out.println("\nLISTAR EMPRÉSTIMOS:");
        System.out.println("1. Listar todos os empréstimos");
        System.out.println("2. Listar empréstimos por ID do cliente");
        System.out.println("3. Listar empréstimos por ID do livro");
        System.out.println("4. Listar empréstimos atrasados");
        System.out.print("Escolha uma opção: ");
        String input = scanner.nextLine();
        try {
            int opcao = Integer.parseInt(input);
            switch (opcao) {
                case 1 -> listarTodosOsEmprestimos();
                case 2 -> listarEmprestimosPorCliente();
                case 3 -> listarEmprestimosPorLivro();
                case 4 -> listarEmprestimosAtrasadosNaUI();
                default -> System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    private void listarTodosOsEmprestimos() { 
        System.out.println("\nTODOS OS EMPRÉSTIMOS:");
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }
        emprestimos.forEach(this::imprimirDetalhesEmprestimo);
    }

    private void listarEmprestimosAtrasadosNaUI() {
        System.out.println("\nEMPRÉSTIMOS ATRASADOS:");
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAtrasados();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo atrasado encontrado.");
            return;
        }
        emprestimos.forEach(this::imprimirDetalhesEmprestimo);
    }

    private void listarEmprestimosPorCliente() {
        System.out.print("\nDigite o ID do cliente: ");
        try {
            Long clienteId = Long.parseLong(scanner.nextLine());
            List<Emprestimo> emprestimos = emprestimoService.buscarPorCliente(clienteId);
            if (emprestimos.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado para este cliente.");
                return;
            }
            System.out.println("\nEMPRÉSTIMOS DO CLIENTE ID " + clienteId + ":");
            emprestimos.forEach(this::imprimirDetalhesEmprestimo);
        } catch (NumberFormatException e) {
            System.out.println("ID do cliente inválido.");
        }
    }

    private void listarEmprestimosPorLivro() {
        System.out.print("\nDigite o ID do livro: ");
         try {
            Long livroId = Long.parseLong(scanner.nextLine());
            List<Emprestimo> emprestimos = emprestimoService.buscarPorLivro(livroId);
            if (emprestimos.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado para este livro.");
                return;
            }
            System.out.println("\nEMPRÉSTIMOS DO LIVRO ID " + livroId + ":");
            emprestimos.forEach(this::imprimirDetalhesEmprestimo);
        } catch (NumberFormatException e) {
            System.out.println("ID do livro inválido.");
        }
    }

    private void imprimirDetalhesEmprestimo(Emprestimo emprestimo) {
        System.out.println("--------------------------------------------------");
        System.out.println("ID Empréstimo: " + emprestimo.getId());
        System.out.println("  Livro: ID " + emprestimo.getLivro().getId() + " - " + emprestimo.getLivro().getTitulo());
        System.out.println("  Cliente: ID " + emprestimo.getCliente().getId() + " - " + emprestimo.getCliente().getNome());
        System.out.println("  Data Empréstimo: " + emprestimo.getDataEmprestimo());
        System.out.println("  Devolução Prevista: " + emprestimo.getDataDevolucaoPrevista());
        if (emprestimo.isDevolvido()) {
            System.out.println("  Data Devolução Real: " + emprestimo.getDataDevolucaoReal());
            System.out.println("  Status: Devolvido");
        } else {
             System.out.println("  Status: Emprestado" + (emprestimo.estaAtrasado() ? " (ATRASAADO!)" : ""));
        }
        System.out.println("  Renovado: " + (emprestimo.isRenovado() ? "Sim" : "Não"));
        System.out.println("--------------------------------------------------");
    }


    private void cadastrarLivro() {
        System.out.println("\nCADASTRAR NOVO LIVRO");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Ano de Publicação: ");
        Integer anoPublicacao = null;
        try {
            anoPublicacao = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ano de publicação inválido. Será salvo como nulo.");
        }

        Livro livro = new Livro(titulo, autor, isbn, anoPublicacao);
        try {
            Livro livroSalvo = livroService.cadastraLivro(livro);
            System.out.println("Livro cadastrado com sucesso! ID: " + livroSalvo.getId());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    private void cadastrarCliente() {
        System.out.println("\nCADASTRAR NOVO CLIENTE");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);
        cliente.setEndereco(endereco);

        try {
            Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
            System.out.println("Cliente cadastrado com sucesso! ID: " + clienteSalvo.getId());
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private void realizarEmprestimo() {
        System.out.println("\nREALIZAR NOVO EMPRÉSTIMO");
        System.out.print("Digite o ID do Livro: ");
        Long livroId;
        try {
            livroId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID do livro inválido.");
            return;
        }

        System.out.print("Digite o ID do Cliente: ");
        Long clienteId;
        try {
            clienteId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID do cliente inválido.");
            return;
        }

        try {
            Emprestimo novoEmprestimo = emprestimoService.realizarEmprestimo(livroId, clienteId);
            System.out.println("Empréstimo realizado com sucesso! ID do Empréstimo: " + novoEmprestimo.getId());
            imprimirDetalhesEmprestimo(novoEmprestimo);
        } catch (Exception e) {
            System.out.println("Falha ao realizar empréstimo: " + e.getMessage());
        }
    }

    private void registrarDevolucao() {
        System.out.println("\nREGISTRAR DEVOLUÇÃO DE LIVRO");
        System.out.print("Digite o ID do Empréstimo a ser devolvido: ");
        Long emprestimoId;
        try {
            emprestimoId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID do empréstimo inválido.");
            return;
        }

        try {
            Emprestimo emprestimoDevolvido = emprestimoService.devolverLivro(emprestimoId);
            System.out.println("Devolução registrada com sucesso para o Empréstimo ID: " + emprestimoDevolvido.getId());
            imprimirDetalhesEmprestimo(emprestimoDevolvido);
            List<Multa> multas = emprestimoService.buscarMultasPorCliente(emprestimoDevolvido.getCliente().getId());
            Optional<Multa> multaDoEmprestimo = multas.stream().filter(m -> m.getEmprestimo().getId().equals(emprestimoId) && !m.isPaga()).findFirst();
            if(multaDoEmprestimo.isPresent()){
                System.out.println("ATENÇÃO: Multa de R$" + multaDoEmprestimo.get().getValor() + " aplicada por atraso. ID da Multa: " + multaDoEmprestimo.get().getId());
            }

        } catch (Exception e) {
            System.out.println("Falha ao registrar devolução: " + e.getMessage());
        }
    }

    private void renovarEmprestimoUI() {
        System.out.println("\nRENOVAR EMPRÉSTIMO");
        System.out.print("Digite o ID do Empréstimo a ser renovado: ");
        Long emprestimoId;
        try {
            emprestimoId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID do empréstimo inválido.");
            return;
        }
        try {
            emprestimoService.renovarEmprestimo(emprestimoId);
            System.out.println("Empréstimo ID " + emprestimoId + " renovado com sucesso!");
             Optional<Emprestimo> emprestimoOpt = emprestimoService.buscarPorId(emprestimoId);
             emprestimoOpt.ifPresent(this::imprimirDetalhesEmprestimo);

        } catch (Exception e) {
            System.out.println("Falha ao renovar empréstimo: " + e.getMessage());
        }
    }

    private void menuMultas() {
        System.out.println("\nGERENCIAR MULTAS:");
        System.out.println("1. Listar multas pendentes de um cliente");
        System.out.println("2. Calcular total de multas pendentes de um cliente");
        System.out.println("3. Pagar multa");
        System.out.print("Escolha uma opção: ");
        String input = scanner.nextLine();
        try {
            int opcao = Integer.parseInt(input);
            switch (opcao) {
                case 1 -> listarMultasPendentesCliente();
                case 2 -> calcularTotalMultasPendentesCliente();
                case 3 -> pagarMultaNaUI();
                default -> System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    private void listarMultasPendentesCliente() {
        System.out.print("\nDigite o ID do Cliente: ");
        Long clienteId;
        try {
            clienteId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID do cliente inválido.");
            return;
        }
        List<Multa> multas = emprestimoService.buscarMultasPorCliente(clienteId);
        if (multas.isEmpty()) {
            System.out.println("Cliente não possui multas pendentes.");
            return;
        }
        System.out.println("\nMULTAS PENDENTES DO CLIENTE ID " + clienteId + ":");
        multas.forEach(multa -> {
            System.out.println("  ID Multa: " + multa.getId() +
                               " | Empréstimo ID: " + multa.getEmprestimo().getId() +
                               " | Livro: " + multa.getEmprestimo().getLivro().getTitulo() +
                               " | Valor: R$" + multa.getValor());
        });
    }

    private void calcularTotalMultasPendentesCliente() {
        System.out.print("\nDigite o ID do Cliente: ");
        Long clienteId;
        try {
            clienteId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID do cliente inválido.");
            return;
        }
        BigDecimal total = emprestimoService.calcularTotalMultasCliente(clienteId);
        System.out.println("Total de multas pendentes para o Cliente ID " + clienteId + ": R$" + total);
    }

    private void pagarMultaNaUI() {
        System.out.print("\nDigite o ID da Multa a ser paga: ");
        Long multaId;
        try {
            multaId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID da multa inválido.");
            return;
        }
        try {
            emprestimoService.pagarMulta(multaId);
            System.out.println("Multa ID " + multaId + " paga com sucesso!");
        } catch (Exception e) {
            System.out.println("Falha ao pagar multa: " + e.getMessage());
        }
    }
}

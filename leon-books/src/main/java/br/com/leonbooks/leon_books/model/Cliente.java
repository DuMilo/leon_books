package br.com.leonbooks.leon_books.model;

import jakarta.persistence.*;
//import java.util.List;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String endereco;

    public Cliente() {
    }

    public Cliente(String nome, String email, String telefone, String endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    // public List<Emprestimo> getEmprestimos() {
    //     return emprestimos;
    // }

    // public void setEmprestimos(List<Emprestimo> emprestimos) {
    //     this.emprestimos = emprestimos;
    // }

    // public void adicionarEmprestimo(Emprestimo emprestimo) {
    //     if (this.emprestimos == null) {
    //         this.emprestimos = new java.util.ArrayList<>();
    //     }
    //     this.emprestimos.add(emprestimo);
    //     emprestimo.setCliente(this);
    // }
}

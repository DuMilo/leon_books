package br.com.leonbooks.leon_books.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprestimo> emprestimosAtivos = new ArrayList<>();

    public Cliente() {}

    public Cliente(String nome, String email){
        this.nome = nome;
        this.email = email;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public List<Emprestimo> getEmprestimosAtivos(){
        return emprestimosAtivos;
    }

    public void adicionarEmprestimo(Emprestimo emprestimo) {
        this.emprestimosAtivos.add(emprestimo);
        emprestimo.setCliente(this);
    }

    public boolean temAtrasos() {
        return this.emprestimosAtivos.stream().anyMatch(e -> !e.isDevolvido() && e.getDataDevolucao().isBefore(java.time.LocalDate.now()));
    }
}

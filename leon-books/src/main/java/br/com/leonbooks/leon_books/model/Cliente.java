package br.com.leonbooks.leon_books.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private static int proximoId = 1;

    private int id;
    private String nome;
    private String email;
    private List<Emprestimo> emprestimosAtivos = new ArrayList<>();

    public Cliente(String nome, String email){
        this.id = proximoId++;
        this.nome = nome;
        this.email = email;
    }

    public int getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getEmail(){
        return email;
    }

    public void adicionarEmprestimo(Emprestimo emprestimo) {
        this.emprestimosAtivos.add(emprestimo);
    }

    public boolean temAtrasos() {
        return this.emprestimosAtivos.stream().anyMatch(e -> !e.isDevolvido() && e.getDataDevolucao().isBefore(java.time.LocalDate.now()));
    }
}

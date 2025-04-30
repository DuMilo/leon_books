package br.com.leonbooks.leon_books.model;

import java.time.LocalDate;

public class Emprestimo {
    private static int proximoId = 1;

    private int id;
    private Livro livro;
    private Cliente cliente;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean devolvido;
    private boolean renovado;

    public Emprestimo(Livro livro, Cliente cliente) {
        this.id = proximoId++;
        this.livro = livro;
        this.cliente = cliente;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = LocalDate.now().plusDays(14);
        this.devolvido = false;
        this.renovado = false;
    }

    public int getId(){
        return id;
    }
    public Livro getLivro(){
        return livro;
    }
    public Cliente getCliente(){
        return cliente;
    }
    public LocalDate getDataEmprestimo(){
        return dataEmprestimo;
    }
    public LocalDate getDataDevolucao(){
        return dataDevolucao;
    }
    public boolean isDevolvido(){
        return devolvido;
    }
    public void setDevolvido(boolean devolvido){
        this.devolvido = devolvido;
    }
    public boolean isRenovado(){
        return renovado;
    }
    public void renovarEmprestimo(){
        this.dataDevolucao = dataDevolucao.plusDays(14);
        this.renovado = true;
    }
}

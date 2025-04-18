package br.com.leonbooks.leon_books.model;

import java.time.LocalDate;

public class Emprestimo {
    private String id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean devolvido;
    private boolean renovado;

    public Emprestimo(String id, Livro livro, Usuario usuario) {
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = LocalDate.now().plusDays(14);
        this.devolvido = false;
        this.renovado = false;
    }

    public String getId(){
        return id;
    }
    public Livro getLivro(){
        return livro;
    }
    public Usuario getUsuario(){
        return usuario;
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

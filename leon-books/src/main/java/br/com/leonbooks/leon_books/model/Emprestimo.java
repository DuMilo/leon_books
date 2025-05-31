package br.com.leonbooks.leon_books.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Livro livro;

    @ManyToOne
    private Cliente cliente;

    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;

    private boolean devolvido;
    private boolean renovado;

    public Emprestimo() {}

    public Emprestimo(Livro livro, Cliente cliente) {
        this.livro = livro;
        this.cliente = cliente;
        this.dataEmprestimo = LocalDate.now();
        this.devolvido = false;
        this.renovado = false;
    }

    public Long getId() {
        return id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
        if (devolvido && this.dataDevolucaoReal == null) {
            this.dataDevolucaoReal = LocalDate.now();
        }
    }

    public boolean isRenovado() {
        return renovado;
    }

    public void setRenovado(boolean renovado) {
        this.renovado = renovado;
    }

    public void renovarEmprestimo() {
        if (!renovado && !devolvido) {
            this.dataDevolucaoPrevista = this.dataDevolucaoPrevista.plusDays(7);
            this.renovado = true;
        } else if (renovado) {
            throw new IllegalStateException("Empréstimo já foi renovado.");
        } else {
            throw new IllegalStateException("Empréstimo já foi devolvido e não pode ser renovado.");
        }
    }

    public boolean estaAtrasado() {
        return !devolvido && LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucaoReal != null ? dataDevolucaoReal : dataDevolucaoPrevista;
    }
}

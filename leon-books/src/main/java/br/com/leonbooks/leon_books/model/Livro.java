package br.com.leonbooks.leon_books.model;

public class Livro {
    private static int proximoId = 1;

    private Integer id;
    private String titulo;
    private String autor;
    private boolean disponivel;

    public Livro(String titulo, String autor){
        this.id = proximoId++;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
    }

    public int getId(){
        return id;
    }
    public String getTitulo(){
        return titulo;
    }
    public String getAutor(){
        return autor;
    }
    public boolean isDisponivel(){
        return disponivel;
    }
    public void setDisponivel(boolean disponivel){
        this.disponivel = disponivel;
    }

}

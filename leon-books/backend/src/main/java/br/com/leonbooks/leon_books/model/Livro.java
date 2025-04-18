package br.com.leonbooks.leon_books.model;

public class Livro {
    private Integer id;
    private String titulo;
    private String autor;
    private String isbn; 
    private boolean disponivel;

    public Livro(Integer id, String titulo, String autor, String isbn){
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.disponivel = true;
    }

    public Integer getId(){
        return id;
    }
    public String getTitulo(){
        return titulo;
    }
    public String getAutor(){
        return autor;
    }
    public String getIsbn() {
        return isbn;
    }
    public boolean isDisponivel(){
        return disponivel;
    }
    public void setDisponivel(boolean disponivel){
        this.disponivel = disponivel;
    }

}

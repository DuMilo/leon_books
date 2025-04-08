package br.com.leonbooks.leon_books.model;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private boolean funcionario;

    public Usuario(String id, String nome, String email, boolean funcionario){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.funcionario = funcionario;
    }

    public String getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getEmail(){
        return email;
    }
    public boolean isFuncionario(){
        return funcionario;
    }
}

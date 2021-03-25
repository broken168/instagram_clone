package com.cursoandroid.gabriel.instagramclone.model;

public class Comentario {

    private String idUsario, caminhoFoto, nome, comentario;

    public Comentario() {
    }

    public String getIdUsario() {
        return idUsario;
    }

    public void setIdUsario(String idUsario) {
        this.idUsario = idUsario;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

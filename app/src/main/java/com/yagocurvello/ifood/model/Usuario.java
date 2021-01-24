package com.yagocurvello.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.yagocurvello.ifood.config.ConfigFirebase;

import java.io.Serializable;


public class Usuario implements Serializable {

    private String name;
    private String email;
    private String senha;
    private String id;
    private String tipo;
    private String endereco;

    public Usuario() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void salvar(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase()
                .child("usuarios").child(getId());
        reference.setValue(this);

    }
}


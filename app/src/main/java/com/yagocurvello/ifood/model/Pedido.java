package com.yagocurvello.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.yagocurvello.ifood.config.ConfigFirebase;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    private String endereco, idEmpresa, idPedido, idUsuario, nome, status;
    private List<Produto> produtoList;

    public Pedido() {
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getidPedido() {
        return idPedido;
    }

    public void setidPedido(String ididPedido) {
        this.idPedido = idPedido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Produto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }

    public void salvar(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase().child("pedidos");
        idPedido = reference.push().getKey();
        DatabaseReference referencePedido = reference.child(idPedido).child(idEmpresa);
        reference.setValue(this);
    }
}

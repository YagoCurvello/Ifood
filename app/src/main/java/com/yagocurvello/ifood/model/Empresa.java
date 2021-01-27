package com.yagocurvello.ifood.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.yagocurvello.ifood.config.ConfigFirebase;

import java.util.HashMap;
import java.util.List;

public class Empresa {

    private String culinaria, tempo, id, caminhoFoto, nome, taxa;;
    private List <Produto> produtoList;

    public Empresa() {
    }

    public List<Produto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getCulinaria() {
        return culinaria;
    }

    public void setCulinaria(String culinaria) {
        this.culinaria = culinaria;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getTaxa() {
        return taxa;
    }

    public void setTaxa(String taxa) {
        this.taxa = taxa;
    }

    public void salvar(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase().child("empresas").child(getId());
        reference.setValue(this);
    }

    public void atualizar(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase().child("empresas").child(getId());

        HashMap map = new HashMap();
        map.put("nome", getNome());
        map.put("culinaria", getCulinaria());
        map.put("tempo", getTempo());
        map.put("taxa", getTaxa());
        map.put("caminhoFoto", getCaminhoFoto());

        reference.updateChildren(map);
    }
}

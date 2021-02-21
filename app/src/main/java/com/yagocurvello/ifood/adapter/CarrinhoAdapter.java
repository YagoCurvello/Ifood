package com.yagocurvello.ifood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.model.Produto;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder> {

    private List<Produto> produtoList;

    public CarrinhoAdapter(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_carrinho, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        Produto produto = produtoList.get(position);

        holder.nome.setText(produto.getNome());
        holder.valor.setText("R$ " + produto.getValor());
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textProdutoCarrinho);
            valor = itemView.findViewById(R.id.textValorProdutoCarrinho);
        }
    }
}

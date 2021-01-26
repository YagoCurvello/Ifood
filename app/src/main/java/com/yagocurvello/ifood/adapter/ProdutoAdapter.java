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

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> {

    private List<Produto> produtoList;
    private Context context;

    public ProdutoAdapter(List<Produto> produtoList,Context context) {
        this.produtoList = produtoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        Produto produto = produtoList.get(position);

        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());
        holder.valor.setText("R$ " + produto.getValor());
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, descricao, valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeProduto);
            descricao = itemView.findViewById(R.id.textDescricaoProduto);
            valor = itemView.findViewById(R.id.textPrecoProduto);
        }
    }
}

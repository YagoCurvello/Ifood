package com.yagocurvello.ifood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.model.Empresa;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.MyViewHolder> {

    private List<Empresa> empresaList;
    private Context context;

    public EmpresaAdapter(List<Empresa> empresaList,Context context) {
        this.empresaList = empresaList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_empresa, parent, false);
        return new EmpresaAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        Empresa empresa = empresaList.get(position);

        holder.nome.setText(empresa.getNome());
        holder.tempo.setText(empresa.getTempo());
        holder.taxa.setText("R$ " + empresa.getTaxa());
        holder.culinaria.setText(empresa.getCulinaria());

        if (empresa.getCaminhoFoto() != null){
            Picasso.get().load(empresa.getCaminhoFoto()).into(holder.foto);
        }else {
            holder.foto.setImageResource(R.drawable.perfil);
        }
    }

    @Override
    public int getItemCount() {
        return empresaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, tempo, taxa, culinaria;
        CircleImageView foto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNomeEmpresa);
            tempo = itemView.findViewById(R.id.textTempo);
            taxa = itemView.findViewById(R.id.textTaxa);
            culinaria = itemView.findViewById(R.id.textCulinaria);
            foto = itemView.findViewById(R.id.circleFotoEmpresaHome);
        }
    }
}

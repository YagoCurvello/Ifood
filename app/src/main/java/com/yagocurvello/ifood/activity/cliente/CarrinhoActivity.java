package com.yagocurvello.ifood.activity.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.adapter.ProdutoAdapter;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.model.Empresa;
import com.yagocurvello.ifood.model.Pedido;
import com.yagocurvello.ifood.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView recyclerCarrinho;
    private ProdutoAdapter adapter;
    private TextView nomeEmpresa, valorCarrinho;
    private Button confirmarPedido;

    private Pedido pedido;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            pedido = (Pedido) bundle.getSerializable("pedido");
            empresa = (Empresa) bundle.getSerializable("empresa");
        }else {
            Toast.makeText(this, "Erro ao carregar carrinho", Toast.LENGTH_SHORT).show();
            finish();
        }

        configIniciais();

        adapter = new ProdutoAdapter(empresa.getProdutoList(), this);
        recyclerCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarrinho.setHasFixedSize(true);
        recyclerCarrinho.setAdapter(adapter);

    }
    private void configIniciais(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Carrinho");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeEmpresa = findViewById(R.id.textEmpresaCarrinho);
        valorCarrinho = findViewById(R.id.textValorCarrinho);
        confirmarPedido = findViewById(R.id.buttonConfirmarPedido);

    }


}
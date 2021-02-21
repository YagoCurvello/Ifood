package com.yagocurvello.ifood.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.adapter.ProdutoAdapter;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.listener.RecyclerItemClickListener;
import com.yagocurvello.ifood.model.Empresa;
import com.yagocurvello.ifood.model.Pedido;
import com.yagocurvello.ifood.model.Produto;
import com.yagocurvello.ifood.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardapioActivity extends AppCompatActivity {

    private CircleImageView foto;
    private TextView nome, tempo, taxa, valorTotal, quantidade;
    private LinearLayout linearLayoutValores;
    private Button buttonCardapio;

    private RecyclerView recyclerCardapio;
    private ProdutoAdapter adapter;
    private Empresa empresa;
    private String idEmpresa;
    private List<Produto> produtoList;

    private Pedido pedido;
    private Usuario usuario;
    private double aPagar;
    private int quantidadeValor;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            idEmpresa = bundle.getString("idEmpresa");
        }else {
            Toast.makeText(this, "Erro ao carregar cardapio", Toast.LENGTH_SHORT).show();
            //finish();
        }

        configIniciais();

        recuperarUsuario();
        recuperarEmpresa();

        buttonCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pedido.getProdutoList().isEmpty()){
                    pedido.setEndereco(usuario.getEndereco());
                    pedido.setIdEmpresa(empresa.getId());
                    pedido.setIdUsuario(usuario.getId());
                    pedido.setNome(usuario.getName());
                    Intent i = new Intent(CardapioActivity.this, CarrinhoActivity.class);
                    i.putExtra("pedido", pedido);
                    i.putExtra("empresa", empresa);
                    i.putExtra("valor", aPagar);
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(), "Adicione um produto no carrinho", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void configIniciais(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cardapio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foto = findViewById(R.id.circleFotoEmpresaCardapio);
        nome = findViewById(R.id.textNomeEmpresaCardapio);
        tempo = findViewById(R.id.textTempoCardapio);
        taxa = findViewById(R.id.textTaxaCardapio);
        valorTotal = findViewById(R.id.textValorTotal);
        quantidade = findViewById(R.id.textQuantidade);
        buttonCardapio = findViewById(R.id.buttonCardapio);
        linearLayoutValores = findViewById(R.id.linearLayoutValor);
        recyclerCardapio = findViewById(R.id.recyclerCardapio);

        auth = ConfigFirebase.getFirebaseAutenticacao();
        reference = ConfigFirebase.getFirebaseDatabase().child("empresas")
                .child(idEmpresa);

        pedido = new Pedido();
        pedido.setProdutoList(new ArrayList<>());
        empresa = new Empresa();
        aPagar = 0;
        quantidadeValor = 0;
    }

    private void recuperarEmpresa(){

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);
                produtoList = empresa.getProdutoList();
                carregarDados();
                listarProdutos();
                Log.i("EmpresaNome", empresa.getNome());
                aPagar += Double.parseDouble(empresa.getTaxa());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarUsuario(){
        DatabaseReference usuarioRef = ConfigFirebase.getFirebaseDatabase().child("usuarios").child(auth.getUid());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void carregarDados(){
        nome.setText(empresa.getNome());
        tempo.setText(empresa.getTempo());
        taxa.setText("R$ " + empresa.getTaxa());
        if (empresa.getCaminhoFoto() == null || empresa.getCaminhoFoto().equals("")){
            foto.setImageResource(R.drawable.perfil);
        }else {
            Picasso.get().load(empresa.getCaminhoFoto()).into(foto);
        }
    }

    private void listarProdutos(){
        adapter = new ProdutoAdapter(produtoList, CardapioActivity.this);
        recyclerCardapio.setLayoutManager(new LinearLayoutManager(CardapioActivity.this));
        recyclerCardapio.setHasFixedSize(true);
        recyclerCardapio.setAdapter(adapter);
        recyclerCardapio.addItemDecoration(new DividerItemDecoration(CardapioActivity.this, LinearLayout.VERTICAL));

        recyclerCardapio.addOnItemTouchListener(new RecyclerItemClickListener(
                CardapioActivity.this,recyclerCardapio,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view,int position) {

                        Produto produto = produtoList.get(position);
                        double valor = produto.getValor();
                        aPagar += valor;
                        quantidadeValor ++;

                        pedido.getProdutoList().add(produto);

                        quantidade.setText(String.valueOf(quantidadeValor));
                        valorTotal.setText(String.format("%.2f", aPagar));
                        linearLayoutValores.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLongItemClick(View view,int position) {
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,View view,int i,long l) {

                    }
                }));
    }


}
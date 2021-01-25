package com.yagocurvello.ifood.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.model.Empresa;
import com.yagocurvello.ifood.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class NovoProdutoActivity extends AppCompatActivity {

    private EditText editNome, editDescricao, editPreco;
    private Button buttonSalvar;

    private Produto produto;
    private List<Produto> produtoList;
    private String nome, descricao, preco;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        configIniciais();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome = editNome.getText().toString();
                descricao = editDescricao.getText().toString();
                preco = editPreco.getText().toString();

                if (verificaTexto()){
                    produto.setNome(editNome.getText().toString());
                    produto.setDescricao(editDescricao.getText().toString());
                    produto.setValor(editPreco.getText().toString());

                    salvarProduto();
                }
            }
        });
    }

    private void configIniciais(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNome = findViewById(R.id.editNomeProduto);
        editDescricao = findViewById(R.id.editDescricaoProduto);
        editPreco = findViewById(R.id.editValorProduto);
        buttonSalvar = findViewById(R.id.buttonSalvarProduto);

        auth = ConfigFirebase.getFirebaseAutenticacao();
        reference = ConfigFirebase.getFirebaseDatabase().child("empresas").child(auth.getUid());

        produto = new Produto();
    }

    private boolean verificaTexto() {
        if (!nome.isEmpty() && !nome.equals("")) {
            if (!descricao.isEmpty() && !descricao.equals("")) {
                if (!preco.isEmpty() && !preco.equals("")) {
                    return true;
                } else {
                    Toast.makeText(NovoProdutoActivity.this,"Preencher campo Preço",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(NovoProdutoActivity.this,"Preencher campo Descrição",Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(NovoProdutoActivity.this,"Preencher campo Nome",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void salvarProduto(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);
                if (empresa.getProdutoList() == null){
                    produtoList = new ArrayList<>();
                    produtoList.add(produto);
                    empresa.setProdutoList(produtoList);
                }else {
                    empresa.getProdutoList().add(produto);
                }
                reference.setValue(empresa);
                Toast.makeText(getApplicationContext(), "Produto salvo", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
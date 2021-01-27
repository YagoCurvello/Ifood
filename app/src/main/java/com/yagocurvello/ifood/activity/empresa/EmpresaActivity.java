package com.yagocurvello.ifood.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.AuthActivity;
import com.yagocurvello.ifood.adapter.ProdutoAdapter;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.helper.Permissao;
import com.yagocurvello.ifood.listener.RecyclerItemClickListener;
import com.yagocurvello.ifood.model.Empresa;
import com.yagocurvello.ifood.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmpresa;
    private ProdutoAdapter adapter;
    private Empresa empresa;
    private List<Produto> produtoList;
    private ValueEventListener eventListener;

    private FirebaseAuth auth;
    private DatabaseReference referenceProdutos;
    private DatabaseReference referenceEmpresa;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        Permissao.ValidarPermissoes(permissoesNecessarias, EmpresaActivity.this, 1);

        configIniciais();

        adapter = new ProdutoAdapter(produtoList, EmpresaActivity.this);
        recyclerViewEmpresa.setLayoutManager(new LinearLayoutManager(EmpresaActivity.this));
        recyclerViewEmpresa.setHasFixedSize(true);
        recyclerViewEmpresa.setAdapter(adapter);
        recyclerViewEmpresa.addItemDecoration(new DividerItemDecoration(EmpresaActivity.this, LinearLayout.VERTICAL));

        recyclerViewEmpresa.addOnItemTouchListener(new RecyclerItemClickListener(
                EmpresaActivity.this, recyclerViewEmpresa,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {

            }

            @Override
            public void onLongItemClick(View view,int position) {
                openAlert(position);
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView,View view,int i,long l) {

            }
        }));
    }

    private void configIniciais(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - Empresa");
        setSupportActionBar(toolbar);

        recyclerViewEmpresa = findViewById(R.id.recycleEmpresa);

        produtoList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empresa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                startActivity(new Intent(EmpresaActivity.this, AuthActivity.class));
                finish();
                break;

            case R.id.menuConfig:
                startActivity(new Intent(EmpresaActivity.this, ConfigEmpresaActivity.class));
                break;

            case R.id.menuAdd:
                startActivity(new Intent(EmpresaActivity.this, NovoProdutoActivity.class));

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void recuperarProdutos(){
        produtoList.clear();
        eventListener = referenceProdutos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    produtoList.add(dataSnapshot.getValue(Produto.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = ConfigFirebase.getFirebaseAutenticacao();
        referenceProdutos = ConfigFirebase.getFirebaseDatabase()
                .child("empresas").child(auth.getUid()).child("produtoList");
        recuperarProdutos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceProdutos.removeEventListener(eventListener);
    }

    private void openAlert(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(EmpresaActivity.this);
        builder.setTitle("Apagar " + produtoList.get(position).getNome() + "?");
        builder.setMessage("Tem certeza que deseja apagar esse produto da sua lista? (Ao selecionar SIM, " +
                "os clientes não terão mais acesso ao Produto excluido)");
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,int i) {
                excluirProduto(position);
            }
        });
        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void excluirProduto(int position){
        referenceEmpresa = ConfigFirebase.getFirebaseDatabase().child("empresas").child(auth.getUid());
        referenceEmpresa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);
                empresa.getProdutoList().remove(position);
                referenceEmpresa.setValue(empresa);
                produtoList.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
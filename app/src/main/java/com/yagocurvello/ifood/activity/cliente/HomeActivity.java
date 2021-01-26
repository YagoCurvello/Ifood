package com.yagocurvello.ifood.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.AuthActivity;
import com.yagocurvello.ifood.activity.empresa.EmpresaActivity;
import com.yagocurvello.ifood.adapter.EmpresaAdapter;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.model.Empresa;
import com.yagocurvello.ifood.model.Produto;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerEmpresa;
    private EmpresaAdapter adapter;
    private MaterialSearchView searchView;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private List<Empresa> empresaList;
    private ValueEventListener eventListener;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        configIniciais();

        adapter = new EmpresaAdapter(empresaList, HomeActivity.this);
        recyclerEmpresa.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recyclerEmpresa.setHasFixedSize(true);
        recyclerEmpresa.setAdapter(adapter);
        recyclerEmpresa.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayout.VERTICAL));

        /*
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
         */
    }

    private void configIniciais(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood");
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        recyclerEmpresa = findViewById(R.id.recyclerEmpresa);

        empresaList = new ArrayList<>();
    }

    private void recuperarEmpresas(){
        empresaList.clear();
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    empresa = dataSnapshot.getValue(Empresa.class);
                    empresaList.add(empresa);

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
        reference = ConfigFirebase.getFirebaseDatabase().child("empresas");
        recuperarEmpresas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(eventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                startActivity(new Intent(HomeActivity.this, AuthActivity.class));
                finish();
                break;

            case R.id.action_search:

                break;

            case R.id.menuConfig:
                startActivity(new Intent(HomeActivity.this, ConfigUsuarioActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
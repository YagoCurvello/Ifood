package com.yagocurvello.ifood.activity.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.AuthActivity;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.helper.Permissao;

public class EmpresaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmpresa;

    private FirebaseAuth auth;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - Empresa");
        setSupportActionBar(toolbar);

        Permissao.ValidarPermissoes(permissoesNecessarias, EmpresaActivity.this, 1);

        configIniciais();

    }

    private void configIniciais(){
        recyclerViewEmpresa = findViewById(R.id.recycleEmpresa);

        auth = ConfigFirebase.getFirebaseAutenticacao();
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

}
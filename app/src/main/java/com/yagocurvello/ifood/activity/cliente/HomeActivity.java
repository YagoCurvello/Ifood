package com.yagocurvello.ifood.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.AuthActivity;
import com.yagocurvello.ifood.config.ConfigFirebase;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerEmpresa;
    private MaterialSearchView searchView;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        configIniciais();

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

    }

    private void configIniciais(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood");
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        recyclerEmpresa = findViewById(R.id.recyclerEmpresa);

        auth = ConfigFirebase.getFirebaseAutenticacao();
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
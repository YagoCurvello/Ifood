package com.yagocurvello.ifood.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.empresa.ConfigEmpresaActivity;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.model.Empresa;
import com.yagocurvello.ifood.model.Usuario;

public class ConfigUsuarioActivity extends AppCompatActivity {

    //Interface
    private EditText campoNome, campoEndereco;
    private Button buttonSalvar;

    private String nome, endereco;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_usuario);

        configIniciais();

        recuperarDados();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome = campoNome.getText().toString();
                endereco = campoEndereco.getText().toString();
                if (verificaTexto()){
                    usuario.setName(nome);
                    usuario.setEndereco(endereco);
                    usuario.setId(auth.getUid());
                    usuario.atualizar();
                    finish();
                }
            }
        });
    }

    private void configIniciais(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        campoNome = findViewById(R.id.editNomeUsuario);
        campoEndereco = findViewById(R.id.editEnderecoUsuario);
        buttonSalvar = findViewById(R.id.buttonAtualizarUsuario);

        auth = ConfigFirebase.getFirebaseAutenticacao();
        reference = ConfigFirebase.getFirebaseDatabase().child("usuarios").child(auth.getUid());

        usuario = new Usuario();
    }

    private boolean verificaTexto() {
        if (!nome.isEmpty() && !nome.equals("")) {
            if (!endereco.isEmpty() && !endereco.equals("")) {
                return true;
            } else {
                Toast.makeText(ConfigUsuarioActivity.this,"Preencher campo Endereço",Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ConfigUsuarioActivity.this,"Preencher campo Nome",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void recuperarDados(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                if (usuario != null){
                    campoNome.setText(usuario.getName());
                    campoEndereco.setText(usuario.getEndereco());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
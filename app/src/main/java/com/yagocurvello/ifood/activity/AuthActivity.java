package com.yagocurvello.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.config.ConfigFirebase;

public class AuthActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Switch switchAuth;
    private Button buttonEntrar;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();

        inicializarComponentes();

    }

    private void inicializarComponentes(){
        //Interface
        editEmail = findViewById(R.id.editEmailAuth);
        editSenha = findViewById(R.id.editSenhaAuth);
        switchAuth = findViewById(R.id.switchAuth);
        buttonEntrar = findViewById(R.id.buttonEntrarAuth);

        //Firebase Configs
        auth = ConfigFirebase.getFirebaseAutenticacao();
        reference = ConfigFirebase.getFirebaseDatabase().child("usuarios");
    }

    public void entrar(View view){
        email = editEmail.getText().toString();
        senha = editSenha.getText().toString();
        if (verificaTexto()){
            if (switchAuth.isChecked()){
                cadastrar();
            }else {
                logar();
            }
        }
    }

    private void logar(){
        auth.signInWithEmailAndPassword(email, senha).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        });
    }

    private void cadastrar(){
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                }
            }
        });
    }

    private boolean verificaTexto(){

        if (!email.isEmpty() && !email.equals("")){
            if (!senha.isEmpty() && !senha.equals("")){
                return true;
            }else {
                Toast.makeText(AuthActivity.this, "Preencher campo Login", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(AuthActivity.this, "Preencher campo Login", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
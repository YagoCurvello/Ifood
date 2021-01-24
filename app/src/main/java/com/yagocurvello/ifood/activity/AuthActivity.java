package com.yagocurvello.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.cliente.HomeActivity;
import com.yagocurvello.ifood.activity.empresa.EmpresaActivity;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.model.Usuario;

import java.util.EventListener;

public class AuthActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Switch switchAuth;
    private Switch switchUser;
    private Button buttonEntrar;
    private LinearLayout linearLayoutUser;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private EventListener eventListener;

    private String email, senha;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        inicializarComponentes();

        verificaUsuarioLogado();

        switchAuth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,boolean b) {
                if (b){
                    linearLayoutUser.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutUser.setVisibility(View.GONE);
                }
            }
        });

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

    private void inicializarComponentes(){
        //Interface
        editEmail = findViewById(R.id.editEmailAuth);
        editSenha = findViewById(R.id.editSenhaAuth);
        switchAuth = findViewById(R.id.switchAuth);
        switchUser = findViewById(R.id.switchEmpresa);
        buttonEntrar = findViewById(R.id.buttonEntrarAuth);
        linearLayoutUser = findViewById(R.id.linearLayoutUser);

        //Firebase Configs
        auth = ConfigFirebase.getFirebaseAutenticacao();
        reference = ConfigFirebase.getFirebaseDatabase().child("usuarios");

        usuario = new Usuario();
    }

    private void logar(){
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    verificaTipoUsuario();

                }else{
                    String erro = "";
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        erro = "Erro: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(AuthActivity.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cadastrar(){

        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Usuario usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setId(auth.getUid());
                    if (switchUser.isChecked()){
                        usuario.setTipo("E");
                        startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));
                        Toast.makeText(getApplicationContext(),
                                "Contiue o cadastro da sua empresa", Toast.LENGTH_SHORT).show();
                    }else {
                        usuario.setTipo("U");
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Toast.makeText(getApplicationContext(),
                                "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    usuario.salvar();
                    finish();
                }else {
                    String erro = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Senha muito fraca";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email inválido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Email já cadastrado";
                    } catch (Exception e) {
                        erro = "Erro: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(AuthActivity.this, erro, Toast.LENGTH_SHORT).show();
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

    private void verificaUsuarioLogado(){
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null){
            verificaTipoUsuario();
        }

    }

    private void verificaTipoUsuario(){
        DatabaseReference reference = ConfigFirebase.getFirebaseDatabase()
                .child("usuarios").child(auth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                Log.i("tipoUsuario", usuario.getTipo());
                if (usuario.getTipo().equals("E")){
                    startActivity(new Intent(AuthActivity.this, EmpresaActivity.class));
                }else {
                    startActivity(new Intent(AuthActivity.this, HomeActivity.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.yagocurvello.ifood.activity.empresa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yagocurvello.ifood.R;
import com.yagocurvello.ifood.activity.AuthActivity;
import com.yagocurvello.ifood.config.ConfigFirebase;
import com.yagocurvello.ifood.helper.Permissao;
import com.yagocurvello.ifood.model.Empresa;

import java.io.ByteArrayOutputStream;

public class ConfigEmpresaActivity extends AppCompatActivity {

    //Interface
    private EditText campoNome, campoCulinaria, campoTempo, campoTaxa;
    private ImageView foto;
    private Button buttonSalvar;

    private String nome, culinaria, tempo, taxa;

    private FirebaseAuth auth;
    private StorageReference storage;
    private DatabaseReference reference;
    private Empresa empresa;

    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_empresa);

        configIniciais();

        recuperaInfo();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i ,SELECAO_GALERIA);
                }
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome = campoNome.getText().toString();
                culinaria = campoCulinaria.getText().toString();
                tempo = campoTempo.getText().toString();
                taxa = campoTaxa.getText().toString();
                if (verificaTexto()){
                    salvarEmpresa();
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

        //Interface
        campoNome = findViewById(R.id.editNomeEmpresa);
        campoCulinaria = findViewById(R.id.editTipoCulinaria);
        campoTempo = findViewById(R.id.editTempoEspera);
        campoTaxa = findViewById(R.id.editTaxaEntrega);
        foto = findViewById(R.id.circleFotoEmpresa);
        buttonSalvar = findViewById(R.id.buttonSalvarEmpresa);

        auth = ConfigFirebase.getFirebaseAutenticacao();
        storage = ConfigFirebase.getFirebaseStorage().child("fotos").child("empresas");
        reference = ConfigFirebase.getFirebaseDatabase().child("empresas").child(auth.getUid());

    }

    private boolean verificaTexto() {
        if (!nome.isEmpty() && !nome.equals("")) {
            if (!culinaria.isEmpty() && !culinaria.equals("")) {
                if (!tempo.isEmpty() && !tempo.equals("")) {
                    if (!taxa.isEmpty() && !taxa.equals("")) {
                        return true;
                    } else {
                        Toast.makeText(ConfigEmpresaActivity.this,"Preencher campo Taxa",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ConfigEmpresaActivity.this,"Preencher campo Tempo",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ConfigEmpresaActivity.this,"Preencher campo Culinaria",Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ConfigEmpresaActivity.this,"Preencher campo Nome",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void salvarEmpresa(){
        empresa.setId(auth.getUid());
        empresa.setNome(nome);
        empresa.setCulinaria(culinaria);
        empresa.setTempo(tempo);
        empresa.setTaxa(taxa);
        if (empresa.getCaminhoFoto() == null){
            empresa.setCaminhoFoto("");
        }

        empresa.atualizar();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        empresa = new Empresa();

        if (resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {
                switch (requestCode){
                    //Caso a imagem venha da galeria
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();

                        //Configura imagem na tela
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                        //Recupera os dados para salvar no firebase
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                        byte[] dadosImagem = baos.toByteArray();

                        //Salvar imagem para o firebase
                        final StorageReference imagemRef = storage.child(auth.getUid() + ".jpeg");
                        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ConfigEmpresaActivity.this,
                                        "Falha ao salvar Imagem", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Recuperar Local da foto para salvar no usuario
                                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri url = task.getResult();

                                        //Salvar caminho da foto
                                        empresa.setCaminhoFoto(url.toString());

                                    }
                                });

                                Toast.makeText(ConfigEmpresaActivity.this,
                                        "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }

                //Se foi escolhida uma imagem
                if (imagem != null){
                    foto.setImageBitmap(imagem);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void recuperaInfo(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                empresa = snapshot.getValue(Empresa.class);
                if (empresa != null){
                    campoNome.setText(empresa.getNome());
                    campoCulinaria.setText(empresa.getCulinaria());
                    campoTaxa.setText(empresa.getTaxa());
                    campoTempo.setText(empresa.getTempo());

                    if (!empresa.getCaminhoFoto().equals("")){
                        Picasso.get().load(empresa.getCaminhoFoto()).into(foto);
                    }else {
                        foto.setImageDrawable(getResources().getDrawable(R.drawable.perfil));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
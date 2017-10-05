package com.poturno.poturnochat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.poturno.poturnochat.R;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.helper.Preferences;
import com.poturno.poturnochat.model.User;

public class AccountActivity extends AppCompatActivity {

    private TextView name;
    private TextView emai;
    private Button changeName;
    private Button changePassword;
    private Button deleteAccount;
    private FirebaseAuth firebaseAuth;
    private String userName;
    private String userEmail;
    private String userId;


    @Override
    protected void onResume() {
        super.onResume();

        final Preferences preferences = new Preferences(AccountActivity.this);

        userName = preferences.getName();
        userEmail = preferences.getEmail();

        name.setText(userName);
        emai.setText(userEmail);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        name = (TextView)findViewById(R.id.txt_name);
        emai = (TextView)findViewById(R.id.txt_email);
        changeName = (Button)findViewById(R.id.btn_change_name);
        changePassword = (Button)findViewById(R.id.btn_change_password);
        deleteAccount = (Button)findViewById(R.id.btn_delete_account);

        final Preferences preferences = new Preferences(AccountActivity.this);
        userId = preferences.getIdentifier();



        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AccountActivity.this, R.style.AlertDialogAddContact);
                aleBuilder.setTitle("Novo nome");
                aleBuilder.setMessage("Digite o novo nome");
                aleBuilder.setCancelable(false);

                final EditText newName = new EditText(AccountActivity.this);
                aleBuilder.setView(newName);

                aleBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newNameValue = newName.getText().toString();
                        if(newNameValue.isEmpty()){
                            Toast.makeText(AccountActivity.this,"Erro: nome vazio",Toast.LENGTH_LONG);
                        }else {
                            User user = new User();
                            user.setName(newNameValue);
                            user.setEmail(userEmail);
                            user.setId(userId);
                            user.save();
                            preferences.saveData(user.getId(),user.getName(),user.getEmail());
                            restart();
                        }
                    }
                });

                aleBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aleBuilder.create();
                aleBuilder.show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AccountActivity.this, R.style.AlertDialogAddContact);
                aleBuilder.setTitle("Digite a senha atual");
                aleBuilder.setCancelable(false);

                final EditText password = new EditText(AccountActivity.this);
                password.setTransformationMethod(new PasswordTransformationMethod());
                aleBuilder.setView(password);

                aleBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String passwordValue = password.getText().toString();
                        if(passwordValue.isEmpty()){
                            Toast.makeText(AccountActivity.this,"Erro: senha vazio",Toast.LENGTH_LONG);
                        }else {
                            AuthCredential credential = EmailAuthProvider.getCredential(userEmail,passwordValue);
                            final FirebaseUser firebaseUser = FirebaseConfig.getFirebaseAuth().getCurrentUser();
                            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        AlertDialog.Builder aleBuilderNewPass = new AlertDialog.Builder(AccountActivity.this, R.style.AlertDialogAddContact);
                                        aleBuilderNewPass.setTitle("Digite a nova senha");
                                        aleBuilderNewPass.setCancelable(false);

                                        final EditText newPassword = new EditText(AccountActivity.this);
                                        newPassword.setTransformationMethod(new PasswordTransformationMethod());
                                        aleBuilderNewPass.setView(newPassword);

                                        aleBuilderNewPass.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String newPasswordValue = newPassword.getText().toString();
                                                if(newPasswordValue.isEmpty()){
                                                    Toast.makeText(AccountActivity.this,"Erro: senha vazio",Toast.LENGTH_LONG).show();
                                                }else {
                                                    FirebaseUser user = FirebaseConfig.getFirebaseAuth().getCurrentUser();
                                                    user.updatePassword(newPasswordValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(AccountActivity.this,"Senha alterado com sucesso",Toast.LENGTH_LONG).show();
                                                            }else{
                                                                Toast.makeText(AccountActivity.this,"Erro ao alterar senha",Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                        aleBuilderNewPass.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                        aleBuilderNewPass.create();
                                        aleBuilderNewPass.show();

                                    }else{
                                        Toast.makeText(AccountActivity.this,"Erro senha errada",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    }
                });

                aleBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aleBuilder.create();
                aleBuilder.show();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(AccountActivity.this, R.style.AlertDialogAddContact);
                aleBuilder.setTitle("Digite a senha atual");
                aleBuilder.setCancelable(false);

                final EditText password = new EditText(AccountActivity.this);
                password.setTransformationMethod(new PasswordTransformationMethod());
                aleBuilder.setView(password);

                aleBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String passWordValue = password.getText().toString();
                        if(passWordValue.isEmpty()){
                            Toast.makeText(AccountActivity.this,"Erro: senha vazio",Toast.LENGTH_LONG);
                        }else {

                            AuthCredential credential = EmailAuthProvider.getCredential(userEmail, passWordValue);
                            final FirebaseUser firebaseUser = FirebaseConfig.getFirebaseAuth().getCurrentUser();
                            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(AccountActivity.this,"Usuario excluido",Toast.LENGTH_LONG).show();
                                                    openLogin();
                                                }else{
                                                    Toast.makeText(AccountActivity.this,"Erro ao excluir usuario",Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                                    }else{
                                        Toast.makeText(AccountActivity.this,"Erro ao excluir usuario",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    }
                });

                aleBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aleBuilder.create();
                aleBuilder.show();



            }
        });
    }

    private void  restart(){
        Intent intent = new Intent(AccountActivity.this,AccountActivity.class);
        startActivity(intent);
        finish();
    }
    private void openLogin(){
        Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

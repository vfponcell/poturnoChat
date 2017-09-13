package com.poturno.poturnochat.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.poturno.poturnochat.R;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.model.User;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password0;
    private EditText password1;
    private Button signup;
    private User user;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.edit_signup_name);
        email = (EditText) findViewById(R.id.edit_signup_email);
        password0 = (EditText) findViewById(R.id.edit_signup_password0);
        password1 = (EditText) findViewById(R.id.edit_signup_password1);
        signup = (Button) findViewById(R.id.btn_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User();
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password0.getText().toString());
                addUser();
            }
        });
    }

    private void addUser(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user.setId(task.getResult().getUser().getUid());
                    user.save();
                    Toast.makeText(SignupActivity.this,"Usuario cadastrado com sucesso!",Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    finish();
                }else{

                    String error = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Senha fraca!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Email invalido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = "Email ja em uso!";
                    } catch (Exception e) {
                        error = "Ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(SignupActivity.this,"Erro: "+error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

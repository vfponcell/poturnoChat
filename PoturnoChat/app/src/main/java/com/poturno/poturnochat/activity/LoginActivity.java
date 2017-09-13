package com.poturno.poturnochat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.poturno.poturnochat.R;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.model.User;

public class LoginActivity extends AppCompatActivity {

    private TextView signup;
    private EditText email;
    private EditText password;
    private Button login;
    private User user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isUserLoged();

        signup = (TextView) findViewById(R.id.txt_signup);
        email = (EditText) findViewById(R.id.edit_login_email);
        password = (EditText) findViewById(R.id.edit_login_password);
        login = (Button) findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                loginValidation();

            }
        });
    }

    private void isUserLoged(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser()!=null){
            openMain();
        }
    }

    private void loginValidation(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    openMain();
                    Toast.makeText(LoginActivity.this,"Sucesso",Toast.LENGTH_LONG).show();
                }else{
                    String error = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                       error = "Usuario ou senha invalidos";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Usuario ou senha invalidos";
                    } catch (Exception e) {
                        error = "Ao fazer login";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,"Erro: "+error,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void openMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSignup(View view){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}

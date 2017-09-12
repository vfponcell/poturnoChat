package com.poturno.poturnochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password0;
    private EditText password1;
    private Button signup;

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

            }
        });
    }
}

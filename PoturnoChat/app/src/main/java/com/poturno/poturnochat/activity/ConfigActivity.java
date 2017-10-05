package com.poturno.poturnochat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.poturno.poturnochat.R;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.helper.Preferences;

public class ConfigActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private Button account;

    @Override
    protected void onResume() {
        super.onResume();

        Preferences preferences = new Preferences(ConfigActivity.this);
        name.setText(preferences.getName());
        email.setText(preferences.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        name = (TextView)findViewById(R.id.txt_name);
        email = (TextView)findViewById(R.id.txt_email);
        account = (Button)findViewById(R.id.btn_conta);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccount();
            }
        });
    }

    private void openAccount(){
        Intent intent = new Intent(ConfigActivity.this,AccountActivity.class);
        startActivity(intent);
    }
}

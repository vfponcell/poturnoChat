package com.poturno.poturnochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.poturno.poturnochat.R;
import com.poturno.poturnochat.config.Constants;
import com.poturno.poturnochat.helper.Preferences;
import com.squareup.picasso.Picasso;

public class ConfigActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private Button account;
    private ImageView mProfilePicture;
    private Button info;

    private Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mPreferences = new Preferences(this);

        name = (TextView) findViewById(R.id.txt_name);
        email = (TextView) findViewById(R.id.txt_email);
        account = (Button) findViewById(R.id.btn_conta);
        info = (Button)findViewById(R.id.btn_info);
        mProfilePicture = (ImageView) findViewById(R.id.profile_picture_iv);

        Picasso.with(this).load(Constants.Api.GET_PICTURE + mPreferences.getIdentifier() + ".png")
                .into(mProfilePicture);


       account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccount();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Preferences preferences = new Preferences(ConfigActivity.this);
        name.setText(preferences.getName());
        email.setText(preferences.getEmail());
    }

    private void openAccount() {
        Intent intent = new Intent(ConfigActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private void openInfo(){
        Intent intent = new Intent(ConfigActivity.this,InfoActivity.class);
        startActivity(intent);
    }
}

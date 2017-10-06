package com.poturno.poturnochat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.poturno.poturnochat.R;

public class InfoActivity extends AppCompatActivity {

    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        version = (TextView) findViewById(R.id.txt_version);

        version.setText("v1.0");
    }
}

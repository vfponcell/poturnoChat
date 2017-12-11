package com.poturno.poturnochat.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.poturno.poturnochat.R;
import com.poturno.poturnochat.adapter.MensageAdapter;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.helper.Base64Custom;
import com.poturno.poturnochat.helper.Preferences;
import com.poturno.poturnochat.model.Chat;
import com.poturno.poturnochat.model.Mensage;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensage;
    private ImageButton btnSend;
    private ImageButton btnLocation;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayList<Mensage> mensages;
    private ArrayAdapter<Mensage> adapter;
    private ValueEventListener valueEventListenerMensage;

    private String destinationContactName;
    private String destinationContactId;

    private String senderContactId;
    private String senderContactName;

    private static final int LOCATION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.tbr_chat);
        editMensage = (EditText) findViewById(R.id.edit_mensage);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnLocation = (ImageButton) findViewById(R.id.btn_location);
        listView = (ListView) findViewById(R.id.lv_chat);

        Preferences preferences = new Preferences(ChatActivity.this);
        senderContactId = preferences.getIdentifier();
        senderContactName = preferences.getName();


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            destinationContactName = bundle.getString("name");
            String destinationContactEmail = bundle.getString("email");
            destinationContactId = Base64Custom.encodeBase64(destinationContactEmail);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(destinationContactId.replaceAll("[^0-9]", "")));
        }


        toolbar.setTitle(destinationContactName);
        setSupportActionBar(toolbar);

        mensages = new ArrayList<>();
        adapter = new MensageAdapter(ChatActivity.this, mensages);
        listView.setAdapter(adapter);


        databaseReference = FirebaseConfig.getDatabaseReference()
                .child("mensages")
                .child(senderContactId)
                .child(destinationContactId);


        valueEventListenerMensage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensages.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Mensage mensage = data.getValue(Mensage.class);
                    mensages.add(mensage);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListenerMensage);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensageValue = editMensage.getText().toString();

                if (!(mensageValue.isEmpty())) {
                    Mensage mensage = new Mensage();
                    mensage.setUserId(senderContactId);
                    mensage.setMensage(mensageValue);

                    Boolean senderMensageSaved = saveMensage(senderContactId, destinationContactId, mensage);
                    if (!senderMensageSaved) {
                        Toast.makeText(ChatActivity.this, "Problema ao salvar mensagem", Toast.LENGTH_LONG).show();
                    } else {
                        Boolean destinatioMensageSaved = saveMensage(destinationContactId, senderContactId, mensage);
                        if (!destinatioMensageSaved) {
                            Toast.makeText(ChatActivity.this, "Problema ao enviar mensagem", Toast.LENGTH_LONG).show();
                        }
                    }

                    Chat chat = new Chat();
                    chat.setSenderId(senderContactId);
                    chat.setUserId(destinationContactId);
                    chat.setName(destinationContactName);
                    chat.setMensageValue(mensageValue);

                    Boolean senderChatSaved = saveChat(senderContactId, destinationContactId, chat);
                    if (!senderChatSaved) {
                        Toast.makeText(ChatActivity.this, "Problema ao salvar conversa", Toast.LENGTH_LONG).show();
                    } else {
                        chat = new Chat();
                        chat.setUserId(senderContactId);
                        chat.setName(senderContactName);
                        chat.setMensageValue(mensageValue);

                        Boolean destinationChatSaved = saveChat(destinationContactId, senderContactId, chat);
                        if (!destinationChatSaved) {
                            Toast.makeText(ChatActivity.this, "Problema ao enviar conversa", Toast.LENGTH_LONG).show();
                        }
                    }

                    editMensage.setText("");
                }
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                double lat = data.getDoubleExtra("lat", 0);
                double lng = data.getDoubleExtra("lng", 0);

                Log.i("LOCATION", "lat: " + lat + ", lng: " + lng);
            }
        }
    }

    private void openMapActivity() {
        Intent i = new Intent(this, MapActivity.class);
        startActivityForResult(i, LOCATION_REQUEST_CODE);
    }

    private boolean saveMensage(String senderId, String destinationId, Mensage mensage) {
        try {
            databaseReference = FirebaseConfig.getDatabaseReference().child("mensages");

            databaseReference.child(senderId)
                    .child(destinationId)
                    .push()
                    .setValue(mensage);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveChat(String senderId, String destinationId, Chat chat) {
        try {
            databaseReference = FirebaseConfig.getDatabaseReference().child("chat");
            databaseReference.child(senderId)
                    .child(destinationId)
                    .setValue(chat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerMensage);
    }
}

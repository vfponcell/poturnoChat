package com.poturno.poturnochat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.poturno.poturnochat.R;
import com.poturno.poturnochat.adapter.TabAdapter;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.helper.Base64Custom;
import com.poturno.poturnochat.helper.Preferences;
import com.poturno.poturnochat.helper.SlidingTabLayout;
import com.poturno.poturnochat.model.Contact;
import com.poturno.poturnochat.model.User;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_page);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorLight1));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_logout:
                openLogin();
                return true;
            case R.id.item_settings:
                openConfig();
                return true;
            case R.id.item_add:
                openContactRegister();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openContactRegister(){

        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogAddContact);

        aleBuilder.setTitle("Novo contato");
        aleBuilder.setMessage("E-mail do usuario");
        aleBuilder.setCancelable(false);

        final EditText contactEmail = new EditText(MainActivity.this);
        aleBuilder.setView(contactEmail);

        aleBuilder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String contactEmailValue = contactEmail.getText().toString();

                if(contactEmailValue.isEmpty()){
                    Toast.makeText(MainActivity.this,"Email vazio",Toast.LENGTH_LONG).show();
                }else {
                    final String contactIdentifier = Base64Custom.encodeBase64(contactEmailValue);
                    databaseReference = FirebaseConfig.getDatabaseReference().child("users").child(contactIdentifier);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null){

                                User contactUser = dataSnapshot.getValue(User.class);

                                Preferences preferences = new Preferences(MainActivity.this);
                                String logedUserIdentifier = preferences.getIdentifier();

                                databaseReference = FirebaseConfig.getDatabaseReference();
                                databaseReference = databaseReference.child("contacts")
                                        .child(logedUserIdentifier)
                                        .child(contactIdentifier);

                                Contact contact = new Contact();
                                contact.setUserIdentifier(contactIdentifier);
                                contact.setEmail(contactUser.getEmail());
                                contact.setName(contactUser.getName());

                                databaseReference.setValue(contact);

                            }else{
                                Toast.makeText(MainActivity.this,"Usuario não encontrado",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

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


    private void openLogin(){
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openConfig(){
        Intent intent = new Intent(MainActivity.this,ConfigActivity.class);
        startActivity(intent);
    }
}

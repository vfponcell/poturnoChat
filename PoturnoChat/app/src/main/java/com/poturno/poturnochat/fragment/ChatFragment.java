package com.poturno.poturnochat.fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.poturno.poturnochat.R;
import com.poturno.poturnochat.activity.ChatActivity;
import com.poturno.poturnochat.adapter.ChatAdapter;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.helper.Base64Custom;
import com.poturno.poturnochat.helper.Preferences;
import com.poturno.poturnochat.model.Chat;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Chat> chats;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerChats;
    private NotificationManager notificationManager;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListenerChats);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chats = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        listView = (ListView) view.findViewById(R.id.lv_chats);
        adapter = new ChatAdapter(getActivity(),chats);
        listView.setAdapter(adapter);

        Preferences preferences = new Preferences(getActivity());
        final String logedUserId = preferences.getIdentifier();

        databaseReference = FirebaseConfig.getDatabaseReference().child("chat").child(logedUserId);

        valueEventListenerChats = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Chat chat = data.getValue(Chat.class);
                    chats.add(chat);
                    if(!(logedUserId.equals(chat.getUserId()))){
                        notifyNewMensage(chat);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListenerChats);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                Chat chat = chats.get(position);

                intent.putExtra("name",chat.getName());
                intent.putExtra("email", Base64Custom.decodeBase64(chat.getUserId()));

                startActivity(intent);
            }
        });

        return view;

    }

    private void notifyNewMensage(Chat chat){
        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("name",chat.getName());
        intent.putExtra("email", Base64Custom.decodeBase64(chat.getUserId()));

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0, intent,0);

        NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(getContext());
        builder.setTicker("Nova mensagem de "+chat.getName());
        builder.setContentTitle("Mensagem de "+chat.getName());
        builder.setContentText(chat.getMensageValue());

        Notification notification = builder.build();
        notificationManager.notify(Integer.parseInt(chat.getUserId()), notification);

        try{
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(),uri);
            ringtone.play();
        }catch (Exception e){

        }
    }

}

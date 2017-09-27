package com.poturno.poturnochat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.poturno.poturnochat.activity.ChatActivit;
import com.poturno.poturnochat.adapter.ChatAdapter;
import com.poturno.poturnochat.config.FirebaseConfig;
import com.poturno.poturnochat.helper.Base64Custom;
import com.poturno.poturnochat.helper.Preferences;
import com.poturno.poturnochat.model.Chat;
import com.poturno.poturnochat.model.Contact;

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


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerChats);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        String logedUserId = preferences.getIdentifier();

        databaseReference = FirebaseConfig.getDatabaseReference().child("chat").child(logedUserId);

        valueEventListenerChats = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Chat chat = data.getValue(Chat.class);
                    chats.add(chat);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), ChatActivit.class);

                Chat chat = chats.get(position);

                intent.putExtra("name",chat.getName());
                intent.putExtra("email", Base64Custom.decodeBase64(chat.getUserId()));

                startActivity(intent);
            }
        });

        return view;

    }

}

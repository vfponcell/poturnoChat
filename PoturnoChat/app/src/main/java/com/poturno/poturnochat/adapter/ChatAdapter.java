package com.poturno.poturnochat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poturno.poturnochat.R;
import com.poturno.poturnochat.model.Chat;

import java.util.ArrayList;

/**
 * Created by vitor on 26/09/2017.
 */

public class ChatAdapter extends ArrayAdapter<Chat> {

    private ArrayList<Chat> chats;
    private Context context;

    public ChatAdapter( Context context, ArrayList<Chat> objects) {
        super(context, 0, objects);
        this.context = context;
        this.chats = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(chats!=null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.chat_list, parent, false);

            Chat chat = chats.get(position);
            TextView contactName = (TextView) view.findViewById(R.id.txt_name);
            TextView lastmensage = (TextView) view.findViewById(R.id.txt_mensage);
            contactName.setText(chat.getName());
            lastmensage.setText(chat.getMensageValue());

            if(position%2==1){
                view.setBackgroundResource(R.color.colorLight0);

            }

        }
        return view;
    }
}

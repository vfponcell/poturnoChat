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
        View _view = convertView;
        ViewHolder vh;

        if(chats!=null){
            if(_view==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                _view = inflater.inflate(R.layout.chat_list, parent, false);
                vh = new ViewHolder();
                vh.contactName = (TextView) _view.findViewById(R.id.txt_name);
                vh.lastMensage = (TextView) _view.findViewById(R.id.txt_mensage);
                _view.setTag(vh);
            }else{
                vh = (ViewHolder) _view.getTag();
            }

            Chat chat = chats.get(position);

            vh.contactName.setText(chat.getName());
            vh.lastMensage.setText(chat.getMensageValue());

            if(position%2==1){
                _view.setBackgroundResource(R.color.colorLight0);
            }else{
                _view.setBackgroundResource(R.color.colorAccent);
            }

        }
        return _view;
    }

    private static class ViewHolder{
        public  TextView contactName;
        public  TextView lastMensage;
    }
}

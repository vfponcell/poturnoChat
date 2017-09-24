package com.poturno.poturnochat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poturno.poturnochat.R;
import com.poturno.poturnochat.model.Contact;

import java.util.ArrayList;

/**
 * Created by vitor on 20/09/2017.
 */

public class ContactsAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;
    private Context context;

    public ContactsAdapter( Context context, ArrayList<Contact> objects) {
        super(context, 0, objects);
        this.context = context;
        this.contacts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(contacts!=null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.contact_list, parent,false);

            Contact contact = contacts.get(position);
            TextView contactName = (TextView) view.findViewById(R.id.txt_name);
            TextView contactEmail = (TextView) view.findViewById(R.id.txt_email);
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());

            if(position%2==1){
                view.setBackgroundResource(R.color.colorLight0);

            }
        }

        return  view;
    }
}

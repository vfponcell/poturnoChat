package com.poturno.poturnochat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poturno.poturnochat.R;
import com.poturno.poturnochat.helper.Preferences;
import com.poturno.poturnochat.model.Mensage;

import java.util.ArrayList;

/**
 * Created by vitor on 22/09/2017.
 */

public class MensageAdapter extends ArrayAdapter<Mensage>{

    private Context context;
    private ArrayList<Mensage> mensages;

    public MensageAdapter(Context context, ArrayList<Mensage> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mensages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(mensages!=null){

            Preferences preferences = new Preferences(context);
            String senderContactId = preferences.getIdentifier();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Mensage mensage = mensages.get(position);

            if(senderContactId.equals(mensage.getUserId())){
                view = inflater.inflate(R.layout.right_mensage, parent, false);
            }else{
                view = inflater.inflate(R.layout.left_mensage, parent, false);
            }

            TextView mensageText = (TextView) view.findViewById(R.id.tv_mensage);
            mensageText.setText(mensage.getMensage());


        }
        return view;
    }
}

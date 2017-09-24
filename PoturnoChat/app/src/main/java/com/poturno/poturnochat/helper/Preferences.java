package com.poturno.poturnochat.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vitor on 14/09/2017.
 */

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private final String FILE_NAME = "poturnochat.preferences";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String INDENTIFIER_KEY = "logedUserIdentifier";
    private final String NAME_KEY = "logedUserName";

    public Preferences(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences(FILE_NAME,MODE);
        editor = preferences.edit();
    }

    public void saveData(String userIdentifier ,String userName){
        editor.putString(INDENTIFIER_KEY,userIdentifier);
        editor.putString(NAME_KEY,userName);
        editor.commit();
    }

    public String getIdentifier(){
        return preferences.getString(INDENTIFIER_KEY,null);
    }

    public String getName(){
        return preferences.getString(NAME_KEY,null);
    }
}

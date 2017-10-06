package com.poturno.poturnochat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.poturno.poturnochat.fragment.ChatFragment;
import com.poturno.poturnochat.fragment.ContactsFragment;

/**
 * Created by vitor on 13/09/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter{

    private String[] tapTitles = {"CONVERSAS","CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0 :
                fragment = new ChatFragment();
                break;
            case 1:
                fragment = new ContactsFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tapTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tapTitles[position];
    }
}

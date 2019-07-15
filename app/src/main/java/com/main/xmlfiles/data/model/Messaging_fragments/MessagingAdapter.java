package com.main.xmlfiles.data.model.Messaging_fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.main.xmlfiles.Tab1;
import com.main.xmlfiles.Tab2;
import com.main.xmlfiles.Tab3;

import java.util.ArrayList;

public class MessagingAdapter extends FragmentPagerAdapter {

    String tabName;

    public MessagingAdapter(FragmentManager fm,String tabName){
        super(fm);
        this.tabName = tabName;
    }
    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Inbox();
            case 1: return new Sent();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return tabName;
            case 1: return "Sent";
            default: return null;
        }
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}

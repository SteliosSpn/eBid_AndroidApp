package com.main.xmlfiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    String tabName;

    public MyPagerAdapter(FragmentManager fm, String tabName){
        super(fm);
        this.tabName = tabName;
    }
    @Override    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Tab1();
            case 1: return new Tab2();
            case 2: return new Tab3();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override    public CharSequence getPageTitle(int position) {
        switch (position){
        case 0: return "All Auctions";
        case 1: return "My Auctions";
        case 2: return tabName;
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

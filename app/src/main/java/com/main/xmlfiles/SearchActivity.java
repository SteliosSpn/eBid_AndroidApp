package com.main.xmlfiles;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.main.xmlfiles.data.model.Messaging_fragments.Inbox;
import com.main.xmlfiles.data.model.Messaging_fragments.Sent;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class SearchActivity extends AppCompatActivity implements
        Tab1.OnFragmentInteractionListener , Tab2.OnFragmentInteractionListener, Tab3.OnFragmentInteractionListener,
Inbox.OnFragmentInteractionListener, Sent.OnFragmentInteractionListener{

    String tabName = "Messaging";
    private APIService mAPIService;

    TabLayout tabLayout;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        threadForIncomes();

        viewPager = (ViewPager) findViewById(R.id.pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),"Messaging");
        viewPager.setAdapter(myPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    void threadForIncomes(){
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                Log.e("Pager Adapter","Thread..");
                getUnread();
                myPagerAdapter.setTabName(tabName);
                myPagerAdapter.notifyDataSetChanged();
                //viewPager.setAdapter(myPagerAdapter);
                viewPager.invalidate();
                //tabLayout.setupWithViewPager(viewPager);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    public void getUnread() {
        Log.d("Page Adapter", "Unread...");

        getUnreadMsgs(getData());
    }

    String getData() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        //String restoredText = prefs.getString("text", null);
        //if (restoredText != null) {
        String username = prefs.getString("username", "0");//"No name defined" is the default value.
        //int idName = prefs.getInt("idName", 0); //0 is the default value.
        //}
        return username;
    }

    void getUnreadMsgs(String user){
        Log.d("Pager Adapter", "Getting Unread Messages...");
        mAPIService.getUnreadMessages(user).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                tabName = "Messaging("+response.body()+")";
                if (response.isSuccessful()) {
                    Log.i("Pager Adapter", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Pager Adapter", "Unable to submit post to API.");
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

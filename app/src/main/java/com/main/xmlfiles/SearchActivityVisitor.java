package com.main.xmlfiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.main.xmlfiles.data.model.Items;
import com.main.xmlfiles.data.model.MyAuctions;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class SearchActivityVisitor extends AppCompatActivity {
    EditText search;
    private APIService mAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_visitor);

        mAPIService = ApiUtils.getAPIService(getApplicationContext());

        final ListView list = findViewById(R.id.list);
        final ArrayList<ListViewData> arrayList = new ArrayList<ListViewData>();
        final List<MyAuctions> a = new ArrayList<>();
        search = findViewById(R.id.search1);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                a.clear();
                int imagescount = 0;
                arrayList.clear();
                if(s.toString() != null && !s.toString().isEmpty())
                    a.addAll(searchAuctions(s.toString()));
                for(MyAuctions myAuction: a){
                    Log.e(TAG,a.get(0).getName());
                    arrayList.add(new ListViewData(myAuction.getName(),"Highest Bid: "+String.valueOf(myAuction.getCurrent_bid()),getBitmapFromString(myAuction)));
                    imagescount++;
                }
                ListViewAdapter customAdapter = new ListViewAdapter(getBaseContext(), R.layout.row_item, arrayList);
                list.setAdapter(customAdapter);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewAuction.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("ActivityName", "Tab2");
                mBundle.putSerializable("MyClass", a.get(position));
                //intent.putExtra("MyClass", a.get(position));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }

    List<MyAuctions> searchAuctions(String searchTag) {
        Log.d(TAG, "Get All Auctions");
        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        mAPIService = ApiUtils.getAPIService(getApplicationContext());

        return sendPost(searchTag);
    }

    List<MyAuctions> sendPost(String searchTag){
        List<MyAuctions> myList = new ArrayList<>();
        Call<JsonArray> call = mAPIService.searchauctionsbyword(searchTag);
        try {
            Response<JsonArray> resp = call.execute();
            String myString = resp.body().toString();

            Type listType = new TypeToken<ArrayList<MyAuctions>>(){}.getType();
            Log.e(TAG,myString);
            Gson gson = new Gson();
            MyAuctions auctionsArray[] = gson.fromJson(myString, MyAuctions[].class);
            myList = Arrays.asList(auctionsArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myList;
    }

    Bitmap getBitmapFromString(MyAuctions a){
        Bitmap bmp = null;
        for(Items item: a.getItems()){
            for(String pic: item.getPictures_str()){
                if(Build.VERSION.SDK_INT >= 26){
                    bmp = BitmapFactory.decodeByteArray(Base64.getDecoder().decode(pic),0, Base64.getDecoder().decode(pic).length);
                }
            }
        }
        return bmp;
    }
}

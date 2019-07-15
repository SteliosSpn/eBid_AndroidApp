package com.main.xmlfiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.main.xmlfiles.data.model.Items;
import com.main.xmlfiles.data.model.MyAuctions;

import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class ViewAuction extends AppCompatActivity {

    private static final String TAG = "ViewAuctionActivity";
    //private static final int REQUEST_SIGNUP = 0;
    MyAuctions auctions;

    private APIService mAPIService;

    @BindView(R.id.current_bid) TextView _current_bid;
    @BindView(R.id.starting_bid) TextView _starting_bid;
    @BindView(R.id.auction_name) TextView _auction_name;
    @BindView(R.id.auctionner) TextView _auctionner;
    @BindView(R.id.btn_vote) Button _vote;
    @BindView(R.id.starts) TextView _hour_starts;
    @BindView(R.id.countdown) TextView _hour_ends;
    @BindView(R.id.input_bid_amount) EditText _bid_amount;
    @BindView(R.id.input_bid_amount_layout)
    TextInputLayout _bid_amount_layout;
    @BindView(R.id.btn_place_bid) Button _btn_place_bid;
    @BindView(R.id.btn_start_now) Button _start_now;
    @BindView(R.id.highest_bidder) TextView _highest_bidder;

    @BindView(R.id.myLayout) LinearLayout _myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auction);
        ButterKnife.bind(this);
        auctions = getAuctionObj();
        final ListView list_rec = findViewById(R.id.list_rec);
        _myLayout.removeView(_start_now);

        //Changing hours...
        String starts = auctions.getStarts().replace("T"," ");
        starts = starts.substring(0,16);
        String ends = auctions.getEnds().replace("T"," ");
        ends = ends.substring(0,16);

        Timestamp start_t = (Timestamp.valueOf(auctions.getStarts().replace("T"," ").replace("+0000","")));
        Log.e(TAG,start_t+"");

        if(getActivityName().equals("Tab2")){
            initRec(list_rec);
            if(auctions.getAuctioneer().equals(getData())){
                _myLayout.addView(_start_now);
                _myLayout.removeView(_btn_place_bid);
                _myLayout.removeView(_bid_amount);
                _myLayout.removeView(_bid_amount_layout);
                _myLayout.removeView(_vote);

                if(auctions.getHighest_bidder() != null){
                    _highest_bidder.setText("Highest Bidder: "+auctions.getHighest_bidder());
                    _highest_bidder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createPopUpForVoteBidder();
                        }
                    });
                }else{
                    _highest_bidder.setText("Highest Bidder: -");
                }
                if(start_t.after(new Timestamp(System.currentTimeMillis()))){
                    Log.e(TAG,"mpikeeee");
                    _start_now.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createAuctionNow(getData(),auctions.getAuction_id());
                        }
                    });
                }
                else{
                    _myLayout.removeView(_start_now);
                }
            }

        }
        if(getActivityName().equals("Tab1")){
            initRec(list_rec);
            if(auctions.getAuctioneer().equals(getData())){
                _myLayout.addView(_start_now);
                if(start_t.after(new Timestamp(System.currentTimeMillis()))){
                    Log.e(TAG,"mpikeeee");
                    _start_now.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createAuctionNow(getData(),auctions.getAuction_id());
                        }
                    });
                }
                else{
                    _myLayout.removeView(_start_now);
                }
                _myLayout.removeView(_btn_place_bid);
                _myLayout.removeView(_bid_amount);
                _myLayout.removeView(_bid_amount_layout);
                _myLayout.removeView(_vote);
                if(auctions.getHighest_bidder() != null){
                    _highest_bidder.setText("Highest Bidder: "+auctions.getHighest_bidder());
                    _highest_bidder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createPopUpForVoteBidder();
                        }
                    });
                }else{
                    _highest_bidder.setText("Highest Bidder: -");
                }
            }else {
                _highest_bidder.setText("Highest Bidder: " + auctions.getHighest_bidder());
                _myLayout.removeView(_start_now);
                _vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createPopUpForVote();
                    }
                });
            }

            }
        if(getData().equals("visitor")){
            _myLayout.removeView(_highest_bidder);
            _myLayout.removeView(_start_now);
            _myLayout.removeView(_btn_place_bid);
            _myLayout.removeView(_bid_amount);
            _myLayout.removeView(_bid_amount_layout);
            _myLayout.removeView(_vote);
        }
        _btn_place_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUp();
            }
        });

        _auctionner.setText("Auctioneer: "+auctions.getAuctioneer());
        _auction_name.setText("Auction Name: "+auctions.getName());
        _starting_bid.setText("Starting bid: "+auctions.getStart_bid().toString());
        _current_bid.setText("Highest Bid: "+auctions.getCurrent_bid().toString());
        _hour_starts.setText("Starting Hour: "+starts);
        _hour_ends.setText("Ending Hour: "+ends);

        int index = 0;
        for(Items item: auctions.getItems()){
            _myLayout.addView(getViewRecycle());
            _myLayout.addView(createImageView(index));
            _myLayout.addView(createTextView("Item Name: "+item.getName(),"#FFFFFF"));
            _myLayout.addView(createTextView("Item Description: "+item.getDescription(),"#FFFFFF"));
            _myLayout.addView(createTextView("Country: "+item.getCountry(),"#FFFFFF"));
            _myLayout.addView(createTextView("Latitude: "+item.getLatitude(),"#FFFFFF"));
            _myLayout.addView(createTextView("Longitude: "+item.getLongitude(),"#FFFFFF"));
            Log.e(TAG,item.getTags().size()+"");
            for(String tag: item.getTags()){
                _myLayout.addView(createTextView("- "+tag,"#EEEEEE"));
            }
            index++;
        }
        _myLayout.addView(createTextView("Recommended Items For You","#111111"));
        _myLayout.removeView(list_rec);
        _myLayout.addView(list_rec);

    }

    void initRec(ListView list_rec){
        final ArrayList<ListViewData> arrayList = new ArrayList<ListViewData>();
            //item recommentation
            final List<Items> items = new ArrayList<>();
            items.addAll(getRec());
            for(Items item: items){
                Log.e(TAG,item.getName());
                arrayList.add(new ListViewData(item.getName(),item.getCountry(),getBitmapFromStringRec(item)));
            }
            ListViewAdapter customAdapter = new ListViewAdapter(getApplicationContext(), R.layout.row_item, arrayList);
            list_rec.setAdapter(customAdapter);

            list_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<MyAuctions> myAuctions = new ArrayList<>();
                    myAuctions.addAll(getRecData(items.get(position).getAuction_id()));
                    Intent intent = new Intent(getApplicationContext(), ViewAuction.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("ActivityName", "Tab1");
                    for(MyAuctions mc: myAuctions){
                        if(mc.getAuction_id() == items.get(position).getAuction_id()){
                            mBundle.putSerializable("MyClass", mc);
                        }
                    }
                    //intent.putExtra("MyClass", myAuctions.get(0));
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
    }

    Bitmap getBitmapFromStringRec(Items item){
        Bitmap bmp = null;
            for(String pic: item.getPictures_str()){
                if(Build.VERSION.SDK_INT >= 26){
                    bmp = BitmapFactory.decodeByteArray(Base64.getDecoder().decode(pic),0, Base64.getDecoder().decode(pic).length);
                }
            }

        return bmp;
    }

    Bitmap getBitmapFromString(MyAuctions a, int index){
        Bitmap bmp = null;
        int count=0;
        for(Items item: a.getItems()){
            if(count > index){
                break;
            }
            count++;
            for(String pic: item.getPictures_str()){
                if(Build.VERSION.SDK_INT >= 26){
                    Log.e(TAG,pic);
                    bmp = BitmapFactory.decodeByteArray(Base64.getDecoder().decode(pic),0, Base64.getDecoder().decode(pic).length);
                }
            }
        }
        return bmp;
    }

    List<Items> getRec() {
        Log.d(TAG, "Getting rec..");

        mAPIService = ApiUtils.getAPIService(getApplicationContext());

        return sendPostRec();
    }

    List<MyAuctions> getRecData(Integer id) {
        Log.d(TAG, "Getting rec data..");

        mAPIService = ApiUtils.getAPIService(getApplicationContext());

        return sendPostRecData(id);
    }

    List<MyAuctions> sendPostRecData(Integer id){
        List<MyAuctions> myList = new ArrayList<>();
        Call<JsonArray> call = mAPIService.getrecommendAdditianlData(id);
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

    List<Items> sendPostRec(){
        List<Items> item = new ArrayList<>();
        Call<List<Items>> call = mAPIService.recommend(getData());
        try {
            Response<List<Items>> resp = call.execute();
            item.addAll(resp.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    RecyclerView getViewRecycle(){
        RecyclerView rv = new RecyclerView(this);
        rv.addItemDecoration(new DividerItemDecoration(this,  LinearLayoutManager.VERTICAL));
        return rv;
    }

    TextView createTextView(final String hint, String textColor) {
        final TextView newEditText = new TextView(this);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //final TextView textView = new TextView(this);
        newEditText.setLayoutParams(lparams);
        newEditText.setText(hint);
        newEditText.setTextColor(Color.parseColor(textColor));
        newEditText.setGravity(Gravity.LEFT);
        newEditText.setHeight(100);
        return newEditText;
    }

    ImageView createImageView(final int index) {
        final ImageView newImage = new ImageView(this);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newImage.setLayoutParams(lparams);
        newImage.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromString(auctions,index), 260, 260, false));
        return newImage;
    }

    void createPopUp(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        placeBid();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAuction.this);
        builder.setMessage("Are you sure you want to place this bid?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    void createPopUpForVote(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        placeVote(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        placeVote(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAuction.this);
        builder.setMessage("Rate the Auctioneer").setPositiveButton("Upvote", dialogClickListener)
                .setNegativeButton("Downvote", dialogClickListener).show();
    }


    void createPopUpForVoteBidder(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        placeVoteBidder(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        placeVoteBidder(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewAuction.this);
        builder.setMessage("Rate the Bidder").setPositiveButton("Upvote", dialogClickListener)
                .setNegativeButton("Downvote", dialogClickListener).show();
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

    String getActivityName() {
        Intent intent = getIntent();
        return intent.getStringExtra("ActivityName");
    }

    MyAuctions getAuctionObj() {
        return (MyAuctions) getIntent().getSerializableExtra("MyClass");
    }

    public void placeBid() {
        Log.d(TAG, "Making bid...");

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        //_btn_place_bid.setEnabled(false);

        // TODO: authentication logic...
        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        sendPost(auctions.getAuction_id(), getData(), Double.parseDouble(_bid_amount.getText().toString()));
    }

    public void placeVote(Boolean type) {
        Log.d(TAG, "Making vote...");

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        //_btn_place_bid.setEnabled(false);

        // TODO: authentication logic...
        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        sendVote(getData(),auctions.getAuctioneer(),type);
        Log.e(TAG,getData());
        Log.e(TAG,auctions.getAuctioneer());
        Log.e(TAG,type.toString());
    }

    public void placeVoteBidder(Boolean type) {
        Log.d(TAG, "Making vote for Bidder...");

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        //_btn_place_bid.setEnabled(false);

        // TODO: authentication logic...
        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        sendVoteBidder(getData(),auctions.getHighest_bidder(),type);
        Log.e(TAG,getData());
        Log.e(TAG,auctions.getHighest_bidder());
        Log.e(TAG,type.toString());
    }

    public void createAuctionNow(String user, Integer auction_id) {
        Log.d(TAG, "Starting auction....");

        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        sendPostCreateAuctionNow(user,auction_id);
    }

    public void sendPostCreateAuctionNow(String user,Integer auction_id) {
        Log.d(TAG, "Sending Post...");
        mAPIService.startAuction(user,auction_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(),response.body(),Toast.LENGTH_LONG).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void sendPost(Integer auction_id, String bidder_id, Double bid) {
        Log.d(TAG, "Sending Post...");
        mAPIService.makeBid(auction_id, bidder_id, bid).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    //createPopUpForVote();
                    Toast.makeText(getBaseContext(),response.body(),Toast.LENGTH_LONG).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void sendVote(String voter_id, String candidate_id, Boolean type_vote) {
        Log.d(TAG, "Sending Post Vote...");
        mAPIService.upvoteSeller(voter_id,candidate_id,type_vote).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(),response.body(),Toast.LENGTH_LONG).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void sendVoteBidder(String voter_id, String candidate_id, Boolean type_vote) {
        Log.d(TAG, "Sending Post Vote for Bidder...");
        mAPIService.upvoteBidder(voter_id,candidate_id,type_vote).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(),response.body(),Toast.LENGTH_LONG).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}

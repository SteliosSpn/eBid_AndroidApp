package com.main.xmlfiles;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main.xmlfiles.data.model.Auctions;
import com.main.xmlfiles.data.model.Items;
import com.main.xmlfiles.data.model.Users;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_Auction extends AppCompatActivity {
    private static final String TAG = "AuctionActivity";
    private APIService mAPIService;
    private Integer auction_id;
    String imagePath;
    List<String> imagesPath;
    HashMap<View,String> itemNames;
    HashMap<View,String> itemCountries;
    HashMap<View,String> itemDescs;
    HashMap<View,Double> latitudes;
    HashMap<View,Double> longitudes;

    HashMap<View,Boolean> games;
    HashMap<View,Boolean> clothes;
    HashMap<View,Boolean> furniture;
    HashMap<View,Boolean> devices;
    HashMap<View,Boolean> sex;

    int count = 0;
    int imagecountin = 0;
    List<String> tags;

    TimePickerDialog picker;
    DatePickerDialog date_picker;
    //starts
    int hour;
    int minutes;
    int day;
    int month;
    int year;
    //ends
    int ehour;
    int eminutes;
    int eday;
    int emonth;
    int eyear;

    @BindView(R.id.input_auctionName) EditText _auctionName;
    @BindView(R.id.input_bid) EditText _bid;
    @BindView(R.id.input_starts) Button _starts;
    @BindView(R.id.input_ends) Button _ends;
    @BindView(R.id.input_starts_date) Button _starts_date;
    @BindView(R.id.input_ends_date) Button _ends_date;
    @BindView(R.id.link_back) TextView _back_btn;
    @BindView(R.id.btn_auction) Button _auction_btn;
    @BindView(R.id.checkbox1) CheckBox _tag1;
    @BindView(R.id.checkbox2) CheckBox _tag2;
    @BindView(R.id.checkbox3) CheckBox _tag3;
    @BindView(R.id.checkbox4) CheckBox _tag4;
    @BindView(R.id.checkbox5) CheckBox _tag5;
    @BindView(R.id.btn_upload) Button _upload_btn;
    @BindView(R.id.btn_plus) Button _plus_btn;
    @BindView(R.id.item_name) EditText _itemName;
    @BindView(R.id.item_country) EditText _country;
    @BindView(R.id.input_description) EditText _description;
    @BindView(R.id.input_lat) EditText _lat;
    @BindView(R.id.input_lon) EditText _lon;

    @BindView(R.id.myLayout) LinearLayout _myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__auction);
        ButterKnife.bind(this);

        imagesPath = new ArrayList<>();

        itemNames = new HashMap<>();
        itemCountries = new HashMap<>();
        itemDescs = new HashMap<>();
        latitudes = new HashMap<>();
        longitudes = new HashMap<>();

        games = new HashMap<>();
        clothes = new HashMap<>();
        furniture = new HashMap<>();
        devices = new HashMap<>();
        sex = new HashMap<>();

        _back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

        _starts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                hour = cldr.get(Calendar.HOUR_OF_DAY);
                minutes = cldr.get(Calendar.MINUTE);
                picker = new TimePickerDialog(Create_Auction.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                _starts.setText(String.format("%02d:%02d", sHour, sMinute));
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        _starts_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                year = cldr.get(Calendar.YEAR);
                // date picker dialog



                date_picker = new DatePickerDialog(Create_Auction.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                _starts_date.setText(String.format("%04d-%02d-%02d", year, monthOfYear+1,dayOfMonth));
                            }
                        }, year, month, day);

                date_picker.show();
            }
        });

        _ends_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                eday = cldr.get(Calendar.DAY_OF_MONTH);
                emonth = cldr.get(Calendar.MONTH);
                eyear = cldr.get(Calendar.YEAR);
                // date picker dialog
                date_picker = new DatePickerDialog(Create_Auction.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                _ends_date.setText(String.format("%04d-%02d-%02d", year, monthOfYear+1,dayOfMonth));
                            }
                        }, eyear, emonth, eday);
                date_picker.show();
            }
        });

        _ends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                ehour = cldr.get(Calendar.HOUR_OF_DAY);
                eminutes = cldr.get(Calendar.MINUTE);

                picker = new TimePickerDialog(Create_Auction.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                _ends.setText(String.format("%02d:%02d", sHour, sMinute));
                            }
                        }, ehour, eminutes, true);
                picker.show();




            }
        });

        _plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                _myLayout.addView(createTextView("Item Name"));
                _myLayout.addView(createTextView("Country"));
                _myLayout.addView(createTextView("Description"));
                _myLayout.addView(createTextView("Latitude"));
                _myLayout.addView(createTextView("Longitude"));
                _myLayout.addView(createCheckBox("Games"));
                _myLayout.addView(createCheckBox("Clothes"));
                _myLayout.addView(createCheckBox("Furniture"));
                _myLayout.addView(createCheckBox("Device"));
                _myLayout.addView(createCheckBox("Toys"));
                _myLayout.addView(createButton("UPLOAD IMAGE"));
                _myLayout.removeView(_plus_btn);
                if(count <=6) {
                    _myLayout.addView(_plus_btn);
                }
                _myLayout.removeView(_auction_btn);
                _myLayout.addView(_auction_btn);
                _myLayout.removeView(_back_btn);
                _myLayout.addView(_back_btn);
            }
        });

        _upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                _upload_btn.setEnabled(false);
                startActivityForResult(intent, 0);
            }
        });


        _auction_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createAuction();
            }
        });
    }

    EditText createTextView(final String hint){
        final EditText newEditText = new EditText(this);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //final TextView textView = new TextView(this);
        newEditText.setLayoutParams(lparams);
        newEditText.setHint(hint);
        newEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(hint.equals("Item Name")){
                        if(!itemNames.containsKey(v)){
                            itemNames.put(v,newEditText.getText().toString());
                            Log.e(TAG,v.toString());
                        }
                        else{
                            itemNames.remove(v);
                            itemNames.put(v,newEditText.getText().toString());
                            Log.e(TAG,newEditText.getText().toString());
                        }
                    }else if(hint.equals("Country")){
                        if(!itemCountries.containsKey(v)){
                            itemCountries.put(v,newEditText.getText().toString());
                        }
                        else{
                            itemCountries.remove(v);
                            itemCountries.put(v,newEditText.getText().toString());
                        }
                    }else if(hint.equals("Description")){
                        if(!itemDescs.containsKey(v)){
                            itemDescs.put(v,newEditText.getText().toString());
                        }
                        else{
                            itemDescs.remove(v);
                            itemDescs.put(v,newEditText.getText().toString());
                        }
                    }else if(hint.equals("Latitude")){
                        if(!latitudes.containsKey(v)){
                            latitudes.put(v,Double.parseDouble(newEditText.getText().toString()));
                        }
                        else{
                            latitudes.remove(v);
                            latitudes.put(v,Double.parseDouble(newEditText.getText().toString()));
                        }
                    }else if(hint.equals("Longitude")){
                        if(!longitudes.containsKey(v)){
                            longitudes.put(v,Double.parseDouble(newEditText.getText().toString()));
                        }
                        else{
                            longitudes.remove(v);
                            longitudes.put(v,Double.parseDouble(newEditText.getText().toString()));
                        }
                    }
                }
            }
        });
        return newEditText;
    }

    CheckBox createCheckBox(final String hint){
        CheckBox newCheckBox = new CheckBox(this);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //final TextView textView = new TextView(this);
        newCheckBox.setLayoutParams(lparams);
        newCheckBox.setText(hint);
        newCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked)
            {
                if(hint.equals("Games")){
                    if(!games.containsKey(v)){
                        games.put(v,isChecked);
                    }
                    else{
                        games.remove(v);
                        games.put(v,isChecked);
                    }
                }else if(hint.equals("Clothes")){
                    if(!clothes.containsKey(v)){
                        clothes.put(v,isChecked);
                    }
                    else{
                        clothes.remove(v);
                        clothes.put(v,isChecked);
                    }
                }else if(hint.equals("Furniture")){
                    if(!furniture.containsKey(v)){
                        furniture.put(v,isChecked);
                    }
                    else{
                        furniture.remove(v);
                        furniture.put(v,isChecked);
                    }
                }else if(hint.equals("Device")){
                    if(!devices.containsKey(v)){
                        devices.put(v,isChecked);
                    }
                    else{
                        devices.remove(v);
                        devices.put(v,isChecked);
                    }
                }else if(hint.equals("Toys")){
                    if(!sex.containsKey(v)){
                        sex.put(v,isChecked);
                    }
                    else{
                        sex.remove(v);
                        sex.put(v,isChecked);
                    }
                }
            }
        });
        return newCheckBox;
    }

    Button createButton(String hint){
        final Button newButton = new Button(this);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //final TextView textView = new TextView(this);
        newButton.setLayoutParams(lparams);
        newButton.setText(hint);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                newButton.setEnabled(false);
                startActivityForResult(intent, 0);
            }
        });
        return newButton;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if(data == null){
                    Toast.makeText(getBaseContext(),"Unable to Choose Image",Toast.LENGTH_LONG).show();
                }
                Uri imageUri = data.getData();
                imagePath = getRealPathFromUri(imageUri);

                imagesPath.add(imagePath);
                imagecountin++;
                //this.finish();
            }
        }
    }

    private String getRealPathFromUri(Uri uri){
        String projection[] = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri ,projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }

    String getData(){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        //String restoredText = prefs.getString("text", null);
        //if (restoredText != null) {
            String username = prefs.getString("username", "No name defined");//"No name defined" is the default value.
            //int idName = prefs.getInt("idName", 0); //0 is the default value.
        //}
        return username;
    }

    void createAuction(){
        Log.d(TAG, "Auction starting");

        if (!validate()) {
            //onLoginFailed();
            return;
        }

        _auction_btn.setEnabled(false);


        Items item = makeItemObj(_itemName.getText().toString(),_country.getText().toString(),_description.getText().toString(),Double.parseDouble(_lat.getText().toString()),
                Double.parseDouble(_lon.getText().toString()),auction_id, _tag1.isChecked(),_tag2.isChecked(),_tag3.isChecked(),_tag4.isChecked(),_tag5.isChecked());

        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        //sendPostAuction(auction);

        String bid = _bid.getText().toString();
        tags = new ArrayList<>();
        if(getTagFromView(_tag1) != null){
            tags.add(getTagFromView(_tag1));
        }
        if(getTagFromView(_tag2) != null) {
            tags.add(getTagFromView(_tag2));
        }
        if(getTagFromView(_tag3) != null) {
            tags.add(getTagFromView(_tag3));
        }
        if(getTagFromView(_tag4) != null) {
            tags.add(getTagFromView(_tag4));
        }
        if(getTagFromView(_tag5) != null) {
            tags.add(getTagFromView(_tag5));
        }
        for(int i=1; i<=count; i++){
            if(getValueFromTextView(games,_tag1.getText().toString()) != null){
                Log.e(TAG,"Games....");
                tags.add("Games");
            }
            if(getValueFromTextView(clothes,_tag2.getText().toString()) != null) {
                Log.e(TAG,"Clothes....");
                tags.add("Clothes");
            }
            if(getValueFromTextView(furniture,_tag3.getText().toString()) != null) {
                tags.add("Furniture");
            }
            if(getValueFromTextView(devices,_tag4.getText().toString()) != null) {
                tags.add("Device");
            }
            if(getValueFromTextView(sex,_tag5.getText().toString()) != null) {
                tags.add("Toys");
            }
        }

        String auctionName = _auctionName.getText().toString();
        Auctions auction = new Auctions();
        auction.setStart_bid(Double.parseDouble(bid));
        auction.setTags(tags);
        auction.setName(auctionName);
        auction.setAuctioneer(getData());

        String text = _starts_date.getText().toString()+" "+_starts.getText().toString()+":00.12345";
        Timestamp ts = (Timestamp.valueOf(text));
        long timeInMilliSeconds = ts.getTime();
        auction.setStarts(new Timestamp(timeInMilliSeconds));

        String ends = _ends_date.getText().toString()+" "+_ends.getText().toString()+":00.00000";
        Timestamp tse = (Timestamp.valueOf(ends));
        long timeInMilliSeconds2 = tse.getTime();
        auction.setEnds(new Timestamp(timeInMilliSeconds2));

        if(tse.before(ts)){
            _starts_date.setError("wrong");
            _ends_date.setError("wrong");
            _starts.setError("wrong");
            _ends.setError("wrong");
            Toast.makeText(getBaseContext(),"Fix Dates",Toast.LENGTH_LONG).show();
            return;
        }

        Integer auction_id = sendPostAuction(auction);
        item.setAuction_id(auction_id);
        sendPostItems(item,imagesPath.get(0));
        List<Items> myItems = new ArrayList<>();

        for(int i=1; i<=count; i++){
            myItems.add(makeItemObj(getValueFromHash(itemNames,i),getValueFromHash(itemCountries,i),getValueFromHash(itemDescs,i),
                    getValueFromHashDouble(latitudes,i),getValueFromHashDouble(longitudes,i),auction_id,
                    getValueFromHashBool(games,i),getValueFromHashBool(clothes,i),getValueFromHashBool(furniture,i),getValueFromHashBool(devices,i),getValueFromHashBool(sex,i)));

            sendPostItems(myItems.get(i-1),imagesPath.get(i));
        }
    }

    String getValueFromTextView(HashMap<View,Boolean> myHash, String tagName){
        String value = null;
        for(Map.Entry<View, Boolean> entry : myHash.entrySet()) {
            if(entry.getValue()){
                value = tagName;
            }
        }
        return value;
    }

    String getTagFromView(CheckBox tag){
        String value = null;
        if(tag.isChecked()){
            value = tag.getText().toString();
        }
        return value;
    }

    String getValueFromHash(HashMap<View,String> myHash, Integer count){
        String value = null;
        Integer myCount = 0;
        for(Map.Entry<View, String> entry : myHash.entrySet()) {
            myCount++;
            value = entry.getValue();
            if(myCount == count){
                break;
            }
        }
        return value;
    }

    Boolean getValueFromHashBool(HashMap<View,Boolean> myHash, Integer count){
        Boolean value = false;
        Integer myCount = 0;
        for(Map.Entry<View, Boolean> entry : myHash.entrySet()) {
            myCount++;
            value = entry.getValue();
            if(myCount == count){
                break;
            }
        }
        return value;
    }

    Double getValueFromHashDouble(HashMap<View,Double> myHash, Integer count){
        Double value = 0.0;
        Integer myCount = 0;
        for(Map.Entry<View, Double> entry : myHash.entrySet()) {
            myCount++;
            value = entry.getValue();
            if(myCount == count){
                break;
            }
        }
        return value;
    }

    Items makeItemObj(String itemName, String itemCountry, String desc, Double lat, Double lon, Integer auction_id,
                      Boolean tag1, Boolean tag2, Boolean tag3, Boolean tag4, Boolean tag5){
        Items item = new Items();
        List<String> tags = new ArrayList<>();
        item.setAuction_id(auction_id);
        item.setName(itemName);
        item.setCountry(itemCountry);
        item.setDescription(desc);
        item.setLatitude(lat);
        item.setLongitude(lon);
        Log.e(TAG,"Setting tags");
        Log.e(TAG,tag1.toString());
        Log.e(TAG,tag2+"");
        Log.e(TAG,tag3+"");
        Log.e(TAG,tag4+"");
        Log.e(TAG,tag5+"");
        if(tag1 == true){
            tags.add("Games");
        }
        if(tag2 == true){
            tags.add("Clothes");
        }
        if(tag3 == true){
            tags.add("Furniture");
        }
        if(tag4 == true){
            tags.add("Device");
        }
        if(tag5 == true){
            tags.add("SexToys");
        }
        item.setTags(tags);
        return item;
    }

    public boolean validate() {
        boolean valid = true;

        if (_bid.getText().toString().isEmpty() || Double.parseDouble(_bid.getText().toString())<=0.0) {
            _bid.setError("Starting bid wrong!");
            valid = false;
        } else {
            _bid.setError(null);
        }

        if (!_tag1.isChecked() && !_tag2.isChecked() && !_tag3.isChecked() && !_tag4.isChecked() && !_tag5.isChecked()) {
            _tag1.setError("At least one tag is needed!");
            valid = false;
        } else {
            _tag1.setError(null);
        }
        if(imagecountin != count+1){
            valid = false;
            _upload_btn.setError("You must add at least 1 picture for every item!");
        }
        if(_lat.getText().toString().isEmpty()){
            _lat.setError("Latitude must not be empty!");
        }
        if(_lon.getText().toString().isEmpty()){
            _lon.setError("Longitude must not be empty!");
        }
        if(_description.getText().toString().isEmpty()){
            _description.setError("Empty description!");
        }
        if(_country.getText().toString().isEmpty()){
            _country.setError("Empty Country");
        }
        if(_auctionName.getText().toString().isEmpty()){
            _auctionName.setError("Auction Name must not be empty");
        }
        if(_itemName.getText().toString().isEmpty()){
            _itemName.setError("Item Name must not me empty!");
        }
        return valid;
    }

    public Integer sendPostAuction(Auctions auction){
        Log.d(TAG, "Sending post for auction...");
        Call<Auctions> call = mAPIService.getAuctions(auction);
        try {
            Response<Auctions> resp = call.execute();
            Auctions myAuction = resp.body();
            auction_id = myAuction.getAuction_id();
            //item.setAuction_id(auction_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return auction_id;
    }

    public void sendPostItems(Items item, String image_path) {
        Log.d(TAG, "Sending post item...");

        Call<Items> call2 = mAPIService.createItem(item);
        try {
            Response<Items> resp = call2.execute();
            Items myItem = resp.body();
            //item_id = myItem.getItem_id();
//            _auction_btn.setText(myItem.getItem_id().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        File file = new File(image_path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(), requestBody);

        Log.e(TAG,image_path);

        Call<Auctions> call3 = mAPIService.addPicture(body);
        try {
            Response<Auctions> resp = call3.execute();
            Toast.makeText(getBaseContext(),"Auction Created!",Toast.LENGTH_LONG).show();
            Auctions auction2 = resp.body();
            //item_id = myItem.getItem_id();
           // _auction_btn.setText(auction2.getAuction_id().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

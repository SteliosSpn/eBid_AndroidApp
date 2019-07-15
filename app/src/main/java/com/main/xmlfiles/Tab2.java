package com.main.xmlfiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.main.xmlfiles.data.model.Auctions;
import com.main.xmlfiles.data.model.Items;
import com.main.xmlfiles.data.model.MyAuctions;
import com.main.xmlfiles.data.model.MyAuctionsJsonObject;
import com.main.xmlfiles.data.model.Users;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2 extends Fragment {
    private APIService mAPIService;
    Button btn_create_auction;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAPIService = ApiUtils.getAPIService(getActivity().getApplicationContext());

        initList();
    }

    void initList(){
        View view = getView();
        if (view != null) {
            final ListView list = view.findViewById(R.id.list);
            final ArrayList<ListViewData> arrayList = new ArrayList<ListViewData>();
            btn_create_auction = view.findViewById(R.id.btn_create_auction);
            btn_create_auction.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Create_Auction.class);
                    startActivity(intent);
                }
            });
            final List<MyAuctions> a = new ArrayList<>();
            int imagescount = 0;
            a.addAll(searchAuctions(getData()));
            for (MyAuctions myAuction : a) {
                arrayList.add(new ListViewData(myAuction.getName(), "Bid: "+String.valueOf(myAuction.getCurrent_bid()), getBitmapFromString(myAuction)));

            }
            ListViewAdapter customAdapter = new ListViewAdapter(getContext(), R.layout.row_item, arrayList);
            list.setAdapter(customAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getBaseContext(), ViewAuction.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("ActivityName", "Tab2");
                    mBundle.putSerializable("MyClass", a.get(position));
                    //intent.putExtra("MyClass", a.get(position));
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                    Integer auction_id = a.get(pos).getAuction_id();
                    createPopUp(getData(), auction_id);
                    return true;
                }
            });
        }
    }

    void createPopUp(final String user, final Integer message_id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAuction(user, message_id);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this auction?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    void deleteAuction(String user, Integer auction_id) {
        mAPIService.deleteAuction(user, auction_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity().getBaseContext(),"Auction Deleted!",Toast.LENGTH_LONG).show();
                    //refreshh
                    initList();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    List<MyAuctions> searchAuctions(String user) {
        Log.d(TAG, "Getting my auctions...");


        mAPIService = ApiUtils.getAPIService(getActivity().getApplicationContext());

        return sendPost(user);
    }

    String getData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String username = prefs.getString("username", "No name defined");//"No name defined" is the default value.
        return username;
    }

     List<MyAuctions> sendPost(String user){
        List<MyAuctions> myList = new ArrayList<>();
        Call<JsonArray> call = mAPIService.getMyAuctions(user);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

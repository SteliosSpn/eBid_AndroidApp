package com.main.xmlfiles;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.main.xmlfiles.data.model.Items;
import com.main.xmlfiles.data.model.Logg;
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

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {
    EditText search;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private APIService mAPIService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAPIService = ApiUtils.getAPIService(getActivity().getApplicationContext());

        View view = getView();
        if(view != null) {
            final ArrayList<ListViewData> arrayList = new ArrayList<ListViewData>();
            final List<MyAuctions> a = new ArrayList<>();

            final ListView list = view.findViewById(R.id.list);
            search = view.findViewById(R.id.search1);
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
                    ListViewAdapter customAdapter = new ListViewAdapter(getContext(), R.layout.row_item, arrayList);
                    list.setAdapter(customAdapter);
                }
            });
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ViewAuction.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("ActivityName", "Tab1");
                    mBundle.putSerializable("MyClass", a.get(position));
                    //intent.putExtra("MyClass", a.get(position));
                    intent.putExtras(mBundle);
                    saveInLog(a.get(position));
                    startActivity(intent);
                }
            });
        }
    }

    List<MyAuctions> searchAuctions(String searchField) {
        Log.d(TAG, "Get All Auctions");
        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        mAPIService = ApiUtils.getAPIService(getActivity().getApplicationContext());

        return sendPost(searchField);
    }

    String getData(){
        SharedPreferences prefs = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        //String restoredText = prefs.getString("text", null);
        //if (restoredText != null) {
        String username = prefs.getString("username", "No name defined");//"No name defined" is the default value.
        //int idName = prefs.getInt("idName", 0); //0 is the default value.
        //}
        return username;
    }

    void saveInLog(MyAuctions myAuctions) {
        Log.d(TAG, "Saving In Logg");

        mAPIService = ApiUtils.getAPIService(getActivity().getApplicationContext());
        Logg log = new Logg();
        log.setAuction_id(myAuctions.getAuction_id());
        log.setUser_id(getData());

        sendPostLog(log);
    }

    void sendPostLog(Logg log){
        Call<Logg> call = mAPIService.insertLog(log);
        try {
            Response<Logg> resp = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

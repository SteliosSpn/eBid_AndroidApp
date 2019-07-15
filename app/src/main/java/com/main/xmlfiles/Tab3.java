package com.main.xmlfiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.main.xmlfiles.data.model.Messaging_fragments.Inbox;
import com.main.xmlfiles.data.model.Messaging_fragments.MessagingAdapter;
import com.main.xmlfiles.data.model.Messaging_fragments.Sent;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab3 extends Fragment implements Inbox.OnFragmentInteractionListener , Sent.OnFragmentInteractionListener{
    private static final String TAG = "Tab3";
    String tabName = "Inbox";
    private APIService mAPIService;

    TabLayout tabLayout;
    ViewPager viewPager;
    MessagingAdapter messagingAdapter;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab3.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
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
        return inflater.inflate(R.layout.fragment_tab3, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null) {
            mAPIService = ApiUtils.getAPIService(getActivity().getApplicationContext());

            viewPager = view.findViewById(R.id.message_pager);
            messagingAdapter = new MessagingAdapter(getChildFragmentManager(),tabName);
            viewPager.setAdapter(messagingAdapter);
            tabLayout = view.findViewById(R.id.message_layout);
            tabLayout.setupWithViewPager(viewPager);
            //threadForIncomes();
        }
    }

    void threadForIncomes(){
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                Log.e("Pager Adapter","Thread..");
                getUnread();
                messagingAdapter.setTabName(tabName);
                messagingAdapter.notifyDataSetChanged();
                //viewPager.setAdapter(myPagerAdapter);
                viewPager.invalidate();
                //tabLayout.setupWithViewPager(viewPager);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    public void getUnread() {
        Log.d("Message Adapter", "Unread...");

        getUnreadMsgs(getData());
    }

    String getData() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String username = prefs.getString("username", "0");//"No name defined" is the default value.
        return username;
    }

    void getUnreadMsgs(String user){
        Log.d("Message Adapter", "Getting Unread Messages...");
        mAPIService.getUnreadMessages(user).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                tabName = "Inbox("+response.body()+" Unread)";
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

    @Override
    public void onFragmentInteraction(Uri uri) {

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

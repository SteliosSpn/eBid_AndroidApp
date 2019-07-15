package com.main.xmlfiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.xmlfiles.data.model.Messaging_fragments.Inbox;
import com.main.xmlfiles.data.model.Users;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessage extends AppCompatActivity {
    private static final String TAG = "SendMessage";
    private APIService mAPIService;
    private EditText editText;
    private TextView message_full;
    private ImageView send_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        editText = findViewById(R.id.editText);
        message_full = findViewById(R.id.full_message);
        message_full.setText(getMessage());
        mAPIService = ApiUtils.getAPIService(getApplicationContext());

        send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (message.length() > 0) {
                    //scaledrone.publish(roomName, message);
                    Log.i("Ayto to activity:", message+" "+getReceiver()+" "+getData());
                    editText.getText().clear();
                    sendPost(getData(), getReceiver(), message);
                }
            }
        });
    }

    String getData() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String username = prefs.getString("username", "No name defined");//"No name defined" is the default value.
        return username;
    }

    String getReceiver() {
        Intent intent = getIntent();
        return intent.getStringExtra("receiver");
    }

    String getMessage(){
        Intent intent = getIntent();
        return intent.getStringExtra("message");
    }

    public void sendPost(String sender, String receiver, String message) {
        mAPIService.sendMessage(sender, receiver, message).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                //startActivity(intent);
                Toast.makeText(getBaseContext(),response.body(),Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
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

package com.main.xmlfiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.main.xmlfiles.data.model.Users;
import com.main.xmlfiles.data.model.remote.APIService;
import com.main.xmlfiles.data.model.remote.ApiUtils;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private APIService mAPIService;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_surname) EditText _surnameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_password_again) EditText _passwordText_again;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;
    @BindView(R.id.input_country) EditText _countryText;
    @BindView(R.id.input_city) EditText _cityText;
    @BindView(R.id.input_afm) EditText _afmText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_Phone) EditText _phoneText;
    @BindView(R.id.input_username) EditText _usernameText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();
        Integer afm = Integer.parseInt(_afmText.getText().toString());
        String address = _addressText.getText().toString();
        String phone = _phoneText.getText().toString();
        String username = _usernameText.getText().toString();
        Users user = new Users();
        user.setName(name);
        user.setUserId(username);
        user.setPassword(password);
        user.setSurname(surname);
        user.setAfm(afm);
        user.setCountry(country);
        user.setCity(city);
        user.setTelephone(phone);
        user.setAddress(address);
        user.setEmail(email);

        // TODO: Implement your own signup logic here.
        mAPIService = ApiUtils.getAPIService(getApplicationContext());
        sendPost(user);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void sendPost(Users user) {

        mAPIService.savePost(user)
                .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("this","this");
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(String response) {
        Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Register failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String password_again = _passwordText_again.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (password_again.isEmpty() || password_again.length() < 4 || password_again.length() > 10) {
            _passwordText_again.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }else if(!password.equals(password_again)){
            _passwordText_again.setError("passwords do not match!");
            valid = false;
        }
        else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

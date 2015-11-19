package com.jshikami235gmail.pataride;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SignupTrial extends AppCompatActivity implements View.OnClickListener{
    EditText fname,lname,email,password;
    Button btnSignin;
    String url_SIGN_UP = "http://10.0.2.2/pataride_api/sign_up.php";


    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_trial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fname = (EditText) findViewById(R.id.etfname);
        lname = (EditText) findViewById(R.id.etlname);
        password = (EditText) findViewById(R.id.etPassword);
        email = (EditText) findViewById(R.id.etEmail);

        btnSignin = (Button) findViewById(R.id.btn_signUpAgain);


    }

    @Override
    public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_signUpAgain:
                    User user = new User();
                    user.setFname(fname.getText().toString());
                    user.setLname(lname.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setEmail(email.getText().toString());

                    storeUser(user);
                    Intent intent = new Intent(SignupTrial.this, MapsActivity.class);
                    startActivity(intent);
                    finish();

            }

            // Session manager
            session = new SessionManager(getApplicationContext());

            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                Intent intent = new Intent(SignupTrial.this,
                        MapsActivity.class);
                startActivity(intent);
                finish();


            }


        if (fname.getText().toString().trim().equals("") ||
                lname.getText().toString().trim().equals("") ||
                email.getText().toString().trim().equals("") ||
                password.getText().toString().trim().equals("")) {
            Toast.makeText(getApplication(), ("Please make sure you entered all fields correctly."), Toast.LENGTH_LONG).show();
        } else {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(SignupTrial.this);
             String url = "http://192.168.44.40/pataride_api/sign_up.php";
               // Request a string response from the provided URL.
               StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                             // Display the first 500 characters of the response string.
                            Toast.makeText(getApplication(), ("Response string: " + response.substring(0, 500)), Toast.LENGTH_LONG).show();

                        }
                        },
                        new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.toString());
                        }
                    });

             }

        }
    

        public void storeUser(final User user){
            RequestQueue queue = Volley.newRequestQueue(this);

            final StringRequest request = new StringRequest(Request.Method.POST, url_SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> postParams = new HashMap<String,String>();
                postParams.put("first_name",user.getFname());
                postParams.put("second_name",user.getLname());
                postParams.put("email",user.getEmail());
                postParams.put("password",user.getPassword());
                return postParams;
            }
        };
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(request);
    }


}


package com.jshikami235gmail.pataride;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {


    private static final String TAG = login.class.getSimpleName();
    private Button btn_signIn;
    private EditText first_name, second_name, inputEmail, inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    String URL_LOGIN = "http://10.0.2.2/pataride_api/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        inputEmail = (EditText) findViewById(R.id.email_l);
        inputPassword = (EditText) findViewById(R.id.pwd_l);

        btn_signIn = (Button) findViewById(R.id.btn_signIn);


        btn_signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_signIn:

                        inputEmail = (EditText) findViewById(R.id.email_l);
                        inputPassword = (EditText) findViewById(R.id.pwd_l);


                }
                //Checking for empty fieldsWSQqw2qQ
                String strEmailAddress = inputEmail.getText().toString().trim();
                String strPassword = inputPassword.getText().toString().trim();

                Log.d("<email>", strEmailAddress);
                Log.d("<password>", strPassword);


                if (!strEmailAddress.isEmpty() && !strPassword.isEmpty()) {
                    // login user
                    checkLogin(strEmailAddress, strPassword);
                } else Toast.makeText(getApplicationContext(),
                        "Please enter credentials!", Toast.LENGTH_LONG)
                        .show();

                if ( inputPassword.getText().toString().length() > 4 ){
                    Toast.makeText(getApplicationContext()," nice length of password.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                }

                //TODO Auto-generated method stub

                Intent intent = new Intent(login.this, MapsActivity.class);
                startActivity(intent);
                finish();

            }

        });


        // Link to MapsActivity Screen


        inputEmail = (EditText) findViewById(R.id.email_l);
        inputPassword = (EditText) findViewById(R.id.pwd_l);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Checking if user is already logged in or not
        // Session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(login.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }

        btn_signIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                // Prompt user to enter credentials
                if (!email.toString().trim().isEmpty() && !password.toString().trim().isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(login.this, MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }


    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {


        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(first_name, second_name, email, uid, created_at);

                        // Launch Maps activity
                        Intent intent = new Intent(login.this,
                                MapsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}



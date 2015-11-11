package com.jshikami235gmail.pataride;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Sign_up extends Activity {


    private Button btn_signUp;
    private EditText inputFirstName, inputSecondName, inputEmail, inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputSecondName =(EditText) findViewById(R.id.second_name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btn_signUp = (Button) findViewById(R.id.btn_signUp);

        //Progress dialogue
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Session manager
        session = new SessionManager(getApplicationContext());

        //SQLite databse handler
        db = new SQLiteHandler(getApplicationContext());

        //Check if user is already logged in or not
        if (session.isLoggedIn()) {
            //User is logged in, take him to next activity.
            Intent intent = new Intent(Sign_up.this, MapsActivity.class);
            startActivity(intent);
            finish();

        }

        Button btn = (Button) findViewById(R.id.btn_signUp);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String first_name = inputFirstName.getText().toString().trim();
                String second_name = inputSecondName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                boolean isError = false;

                if (!first_name.isEmpty() && !second_name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    btn_signUp(first_name, second_name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }


                if (isError) {
                }
             }

          });

    }

     /* Function to store user in MYSQl db will post params (tag, first and second name, email pwd)
         to register uri*/

    private void btn_signUp(final String first_name, final String second_name, final String email, final String password) {

            String tag_json_obj = "json_obj_req";

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading details. Please wait.");
            pDialog.show();

            final HashMap<String, String> postParams = new HashMap<>
                    ();
            postParams.put("first_name", first_name);
            postParams.put("second_name", second_name);
            postParams.put("email", email);
            postParams.put("password", password);

            Response.Listener<JSONObject> listener;
            Response.ErrorListener errorListener;
            final JSONObject jsonObject = new JSONObject(postParams);

            JsonObjectRequest jsonObjReq = new
                    JsonObjectRequest(AppConfig.URL_REGISTER, jsonObject,
                            new com.android.volley.Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    //Log.d("TAG", response.toString());
                                    try {
                                        Toast.makeText(getApplicationContext(),
                                                response.getString("message"), Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), "Thank you for your post", Toast.LENGTH_LONG).show();

                                        if (response.getString("status").equals("success")) {

                                            session.setLogin(true);

                                            pDialog.dismiss();
                                            Intent i = new
                                                    Intent(Sign_up.this, MapsActivity.class);
                                            startActivity(i);

                                            finish();
                                         }

                                        } catch (JSONException e) {
                                        Log.e("TAG", e.toString());
                                    }
                                    pDialog.dismiss();
                                }
                            }, new com.android.volley.Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //VolleyLog.d("TAG", "Error: " + error.getMessage());
                            pDialog.dismiss();

                        }
                     })

                    {@Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                    };

        }

}

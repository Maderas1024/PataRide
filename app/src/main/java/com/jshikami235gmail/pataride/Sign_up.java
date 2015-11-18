package com.jshikami235gmail.pataride;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
//private static final String SIGN_UP_URL = "http://192.168.20.62/pataride_sign_up.php";


        import static android.widget.Toast.LENGTH_SHORT;

public class Sign_up extends Activity {


    private static final String LOG_TAG = Sign_up.class.getName();
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

        inputEmail = (EditText) findViewById(R.id.email_s);
        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputSecondName = (EditText) findViewById(R.id.second_name);
        inputPassword = (EditText) findViewById(R.id.password);

        btn_signUp = (Button) findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v == btn_signUp) {
                    registerUser();
                }
            }

            private void registerUser() {
                String first_name = inputFirstName.getText().toString().trim().toLowerCase();
                String second_name = inputSecondName.getText().toString().trim().toLowerCase();
                String password = inputPassword.getText().toString().trim().toLowerCase();
                String email = inputEmail.getText().toString().trim().toLowerCase();

                register(first_name, second_name, email, password);
            }

            private void register(String first_name, String second_name, String password, String email) {
                String urlSuffix = "?inputFirstName=" + first_name + "inputSecondName" + second_name + "&inputPassword=" + password + "&inputEmail=" + email;
                class RegisterUser extends AsyncTask<String, Void, String> {

                    ProgressDialog loading;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(Sign_up.this, "Please Wait", null, true, true);
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "progress dismissed", Toast.LENGTH_LONG).show();
                    }


                    @Override
                    protected String doInBackground(String... params) {
                        String s = params[0];
                        BufferedReader bufferedReader = null;
                        try {
                            URL url = new URL("http://10.0.2.2/pataride_sign_up.php" + s);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                            String result;

                            result = bufferedReader.readLine();

                            return result;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }

                RegisterUser ru = new RegisterUser();
                ru.execute(urlSuffix);
            }


        });


        if (!(!inputFirstName.equals("") && !inputSecondName.equals("") && !inputEmail.equals("") && !inputPassword.equals(""))) {
            Toast.makeText(getApplication(), ("Please make sure you entered all fields correctly."), Toast.LENGTH_LONG).show();
        } else {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(Sign_up.this);
            String url = "http://10.0.2.2/pataride_api/sign_up.php";
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
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> postParams = new HashMap<>();
                    postParams.put("first_name", inputFirstName.getText().toString());
                    postParams.put("second_name", inputSecondName.getText().toString());
                    postParams.put("email", inputEmail.getText().toString());
                    postParams.put("password", inputPassword.getText().toString());
                    return postParams;
                }

            };

        }

    }

            /* Function to store user in MYSQl db will post params (tag, first and second name, email pwd)
//         to register uri*/
//
    private void btn_signUp(final String first_name, final String second_name, final String email, final String password) {

        String tag_json_obj = "json_obj_req";
        final Uri.Builder builder = Uri.parse("http://10.0.2.2/pataride_api/sign_up.php").buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("password", password);
        builder.appendQueryParameter("name", first_name + " " + second_name);


        Toast.makeText(getApplication(), "we got here", LENGTH_SHORT).show();
        Log.d("url", builder.toString());


        JsonObjectRequest jsonObjReq = new
                JsonObjectRequest(Request.Method.POST, builder.toString(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("TAG", response.toString());
                        try {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Thank you for your post", Toast.LENGTH_LONG).show();

                            if (response.getString("status").equals("success")) {

                                session.setLogin(true);

                                pDialog.dismiss();
                                Intent i = new Intent(Sign_up.this, MapsActivity.class);
                                startActivity(i);

                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"I'm here",Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.e("TAG", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());


            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }




}

//
//                String first_name = inputFirstName.getText().toString().trim();
//                String second_name = inputSecondName.getText().toString().trim();
//                String email = inputEmail.getText().toString().trim();
//                String password = inputPassword.getText().toString().trim();


//                if (!(!inputFirstName.equals("") && !inputSecondName.equals("") && !inputEmail.equals("") && !inputPassword.equals(""))) {
//                    //AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up.this);
//                    Toast.makeText(getApplication(), ("Please make sure you entered all fields correctly."), Toast.LENGTH_LONG).show();
//                } else {
//
//                    // Instantiate the RequestQueue.
//                    RequestQueue queue = Volley.newRequestQueue(Sign_up.this);
//                    String url = "http://192.168.20.62/pataride_api/sign_up.php";
//                    // Request a string response from the provided URL.
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    // Display the first 500 characters of the response string.
//                                    Toast.makeText(getApplication(), ("Response string: " + response.substring(0, 500)), Toast.LENGTH_LONG).show();
//                                    Toast.makeText(getApplication(), "kjdfnkjd", LENGTH_SHORT).show();
//                                }
//                            },
//                            new Response.ErrorListener() {
//
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.e("TAG", error.toString());
//                                }
//
//                            }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> postParams = new HashMap<>();
//                            postParams.put("first_name", inputFirstName.getText().toString());
//                            postParams.put("second_name", inputSecondName.getText().toString());
//                            postParams.put("email", inputEmail.getText().toString());
//                            postParams.put("password", inputPassword.getText().toString());
//                            return postParams;
//                        }
//                    };
//
//                    stringRequest.setRetryPolicy(new RetryPolicy() {
//                        @Override
//                        public int getCurrentTimeout() {
//                            return 5000;
//                        }
//
//                        @Override
//                        public int getCurrentRetryCount() {
//                            return 5000;
//                        }
//
//                        @Override
//                        public void retry(VolleyError error) throws VolleyError {
//
//                        }
//                    });
//// Add the request to the RequestQueue.
//                    queue.add(stringRequest);
//
//                }
//
//
//            }
//
//
//        });
//
//    }
//
//




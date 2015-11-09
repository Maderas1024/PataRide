package com.jshikami235gmail.pataride;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import static com.jshikami235gmail.pataride.R.id.btn_signIn;
import static com.jshikami235gmail.pataride.R.id.toolbar;

public class Login extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

            Button btn = (Button) findViewById(btn_signIn);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    Intent activityChangeIntent = new Intent(Login.this, MapsActivity.class);

                    // currentContext.startActivity(activityChangeIntent);

                    Login.this.startActivity(activityChangeIntent);
                }
            });

        }

}

package com.jshikami235gmail.pataride;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Pay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Button btn = (Button) findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(Pay.this, thanks.class);

                // currentContext.startActivity(activityChangeIntent);

                Pay.this.startActivity(activityChangeIntent);

            }
        });

                Button button = (Button) findViewById(R.id.btn_decline);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click

                        Intent activityChangeIntent = new Intent(Pay.this, Welcome.class);

                        // currentContext.startActivity(activityChangeIntent);

                        Pay.this.startActivity(activityChangeIntent);


    }


      });

    }

}
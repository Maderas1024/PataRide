package com.jshikami235gmail.pataride;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.content.Intent;
import android.os.Handler;

import com.jshikami235gmail.pataride.utils.SessionManager;

import static com.jshikami235gmail.pataride.R.menu.menu_splash_screen;


public class Splash_screen extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        /*
      Duration of wait
     */
        int SPLASH_DISPLAY_LENGTH = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Checking if user is already logged in or not
                SessionManager session = new SessionManager(getApplicationContext());

                if (session.isLoggedIn()) {
                    Intent intent = new Intent(Splash_screen.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                      /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash_screen.this, Welcome.class);
                    Splash_screen.this.startActivity(mainIntent);
                    Splash_screen.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menu_splash_screen, menu);
        return true;
    }


}


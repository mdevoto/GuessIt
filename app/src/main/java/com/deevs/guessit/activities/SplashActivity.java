package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.deevs.guessit.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_DURATION_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        if(!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        setContentView(R.layout.activity_splash);

        final Runnable startNextActivityDelayed = new Runnable() { 
            @Override
            public void run() {
                if(AccessToken.getCurrentAccessToken() == null) {
                    final Intent startLoginScreen = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                    startActivity(startLoginScreen);
                }
                else {
                    final Intent openMainMenu = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(openMainMenu);
                }
                finish();
            }
        };
        new Handler().postDelayed(startNextActivityDelayed, SPLASH_DURATION_MS);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
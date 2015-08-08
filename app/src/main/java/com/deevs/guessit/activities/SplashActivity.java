package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.deevs.guessit.R;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_DURATION_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Runnable startNextActivityDelayed = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "Starting Facebook login Activity");
                final Intent startLoginScreen = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                startActivity(startLoginScreen);
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
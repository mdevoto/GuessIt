package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.AccountWrapper;
import com.deevs.guessit.networking.NetworkManager;
import com.facebook.FacebookSdk;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_DURATION_MS = 2000;

    private Handler mHandler;
    private AccountWrapper mAccount;
    private Runnable mStartNextActivityDelayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mAccount = new AccountWrapper();

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        if(!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        mStartNextActivityDelayed = new Runnable() {
            @Override
            public void run() {
                if(!mAccount.isLoggedIn()) {
                    final Intent startLoginScreen = new Intent(getApplicationContext(), LoginAndNetworkSetupActivity.class);
                    startActivity(startLoginScreen);
                }
                else {
                    final Intent openMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(openMainMenu);
                }
                mAccount = null;
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        initiateStartNextActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelStartNextActivity();
    }

    private void initiateStartNextActivity() {
        mHandler.postDelayed(mStartNextActivityDelayed, SPLASH_DURATION_MS);
    }

    private void cancelStartNextActivity() {
        mHandler.removeCallbacks(mStartNextActivityDelayed);
    }
}
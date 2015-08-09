package com.deevs.guessit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.deevs.guessit.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookLoginActivity extends FragmentActivity {

    public static final String TAG = FacebookLoginActivity.class.getSimpleName();

    private CallbackManager mCallbackMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook_login);

        Log.e(TAG, "onCreate: Starting Facebook login");
        final LoginButton loginBtn = (LoginButton) findViewById(R.id.login_button);
        mCallbackMgr = CallbackManager.Factory.create();

        // Permission to read user's friends who have the application installed.
        loginBtn.setReadPermissions("user_friends");
        loginBtn.registerCallback(mCallbackMgr, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess - Facebook Login, access token = " + loginResult.getAccessToken() + " permissions granted = " + loginResult.getRecentlyGrantedPermissions());

                final Runnable startNextActivityDelayed = new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "Starting Main Menu Activity with Access token");
                        final Intent startMainMenu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(startMainMenu);
                    }
                };
                new Handler().post(startNextActivityDelayed);
                finish();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel - Facebook Login");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "onError - Facebook Login, exception = " + e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackMgr.onActivityResult(requestCode, resultCode, data);
    }
}

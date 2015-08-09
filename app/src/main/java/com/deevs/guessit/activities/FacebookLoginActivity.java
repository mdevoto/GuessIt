package com.deevs.guessit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.deevs.guessit.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookLoginActivity extends FragmentActivity {

    public static final String TAG = FacebookLoginActivity.class.getSimpleName();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        final LoginButton loginBtn = (LoginButton) findViewById(R.id.login_button);
        final CallbackManager callbackManager = CallbackManager.Factory.create();

        // Permission to read user's friends who have the application installed.
        loginBtn.setReadPermissions("user_friends");
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess - Facebook Login, access token user ID = " + loginResult.getAccessToken().getUserId()
                        + " permissions granted = " + loginResult.getRecentlyGrantedPermissions());

                // Login success - Start/show the main menu now.
                final Intent startMainMenu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMainMenu);
                finish();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel - Facebook Login");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "onError - Facebook Login, exception = " + e.getMessage());

                // Disable the login button for 2 seconds before allowing try again..
                loginBtn.setEnabled(false);
                final Runnable reenableLogin = new Runnable() {
                    @Override
                    public void run() {
                        loginBtn.setEnabled(true);
                    }
                };
                new Handler().postDelayed(reenableLogin, 3500);

                // Show an error toast for failed login and do nothing..
                Toast loginFailedToast = new Toast(getApplicationContext());
                loginFailedToast.setText("Failed to Login to Facebook at this time. \nCheck your network and try again later.");
                loginFailedToast.setDuration(Toast.LENGTH_LONG);
                loginFailedToast.show();
            }
        });
    }
}

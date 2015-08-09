package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.AccountWrapper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookLoginActivity extends Activity {

    public static final String TAG = FacebookLoginActivity.class.getSimpleName();
    private CallbackManager mLoginCallbackMgr;

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
        mLoginCallbackMgr = CallbackManager.Factory.create();

        // Permission to read user's friends who have the application installed.
        loginBtn.setReadPermissions(AccountWrapper.PERMISSION_READ_FRIENDS);
        loginBtn.registerCallback(mLoginCallbackMgr, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess - Facebook Login success");
                // Login success - Start/show the main menu now.
                final Intent startMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginCallbackMgr.onActivityResult(requestCode, resultCode, data);
    }
}

package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.AccountWrapper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginAndNetworkSetupActivity extends Activity {

    public static final String TAG = LoginAndNetworkSetupActivity.class.getSimpleName();
    private LoginButton mLoginBtn;
    private CallbackManager mLoginCallbackMgr;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mLoginBtn.registerCallback(null, null);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        mLoginBtn = (LoginButton) findViewById(R.id.login_button);
        mLoginCallbackMgr = CallbackManager.Factory.create();

        // Permission to read user's friends who have the application installed.
        mLoginBtn.setReadPermissions(AccountWrapper.PERMISSION_READ_FRIENDS);
        mLoginBtn.registerCallback(mLoginCallbackMgr, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess - Facebook Login success");
                mLoginBtn.setVisibility(View.INVISIBLE);
                // Login success - Start the main menu now.
                final Intent startMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(startMainMenu);
                finish();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel - Facebook Login");
                enableLoginButton(getResources().getString(R.string.facebook_login_text));
                showFailureToConnectUi();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "onError - Facebook Login, exception = " + e.getMessage());
                enableLoginButton(getResources().getString(R.string.facebook_login_text));
                showFailureToConnectUi();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableLoginButton(getResources().getString(R.string.facebook_login_in_progress_text));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginCallbackMgr.onActivityResult(requestCode, resultCode, data);
    }

    private void enableLoginButton(final String setText) {
        if(mLoginBtn != null) {
            mLoginBtn.setEnabled(true);
            if(setText != null && !setText.isEmpty()) {
                mLoginBtn.setText(setText);
            }
        }
    }

    private void disableLoginButton(final String setText) {
        if(mLoginBtn != null) {
            mLoginBtn.setEnabled(false);
            if(setText != null && !setText.isEmpty()) {
                mLoginBtn.setText(setText);
            }
        }
    }

    private void showFailureToConnectUi() {
        // Show an error toast for failed login and do nothing..
        Toast loginFailedToast = Toast.makeText(getApplicationContext(),
                "Failed to login to the game network. \nCheck your network and try again later.",
                Toast.LENGTH_LONG);
        loginFailedToast.show();
    }
}

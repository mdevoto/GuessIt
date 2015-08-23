package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.networking.interfaces.NetworkManagerInitListener;

public class LoadingGameActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        setContentView(R.layout.activity_loading_game);

        // Initialize our NetworkManager wrapper to create/manage game sessions
        NetworkManager.INSTANCE.init(this, new NetworkManagerInitListener() {
            @Override
            public void initSuccess() {
                final Intent startMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(startMainMenu);
                finish();
            }

            @Override
            public void initFailure(final String errorMsg) {
                showFailureToConnectUi(errorMsg);
                finish();
            }
        });
    }

    private void showFailureToConnectUi(final String errorMsg) {
        // Show an error toast for failed login and do nothing..
        // TODO: Show a real UI that isn't a shitty toast.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast loginFailedToast = Toast.makeText(mContext, "Failed to Init NetworkManager. Reason: " + errorMsg, Toast.LENGTH_LONG);
                loginFailedToast.show();
            }
        });
    }
}

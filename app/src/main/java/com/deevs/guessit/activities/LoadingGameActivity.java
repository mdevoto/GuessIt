package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.networking.interfaces.NetworkManagerInitListener;

public class LoadingGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_game);

        // Initialize our NetworkManager wrapper to create/manage game sessions
        NetworkManager.INSTANCE.init(this, new NetworkManagerInitListener() {
            @Override
            public void initSuccess() {
                final Intent startLobbyIntent = new Intent(getApplicationContext(), GameLobbyActivity.class);
                startActivity(startLobbyIntent);
                finish();
            }

            @Override
            public void initFailure() {
                showFailureToConnectUi();
                finish();
            }
        });
    }

    private void showFailureToConnectUi() {
        // Show an error toast for failed login and do nothing..
        Toast loginFailedToast = new Toast(getApplicationContext());
        loginFailedToast.setText("Failed to create game session. \nCheck your network and try again later.");
        loginFailedToast.setDuration(Toast.LENGTH_SHORT);
        loginFailedToast.show();
    }
}

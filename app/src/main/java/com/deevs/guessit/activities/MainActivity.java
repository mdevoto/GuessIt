package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.deevs.guessit.R;
import com.deevs.guessit.views.TypefaceTextView;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void setupClickListeners() {

        // Create a new game button.
        // TODO: Should be 'Current Game' if already in a game.
        final TypefaceTextView createGameBtn = (TypefaceTextView) findViewById(R.id.create_game);
        createGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Runnable gameLobbyRunnable = new Runnable() {
                    @Override
                    public void run() {
                        final Intent startLobbyIntent = new Intent(getApplicationContext(), GameLobby.class);
                        startActivity(startLobbyIntent);
                    }
                };
                new Handler().post(gameLobbyRunnable);
            }
        });

        // Logout button
        final TypefaceTextView logoutBtn = (TypefaceTextView) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                finish();
            }
        });
    }
}
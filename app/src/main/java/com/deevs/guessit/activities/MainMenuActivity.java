package com.deevs.guessit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.networking.interfaces.NetworkManagerInitListener;
import com.deevs.guessit.utils.Utils;
import com.deevs.guessit.views.TypefaceTextView;
import com.facebook.appevents.AppEventsLogger;

public class MainMenuActivity extends Activity {
    public static final String TAG = MainMenuActivity.class.getSimpleName();

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

        if(!NetworkManager.INSTANCE.isInitialized()) {
            updateMenuVisibilities(ViewGroup.VISIBLE, ViewGroup.GONE);
            NetworkManager.INSTANCE.init(this, new NetworkManagerInitListener() {
                @Override
                public void initSuccess() {
                    updateMenuVisibilities(ViewGroup.GONE, ViewGroup.VISIBLE);
                    return;
                }

                @Override
                public void initFailure(String errorMsg) {
                    Utils.showToast(getApplicationContext(), "Failed to setup network. Check connectivity and try again later.", Toast.LENGTH_LONG);
                    updateMenuVisibilities(ViewGroup.GONE, ViewGroup.VISIBLE);

                    // Change loading text to an error message..
                    // TODO: come up with something better than this shit.
                    ((TypefaceTextView) findViewById(R.id.main_menu_loading_text))
                            .setText(getResources().getString(R.string.menu_error_occurred));
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void updateMenuVisibilities(final int loadingLayoutVisibility, final int mainMenuVisibility) {
        final AsyncTask<Void, Void, Void> visibilityTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                findViewById(R.id.main_menu_loading_stuff).setVisibility(loadingLayoutVisibility);
                findViewById(R.id.main_menu_menu).setVisibility(mainMenuVisibility);
            }
        };
        visibilityTask.execute();
    }

    private void startNewActivityRunnable(final Class<?> className) {
        final Runnable activityRunnable = new Runnable() {
            @Override
            public void run() {
                final Intent activityIntent = new Intent(getApplicationContext(), className);
                startActivity(activityIntent);
            }
        };
        new Handler().post(activityRunnable);
    }

    private void setupClickListeners() {
        // Create a new game button.
        // TODO: Should be 'Current Game' if already in a game.
        // TODO: Create GameManager for current game state..Make him singleton
        final TypefaceTextView createGameBtn = (TypefaceTextView) findViewById(R.id.create_game);
        createGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivityRunnable(GameLobbyActivity.class);
            }
        });

        // Logout button
        final TypefaceTextView logoutBtn = (TypefaceTextView) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkManager.INSTANCE.logout();
                finish();
            }
        });

        // Logout button
        final TypefaceTextView invitesButton = (TypefaceTextView) findViewById(R.id.pending_invites);
        invitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivityRunnable(PendingInvitesActivity.class);
            }
        });
    }
}
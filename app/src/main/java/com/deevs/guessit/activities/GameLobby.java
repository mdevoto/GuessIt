package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.NetworkManager;

public class GameLobby extends Activity {

    public static final String TAG = GameLobby.class.getSimpleName();

    private RecyclerView mLobbyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);
        Log.e(TAG, "Friends list = " + NetworkManager.INSTANCE.getFriendsList());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

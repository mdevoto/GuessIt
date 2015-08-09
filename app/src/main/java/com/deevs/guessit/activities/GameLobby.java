package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.networking.interfaces.NetworkFriendRequestListener;
import com.shephertz.app42.paas.sdk.android.social.Social;

import java.util.ArrayList;

public class GameLobby extends Activity implements NetworkFriendRequestListener {

    public static final String TAG = GameLobby.class.getSimpleName();

    private RecyclerView mLobbyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_lobby_loading);

        // Initialize any singleton managers for use within the game once we are logged in..
        // Especially App42..
        NetworkManager.INSTANCE.init(getApplicationContext());
        NetworkManager.INSTANCE.getFriendsList(this);
    }

    @Override
    public void onCompleted(ArrayList<Social.Friends> friends) {
        Log.e(TAG, "Friends list request completed.");
        for(Social.Friends friend : friends) {
            Log.e(TAG, "friend name = " + friend.getName());
        }
        setContentView(R.layout.activity_game_lobby);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

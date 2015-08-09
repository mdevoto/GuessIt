package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.deevs.guessit.R;
import com.deevs.guessit.adapters.LobbyRecyclerAdapter;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.networking.interfaces.NetworkFriendRequestListener;
import com.shephertz.app42.paas.sdk.android.social.Social;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLobby extends Activity implements NetworkFriendRequestListener {

    public static final String TAG = GameLobby.class.getSimpleName();

    private RecyclerView mLobbyRecyclerView;
    private LobbyRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        mLobbyRecyclerView = (RecyclerView) findViewById(R.id.cur_lobby_list);
        mLobbyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LobbyRecyclerAdapter(new ArrayList<Social.Friends>());
        mLobbyRecyclerView.setAdapter(mAdapter);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutMgr = new LinearLayoutManager(this);
        mLobbyRecyclerView.setLayoutManager(mLayoutMgr);

        // Initialize any singleton managers for use within the game once we are logged in..
        // Especially App42..
        NetworkManager.INSTANCE.init(getApplicationContext());
        NetworkManager.INSTANCE.getFriendsList(this);
    }

    @Override
    public void onCompleted(final ArrayList<Social.Friends> friends) {
        Log.e(TAG, "Friends list request completed.");
        for(Social.Friends friend : friends) {
            Log.e(TAG, "friend name = " + friend.getName());
        }

        mAdapter.updateFriendData(friends);
        mAdapter.notifyDataSetChanged();
    }
}

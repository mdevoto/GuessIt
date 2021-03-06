package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.deevs.guessit.R;
import com.deevs.guessit.adapters.LobbyRecyclerAdapter;
import com.deevs.guessit.gameplay.GameManager;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.networking.interfaces.NetworkFriendRequestListener;
import com.deevs.guessit.views.TypefaceTextView;
import com.shephertz.app42.paas.sdk.android.social.Social;

import java.util.ArrayList;

public class GameLobbyActivity extends Activity implements NetworkFriendRequestListener {

    public static final String TAG = GameLobbyActivity.class.getSimpleName();

    private TypefaceTextView mStartButton;
    private RecyclerView mLobbyRecyclerView;
    private LobbyRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        mStartButton = (TypefaceTextView) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartGameClick();
            }
        });

        mLobbyRecyclerView = (RecyclerView) findViewById(R.id.lobby_list);
        mLobbyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Add this current user to the lobby list
        final ArrayList<String> lobbyList = new ArrayList<>();
        lobbyList.add(NetworkManager.INSTANCE.getPlayerName());

        mAdapter = new LobbyRecyclerAdapter(this, new ArrayList<Social.Friends>(), lobbyList);
        mAdapter.setIsLoadingFriends(true);
        mLobbyRecyclerView.setAdapter(mAdapter);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutMgr = new LinearLayoutManager(this);
        mLobbyRecyclerView.setLayoutManager(mLayoutMgr);

        // Initialize any singleton managers for use within the game once we are logged in..
        // Especially App42..
        NetworkManager.INSTANCE.getFriendsList(this);
    }

    /**
     * Callback currently for the results of getFriendsList in onCreate
     **/
    @Override
    public void onCompleted(final ArrayList<Social.Friends> friends) {
        Log.e(TAG, "Friends list request completed. Friend list size = " + friends.size());
        // Refresh the recycler view
        mAdapter.setIsLoadingFriends(false);
        mAdapter.refreshFriendData(friends);
    }

    /**
     * Start game logic. From the start button click
     * */
    private void handleStartGameClick() {}
}

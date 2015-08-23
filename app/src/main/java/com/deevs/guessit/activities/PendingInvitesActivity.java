package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;

import com.deevs.guessit.networking.NetworkManager;

public class PendingInvitesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager.INSTANCE.getGameInvitations();
    }
}

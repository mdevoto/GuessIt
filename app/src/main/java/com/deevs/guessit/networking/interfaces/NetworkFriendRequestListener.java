package com.deevs.guessit.networking.interfaces;

import com.shephertz.app42.paas.sdk.android.social.Social;

import java.util.ArrayList;

public interface NetworkFriendRequestListener {
    void onCompleted(final ArrayList<Social.Friends> friends);
}

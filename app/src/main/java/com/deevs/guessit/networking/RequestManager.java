package com.deevs.guessit.networking;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

public enum RequestManager {
    INSTANCE;

    public static final String GRAPH_PATH_FRIENDS_LIST = "/me/friends";

    public static final String TAG = RequestManager.class.getSimpleName();

    private RequestManager() {}

    public void requestFriendsList(final GraphRequest.GraphJSONArrayCallback callback) {
        GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), callback).executeAsync();
    }
}

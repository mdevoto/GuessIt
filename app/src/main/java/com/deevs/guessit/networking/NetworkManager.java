package com.deevs.guessit.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.deevs.guessit.networking.interfaces.NetworkFriendRequestListener;
import com.facebook.AccessToken;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.social.SocialService;

import java.util.ArrayList;

public enum NetworkManager {
    INSTANCE;
    private NetworkManager() {}

    private boolean mIsInitialized;
    private SocialService mSocialService;

    private static final String TAG = NetworkManager.class.getSimpleName();
    private static final String sApiKey = "4318a961bea828a55a22ae9d065fadfc6d33e2c28add091f3d7a16a00824b1a4";
    private static final String sSecret = "d44a9b937169ea6f04011c7cfcc159a0e511b29f3817613aec05d9209b69bd8a";

    private void checkInitialized() {
        if(!mIsInitialized) {
            throw new IllegalStateException("NetworkManager's init() must be called before any operations");
        }
    }

    /**
     * Must be called before the NetworkManager can be utilized AT ALL
     *
     * @param context: A valid application context
     * @param
     **/
    public void init(final Context context) {
        if(context != null) {
            App42API.initialize(context.getApplicationContext(), sApiKey, sSecret);
            mIsInitialized = true;

            // Build the social service after init and connect it to the Facebook credentials..
            mSocialService = App42API.buildSocialService();
            if(AccessToken.getCurrentAccessToken() != null) {
                // Connect the Facebook account with App42 for additional support.
                mSocialService.linkUserFacebookAccount(
                        AccessToken.getCurrentAccessToken().getUserId(),
                        AccessToken.getCurrentAccessToken().getToken(),
                        new App42CallBack() {
                    @Override
                    public void onSuccess(Object response) {
                        final Social social  = (Social)response;
                        Log.e(TAG, "onSuccess (account linked): result User ID = " + social.getUserName());
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e(TAG, "onException: exception = " + e.getMessage());
                    }
                });
            }
            else {
                // TODO: Handle logout/session invalid here..
                // TODO: Try to login again?
                AccessToken.refreshCurrentAccessTokenAsync();
            }
        }
    }

    /**
     * Returns the list of friends using this application, otherwise null if unavailable at the moment
     * I.e if a session is invalid we may have to login again..
     **/
    public void getFriendsList(final NetworkFriendRequestListener listener) {
        checkInitialized();
        if(listener == null) return;

        final AccessToken token = AccessToken.getCurrentAccessToken();
        final AsyncTask task = new AsyncTask<Void, Void, ArrayList<Social.Friends>>() {

            @Override
            protected ArrayList<Social.Friends> doInBackground(Void... voids) {
                if(token != null) {
                    return mSocialService.getFacebookFriendsFromLinkUser(token.getUserId()).getFriendList();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Social.Friends> friends) {
                super.onPostExecute(friends);
                listener.onCompleted(friends);
            }
        }.execute();
    }
}

package com.deevs.guessit.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.deevs.guessit.networking.interfaces.NetworkFriendRequestListener;
import com.deevs.guessit.networking.interfaces.NetworkManagerInitListener;
import com.facebook.AccessToken;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.social.SocialService;

import java.util.ArrayList;

public enum NetworkManager {
    INSTANCE;

    private boolean mIsInitialized;
    private SocialService mSocialService;
    private AccountWrapper mAccount = new AccountWrapper();

    private static final String TAG = NetworkManager.class.getSimpleName();
    private static final String sApiKey = "4318a961bea828a55a22ae9d065fadfc6d33e2c28add091f3d7a16a00824b1a4";
    private static final String sSecret = "d44a9b937169ea6f04011c7cfcc159a0e511b29f3817613aec05d9209b69bd8a";

    private void checkInitialized() {
        if(!mIsInitialized) {
            throw new IllegalStateException("NetworkManager's init() must be called before any operations");
        }
    }

    /**
     * Must be called before the NetworkManager can be utilized AT ALL. Will setup necessary services
     * to create and run the game; such as social service integration with Facebook (friends list),
     * TODO: Fill in other services such as timers, game events, messaging, etc..
     *
     * @param context: A valid application context
     * @param initListener: listener called when this network manager is ready and init finished.
     **/
    public void init(final Context context, final NetworkManagerInitListener initListener) {
        if(context != null && mAccount.isLoggedIn()) {
            App42API.initialize(context.getApplicationContext(), sApiKey, sSecret);

            // Build the social service after init and connect it to the Facebook credentials..
            mSocialService = App42API.buildSocialService();

            final AccessToken token = mAccount.getAccessToken();
            // Connect the Facebook account with App42 for additional support functionality
            // and wrapping of trivial tasks like getting friends' list..
            mSocialService.linkUserFacebookAccount(token.getUserId(), token.getToken(), new App42CallBack() {
                @Override
                public void onSuccess(Object response) {
                    final Social social  = (Social)response;
                    Log.e(TAG, "onSuccess (account linked): result User ID = " + social.getUserName());
                    mIsInitialized = true;
                    initListener.initSuccess();
                }

                @Override
                public void onException(Exception e) {
                    Log.e(TAG, "onException: exception = " + e.getMessage());
                    initListener.initFailure();
                }
            });
        }
        else {
            // TODO: Make a login attept using the account, in the listener for that, try and linkUserFacebookAccount again
            // TODO: Then callback the initListener
        }
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    /**
     * Returns the list of friends using this application, otherwise null if unavailable at the moment
     * I.e if a session is invalid we may have to login again..
     *
     * @param listener The listener callback to call when the friend request is completed
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

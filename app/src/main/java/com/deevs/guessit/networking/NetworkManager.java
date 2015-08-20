package com.deevs.guessit.networking;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.deevs.guessit.networking.interfaces.NetworkFriendRequestListener;
import com.deevs.guessit.networking.interfaces.NetworkManagerInitListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.message.Queue;
import com.shephertz.app42.paas.sdk.android.message.QueueService;
import com.shephertz.app42.paas.sdk.android.social.Social;
import com.shephertz.app42.paas.sdk.android.social.SocialService;

import java.util.ArrayList;
import java.util.List;

public enum NetworkManager {
    INSTANCE;

    private Context mContext;

    private String mPlayerName;
    private boolean mIsInitialized;
    private SocialService mSocialService;

    private Queue mInvitationQueue;
    private QueueService mQueueService;
    private String mInvitationQueueName;

    private static final String TAG = NetworkManager.class.getSimpleName();
    private static final String sApiKey = "4318a961bea828a55a22ae9d065fadfc6d33e2c28add091f3d7a16a00824b1a4";
    private static final String sSecret = "d44a9b937169ea6f04011c7cfcc159a0e511b29f3817613aec05d9209b69bd8a";

    private static final String sInvitationQueueDesc = "Queue utilized for invitations";

    public static final String PERMISSION_READ_FRIENDS = "user_friends";

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
        if(context != null && isLoggedIn()) {
            mContext = context.getApplicationContext();
            doInitialization(initListener);
        }
        else {
            // TODO: Make a login attept using the account, in the listener for that, try and linkUserFacebookAccount again
            // TODO: Then callback the initListener
            // // todo - or throw an error?
        }
    }

    private void doInitialization(final NetworkManagerInitListener initListener) {
        App42API.initialize(mContext.getApplicationContext(), sApiKey, sSecret);

        // Build the social service after init and connect it to the Facebook credentials..
        mSocialService = App42API.buildSocialService();

        final AccessToken token = getAccessToken();
        // Connect the Facebook account with App42 for additional support functionality
        // and wrapping of trivial tasks like getting friends' list..
        mSocialService.linkUserFacebookAccount(token.getUserId(), token.getToken(), new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                final Social social = (Social) response;
                mPlayerName = social.getFacebookProfile().getName();
                Log.e(TAG, "onSuccess (account linked): result User ID = " + social.getUserName());

                // Build the QueueService used for listening for game invitations, etc.
                mQueueService = App42API.buildQueueService();

                // Build an invitation room name using player name and their unique ID
                // TODO: Check that User ID and friend ID are unique AND matching..or this is fucked.
                mInvitationQueueName = new StringBuilder().append(mPlayerName)
                        .append(getAccessToken().getUserId()).toString().replaceAll("\\s","");

                mQueueService.createPullQueue(mInvitationQueueName, sInvitationQueueDesc,
                        new App42CallBack() {
                            @Override
                            public void onSuccess(Object response) {
                                mInvitationQueue = (Queue) response;
                                Log.e(TAG, "queueName is " + mInvitationQueue.getQueueName());
                                Log.e(TAG, "queueType is " + mInvitationQueue.getQueueType());
                                Log.e(TAG, "queueDescription is " + mInvitationQueue.getDescription());

                                mIsInitialized = true;
                                initListener.initSuccess();
                            }

                            @Override
                            public void onException(Exception e) {
                                Log.e(TAG, "onException (createPullQueue): exception = " + e.getMessage());
                                initFailed(initListener);
                            }
                        });
            }

            @Override
            public void onException(Exception e) {
                Log.e(TAG, "onException (linkFacebookAccount): exception = " + e.getMessage());
                initFailed(initListener);
            }
        });
    }

    private void initFailed(final NetworkManagerInitListener initListener) {
        if(initListener != null) {
            initListener.initFailure();
        }
        mIsInitialized = false;
    }

    public boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void login(final Activity ctxActivity, final FacebookCallback<LoginResult> callback) {
        // Register here for the login callback using the passed in callback..
        final CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);

        // Perform the login call
        List<String> permissions = new ArrayList<>();
        permissions.add(PERMISSION_READ_FRIENDS);
        LoginManager.getInstance().logInWithReadPermissions(ctxActivity, permissions);
    }

    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public String getUsername() {
        return mPlayerName;
    }

    public void logout() {
        if(isLoggedIn()) {
            LoginManager.getInstance().logOut();
        }
    }

    /**
     * Messages related to the invitation queue
     **/

    public void getPendingMessages(final App42CallBack callback) {
        //todo
    }
}

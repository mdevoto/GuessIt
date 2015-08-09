package com.deevs.guessit.networking;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.List;

public class AccountWrapper {

    public static final String TAG = AccountWrapper.class.getSimpleName();

    public static final String PERMISSION_READ_FRIENDS = "user_friends";

    public AccountWrapper() {}

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

    public void logout() {
        if(isLoggedIn()) {
            LoginManager.getInstance().logOut();
        }
    }

    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }
}

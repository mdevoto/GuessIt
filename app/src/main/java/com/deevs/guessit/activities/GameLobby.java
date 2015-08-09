package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.deevs.guessit.networking.RequestManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;

public class GameLobby extends Activity {

    public static final String TAG = GameLobby.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateFriendsList();
    }

    private void updateFriendsList() {
        RequestManager.INSTANCE.requestFriendsList(
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                        System.out.println("jsonArray: " + jsonArray);
                        System.out.println("GraphResponse: " + response);
                        try {
                            Log.e(TAG, "Printing friends list: ");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.e(TAG, "Friend i: " + i + " = " + jsonArray.get(i).toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

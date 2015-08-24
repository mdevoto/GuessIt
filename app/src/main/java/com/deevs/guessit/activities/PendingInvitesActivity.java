package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.deevs.guessit.networking.NetworkManager;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.message.Queue;

import java.util.ArrayList;
import java.util.HashSet;

public class PendingInvitesActivity extends Activity {

    public static final String TAG = PendingInvitesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager.INSTANCE.getGameInvitations(new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                Queue queue  = (Queue)response;
                Log.e(TAG, "queueName is " + queue.getQueueName());
                Log.e(TAG, "queueType is " + queue.getQueueType());

                // Create a unique hash-set which removes duplicates
                HashSet<Queue.Message> messageList = new HashSet<Queue.Message>(queue.getMessageList());
                for(Queue.Message message : messageList)
                {
                    Log.e(TAG, "correlationId is " + message.getCorrelationId());
                    Log.e(TAG, "messageId is " + message.getMessageId());
                    Log.e(TAG, "payLoad is " + message.getPayLoad());
                }
            }

            @Override
            public void onException(Exception e) {
                Log.e(TAG, "Exception Message" + e.getMessage());
            }
        });
    }
}

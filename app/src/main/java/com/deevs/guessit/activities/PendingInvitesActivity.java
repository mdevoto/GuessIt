package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.deevs.guessit.networking.NetworkManager;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.message.Queue;

import java.util.ArrayList;

public class PendingInvitesActivity extends Activity {

    public static final String TAG = PendingInvitesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager.INSTANCE.getGameInvitations(new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                Queue queue  = (Queue)response;
                System.out.println("queueName is " + queue.getQueueName());
                System.out.println("queueType is " + queue.getQueueType());
                ArrayList<Queue.Message> messageList = queue.getMessageList();
                for(Queue.Message message : messageList)
                {
                    System.out.println("correlationId is " + message.getCorrelationId());
                    System.out.println("messageId is " + message.getMessageId());
                    System.out.println("payLoad is " + message.getPayLoad());
                }
            }

            @Override
            public void onException(Exception e) {
                Log.e(TAG, "Exception Message" + e.getMessage());
            }
        });
    }
}

package com.deevs.guessit.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.deevs.guessit.R;
import com.deevs.guessit.adapters.PendingInvitesRecycclerAdapter;
import com.deevs.guessit.networking.NetworkManager;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42NotFoundException;
import com.shephertz.app42.paas.sdk.android.message.Queue;

import java.util.ArrayList;

public class PendingInvitesActivity extends Activity {

    public static final String TAG = PendingInvitesActivity.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutMgr;
    private RecyclerView mInviteRecycler;
    private PendingInvitesRecycclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_invites);

        // Setup the Recycler list for game invites once loading is done
        mInviteRecycler = (RecyclerView) findViewById(R.id.pending_invite_list);
        mInviteRecycler.setItemAnimator(new DefaultItemAnimator());

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutMgr = new LinearLayoutManager(this);
        mInviteRecycler.setLayoutManager(mLayoutMgr);

        NetworkManager.INSTANCE.getGameInvitations(new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                Queue queue  = (Queue)response;

                mAdapter = new PendingInvitesRecycclerAdapter(getApplicationContext(), queue.getMessageList());
                mInviteRecycler.setAdapter(mAdapter);

                // todo: make this an animation..like fade?
                // todo: Run on ui thread safe?
                setLoadingAndMenuVisibilities(ViewGroup.GONE, ViewGroup.VISIBLE);

                Log.e(TAG, "queueName is " + queue.getQueueName());
                Log.e(TAG, "queueType is " + queue.getQueueType());

                // Create a unique hash-set which removes duplicates
                ArrayList<Queue.Message> messageList = new ArrayList<>(queue.getMessageList());
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

                // If this exception is an empty message queue exception we can finish loading
                // and just show an empty list.
                if(e instanceof App42NotFoundException
                        && ((App42NotFoundException) e).getAppErrorCode() == NetworkManager.INSTANCE.QUEUE_NOT_FOUND_PENDING_EMPTY) {
                    setLoadingAndMenuVisibilities(ViewGroup.GONE, ViewGroup.VISIBLE);
                }
            }
        });
    }

    private void setLoadingAndMenuVisibilities(final int loadingVis, final int contentVis) {
        final AsyncTask<Void, Void, Void> visTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            // Here the code runs on the UI thread..do visibility from here..
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                findViewById(R.id.pending_invite_loading).setVisibility(loadingVis);
                findViewById(R.id.pending_invite_content).setVisibility(contentVis);
            }
        };
        visTask.execute();
    }
}

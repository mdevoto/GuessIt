package com.deevs.guessit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deevs.guessit.R;
import com.deevs.guessit.networking.NetworkManager;
import com.deevs.guessit.views.TypefaceTextView;
import com.shephertz.app42.paas.sdk.android.message.Queue;
import com.shephertz.app42.paas.sdk.android.social.Social;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Provide views to RecyclerView with data from mFriendList.
 */
public class PendingInvitesRecycclerAdapter extends RecyclerView.Adapter<PendingInvitesRecycclerAdapter.ViewHolder> {
    private static final String TAG = PendingInvitesRecycclerAdapter.class.getSimpleName();

    private static final int VIEWTYPE_HEADER        = 0;
    private static final int VIEWTYPE_ITEM_INVITE   = 1;

    private static final int HEADER_COUNT           = 1;

    private Context mContext;
    private ArrayList<Queue.Message> mInviteList;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TypefaceTextView mText              = null;
        private TypefaceTextView mAcceptButton      = null;
        private TypefaceTextView mDeclineButton     = null;

        public ViewHolder(View v, final int viewType) {
            super(v);
            switch(viewType) {
                case VIEWTYPE_HEADER:
                    mText = (TypefaceTextView) v.findViewById(R.id.pending_invite_header);
                    break;
                case VIEWTYPE_ITEM_INVITE:
                    mText = (TypefaceTextView) v.findViewById(R.id.invite_friend_name);
                    mAcceptButton = (TypefaceTextView) v.findViewById(R.id.accept_invite_button);
                    mDeclineButton = (TypefaceTextView) v.findViewById(R.id.decline_invite_button);
                    break;
            }
        }

        public TypefaceTextView getText() {
            return mText;
        }

        public TypefaceTextView getDeclineButton() {
            return mDeclineButton;
        }

        public TypefaceTextView getAcceptButton() {
            return mAcceptButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param messageList HashSet containing the data to populate views to be used by RecyclerView.
     */
    public PendingInvitesRecycclerAdapter(final Context context, final ArrayList<Queue.Message> messageList) {
        mInviteList = messageList;
        mContext = context.getApplicationContext();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new ViewHolder depending on type
        int layoutId = -1;
        switch(viewType) {
            case VIEWTYPE_HEADER:
                layoutId = R.layout.invite_row_header;
                break;
            case VIEWTYPE_ITEM_INVITE:
                layoutId = R.layout.invite_row_pendinginvite;
                break;
        }

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new ViewHolder(v, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEWTYPE_HEADER;
        } else {
            return VIEWTYPE_ITEM_INVITE;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your data set at this position and replace the contents of the view
        // with that element.
        if(position == 0) {
            viewHolder.getText().setText(mContext.getString(R.string.invitations_header));
        } else {
            viewHolder.getText().setText(mInviteList.get(position - (mLobbyList.size() + HEADER_COUNT)).getName());
            // Set the tag to the friend's invite room name
            Queue.Message invite= mInviteList.get(position - getHeaderCount());
            viewHolder.getInviteButton().setTag(NetworkManager.INSTANCE.getInviteQueueName(friend.getName(), friend.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return mInviteList.size() + 1;
    }

    public int getHeaderCount() {
        return HEADER_COUNT;
    }
}

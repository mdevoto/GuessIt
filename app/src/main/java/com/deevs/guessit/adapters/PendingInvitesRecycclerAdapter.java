package com.deevs.guessit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deevs.guessit.R;
import com.deevs.guessit.views.TypefaceTextView;
import com.shephertz.app42.paas.sdk.android.message.Queue;

import java.util.ArrayList;

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

        public ViewHolder(View v) {
            super(v);

            mText = (TypefaceTextView) v.findViewById(R.id.invite_friend_name);
            mAcceptButton = (TypefaceTextView) v.findViewById(R.id.accept_invite_button);
            mAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo - extract channel name from payload message
                    // todo - accept invitation and join/subscribe to channel name
                }
            });

            mDeclineButton = (TypefaceTextView) v.findViewById(R.id.decline_invite_button);
            mDeclineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo - remove this invitation from the invite queue name
                    // todo - remove from the recycler view list..
                }
            });
        }

        public TypefaceTextView getText() {
            return mText;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invite_row_pendinginvite, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your data set at this position and replace the contents of the view
        // with that element.
        viewHolder.getText().setText(mInviteList.get(position - 1).getPayLoad());
    }

    @Override
    public int getItemCount() {
        return mInviteList.size() + 1;
    }
}

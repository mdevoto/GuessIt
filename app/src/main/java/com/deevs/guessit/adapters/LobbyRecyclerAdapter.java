package com.deevs.guessit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deevs.guessit.R;
import com.deevs.guessit.views.TypefaceTextView;
import com.shephertz.app42.paas.sdk.android.social.Social;
import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mFriendList.
 */
public class LobbyRecyclerAdapter extends RecyclerView.Adapter<LobbyRecyclerAdapter.ViewHolder> {
    private static final String TAG = LobbyRecyclerAdapter.class.getSimpleName();

    private static final int VIEWTYPE_HEADER                = 0;
    private static final int VIEWTYPE_EMPTY_FRIENDS_LIST    = 1;
    private static final int VIEWTYPE_ITEM_FRIEND_INVITE    = 2;
    private static final int VIEWTYPE_ITEM_FRIEND_LOBBY     = 3;

    private static final int HEADER_COUNT = 2;

    private Context mContext;
    private boolean mLoadingFriends;
    private ArrayList<Social.Friends> mFriendList;
    private ArrayList<String> mLobbyList;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TypefaceTextView mText    = null;
        private TypefaceTextView mInviteButton = null;

        public ViewHolder(View v) {
            super(v);
            switch((Integer) v.getTag()) {
                case VIEWTYPE_HEADER:
                    mText = (TypefaceTextView) v.findViewById(R.id.header_text);
                    break;
                case VIEWTYPE_ITEM_FRIEND_INVITE:
                    mText = (TypefaceTextView) v.findViewById(R.id.friend_name);
                    mInviteButton = (TypefaceTextView) v.findViewById(R.id.invite_button);
                    mInviteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // todo: add invite logic here and remove from list.
                            Log.e(TAG, "onClick the invite button.");
                        }
                    });
                    break;
                case VIEWTYPE_ITEM_FRIEND_LOBBY:
                    mText = (TypefaceTextView) v.findViewById(R.id.friend_name);
                    break;
                case VIEWTYPE_EMPTY_FRIENDS_LIST:
                    mText = (TypefaceTextView) v.findViewById(R.id.empty_msg);
                    break;
            }
        }

        public TypefaceTextView getText() {
            return mText;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param friendDataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public LobbyRecyclerAdapter(final Context context, final ArrayList<Social.Friends> friendDataSet,
                                final ArrayList<String> lobbyData) {
        mContext = context.getApplicationContext();
        mFriendList = friendDataSet;
        mLobbyList = lobbyData;
    }

    public void setIsLoadingFriends(boolean loading) {
        mLoadingFriends = loading;
    }

    public void refreshFriendData(ArrayList<Social.Friends> friends) {
        this.mFriendList.clear();
        this.mFriendList.addAll(friends);
        notifyDataSetChanged();
    }

    public void refreshLobbyData(ArrayList<String> lobbyFriends) {
        this.mLobbyList.clear();
        this.mLobbyList.addAll(lobbyFriends);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new ViewHolder depending on type
        int layoutId = -1;
        switch(viewType) {
            case VIEWTYPE_HEADER:
                layoutId = R.layout.lobby_row_header;
                break;
            case VIEWTYPE_ITEM_FRIEND_INVITE:
                layoutId = R.layout.lobby_row_friend_invite;
                break;
            case VIEWTYPE_ITEM_FRIEND_LOBBY:
                layoutId = R.layout.lobby_row_friend;
                break;
            case VIEWTYPE_EMPTY_FRIENDS_LIST:
                layoutId = R.layout.lobby_row_friend_empty;
                break;
        }

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        v.setTag(viewType);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == mLobbyList.size() + 1) {
            return VIEWTYPE_HEADER;
        } else if(position <= mLobbyList.size()) {
            return VIEWTYPE_ITEM_FRIEND_LOBBY;
        } else if(mFriendList.isEmpty()) {
            return VIEWTYPE_EMPTY_FRIENDS_LIST;
        } else {
            return VIEWTYPE_ITEM_FRIEND_INVITE;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your data set at this position and replace the contents of the view
        // with that element.
        if(position == 0 || position == mLobbyList.size() + 1) {
            viewHolder.getText().setText(mContext.getString(position == 0 ? R.string.header_lobby : R.string.header_friends));
        } else if(position <= mLobbyList.size()) {
            viewHolder.getText().setText(mLobbyList.get(position-1));
        } else if(mFriendList.isEmpty()) {
            // Either we're empty on friends, or we are currently loading them..show text
            viewHolder.getText().setText(mContext.getString(mLoadingFriends ? R.string.item_loading_friends : R.string.item_no_friends));
        } else {
            viewHolder.getText().setText(mFriendList.get(position - (mLobbyList.size() + HEADER_COUNT)).getName());
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // 2 Headers are always visible..
        final int ONE_EMPTY_VIEW_COUNT  = 1;
        return (mFriendList.isEmpty() ?
                (HEADER_COUNT + ONE_EMPTY_VIEW_COUNT + mLobbyList.size())
                : (HEADER_COUNT + mFriendList.size() + mLobbyList.size())
        );
    }
}

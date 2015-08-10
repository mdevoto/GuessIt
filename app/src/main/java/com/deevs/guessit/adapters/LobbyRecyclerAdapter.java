package com.deevs.guessit.adapters;
/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
 * Provide views to RecyclerView with data from mFriendData.
 */
public class LobbyRecyclerAdapter extends RecyclerView.Adapter<LobbyRecyclerAdapter.ViewHolder> {
    private static final String TAG = LobbyRecyclerAdapter.class.getSimpleName();

    private ArrayList<Social.Friends> mFriendData;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TypefaceTextView mUsernameView;

        public ViewHolder(View v) {
            super(v);
            mUsernameView = (TypefaceTextView) v.findViewById(R.id.username);
        }

        public TypefaceTextView getUsernameView() {
            return mUsernameView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public LobbyRecyclerAdapter(final ArrayList<Social.Friends> dataSet) {
        mFriendData = dataSet;
    }

    public void updateFriendData(ArrayList<Social.Friends> friends) {
        this.mFriendData.clear();
        this.mFriendData.addAll(friends);
        notifyDataSetChanged();
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lobby_row_item, viewGroup, false);
        return new ViewHolder(v);
    }

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.e(TAG, "onBindViewHolder");
        // Get element from your dataset at this position and replace the contents of the view
        // with that element.
        viewHolder.getUsernameView().setText(mFriendData.get(position).getName());
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFriendData.size();
    }
}

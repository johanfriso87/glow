package com.glowteam.Ui.Fragments.ReviewsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.SubscribeCreatorsAdapter;
import com.glowteam.Adapters.VideoAdapter;
import com.glowteam.Models.User;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.BaseFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubscriptionFragment extends BaseFragment {

    RecyclerView mAllViews;
    SubscribeCreatorsAdapter yourReviewAdapter;
    GridLayoutManager linearLayoutManager;
    private ArrayList<User> users  = new ArrayList<>();;
    private LinearLayout no_review;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_product, container, false);
        mAllViews = view.findViewById(R.id.allViews);
        no_review = view.findViewById(R.id.no_review);
        linearLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        mAllViews.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onResume() {
        getrSubscribedUse();
        super.onResume();
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        users.clear();
        if (type == ApiCall.GET_SUBSCRIBED_USERS) {
            try {
                JSONArray jArray = o.getJSONArray("data");

                Gson gson = new Gson();
                for (int i = 0; i < jArray.length(); i++) {
                    User user = gson.fromJson(jArray.getJSONObject(i).toString(), User.class);
                    users.add(user);
                }
                if (users.size() > 0) {
                    no_review.setVisibility(View.GONE);
                    mAllViews.setVisibility(View.VISIBLE);
                    yourReviewAdapter = new SubscribeCreatorsAdapter(getActivity(), users);
                    mAllViews.setAdapter(yourReviewAdapter);

                } else {
                    no_review.setVisibility(View.VISIBLE);
                    mAllViews.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

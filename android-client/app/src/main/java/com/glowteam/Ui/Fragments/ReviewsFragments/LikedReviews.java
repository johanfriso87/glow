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

import com.glowteam.Adapters.VideoAdapter;
import com.glowteam.Adapters.YourReviewAdapter;
import com.glowteam.Constant.Constant;
import com.glowteam.Interfaces.Interface;
import com.glowteam.Models.UserId;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.BaseFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LikedReviews extends BaseFragment {


    RecyclerView mAllViews;
    YourReviewAdapter yourReviewAdapter;
    GridLayoutManager linearLayoutManager;
    private ArrayList<Video> videos = new ArrayList<>();
    private LinearLayout no_review;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);
        mAllViews = view.findViewById(R.id.allViews);
        no_review = view.findViewById(R.id.no_review);
        linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);

        mAllViews.setLayoutManager(linearLayoutManager);

        return view;


    }

    @Override
    public void onResume() {
        getUserLikedVideos();
        super.onResume();
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        videos.clear();
        if (type == ApiCall.GET_USER_LIKED_VIDEOS) {
            try {
                JSONArray jArray = o.getJSONArray("data");

                Gson gson = new Gson();
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray.get(i) != null) {
                        Video video = gson.fromJson(jArray.getJSONObject(i).toString(), Video.class);
                        videos.add(video);
                    }
                }
                if (videos.size() > 0) {
                    no_review.setVisibility(View.GONE);
                    mAllViews.setVisibility(View.VISIBLE);
                    yourReviewAdapter = new YourReviewAdapter(getActivity(), videos,getMainUser());
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

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

import com.glowteam.Adapters.YourReviewAdapter;
import com.glowteam.Constant.Constant;
import com.glowteam.Interfaces.Interface;
import com.glowteam.Models.UserId;
import com.glowteam.Models.Video;
import com.glowteam.Models.VideoData;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.BaseFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YourReviewsFragment extends BaseFragment {


    RecyclerView mAllViews;
    YourReviewAdapter yourReviewAdapter;
    GridLayoutManager linearLayoutManager;
    private ArrayList<Video> videos = new ArrayList<>();
    private LinearLayout no_review;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);
        getUserVideos();
        mAllViews = view.findViewById(R.id.allViews);
        no_review = view.findViewById(R.id.no_review);
        linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        mAllViews.setLayoutManager(linearLayoutManager);
        Constant.costant = YourReviewsFragment.this;
        Interface.SetOnProductAdded1(onProductAdded1);
        return view;
    }

    private Interface.OnProductAdded1 onProductAdded1 = this::getUserVideos;

    public void getAllVideo() {
        getUserVideos();
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        if (type == ApiCall.GET_USER_VIDEOS) {
            try {
                videos.clear();
                JSONArray jArray = o.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++) {
//                    Video video = new Gson().fromJson(jArray.getJSONObject(i).toString(), Video.class);
                    Video video = new Video();
                    video.setAsin(jArray.getJSONObject(i).getString("asin"));
                    video.setId(jArray.getJSONObject(i).getString("_id"));
                    video.setProductName(jArray.getJSONObject(i).getString("productName"));
                    video.setProductThumbnailUrl(jArray.getJSONObject(i).getString("productThumbnailUrl"));
                    video.setProductUrl(jArray.getJSONObject(i).getString("productUrl"));
                    video.setThumbnailUrl(jArray.getJSONObject(i).getString("thumbnailUrl"));
                    video.setV(jArray.getJSONObject(i).getInt("__v"));
                    video.setVideoUrl(jArray.getJSONObject(i).getString("videoUrl"));
                    video.setGif(jArray.getJSONObject(i).getString("gif"));
                    video.setViews(jArray.getJSONObject(i).getInt("views"));
                    UserId userId = new UserId();
                    userId.setId(jArray.getJSONObject(i).getString("userId"));
                    userId.setName(getMainUser().getName());
                    userId.setProfilePic(getMainUser().getProfilePic());
                    video.setUserId(userId);
                    videos.add(video);
                }
                if (videos.size() > 0) {
                    no_review.setVisibility(View.GONE);
                    mAllViews.setVisibility(View.VISIBLE);
                    yourReviewAdapter = new YourReviewAdapter(getActivity(), videos, getMainUser());
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

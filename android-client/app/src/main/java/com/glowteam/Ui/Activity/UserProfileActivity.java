package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Adapters.VideoAdapter;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.User;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.glowteam.Constant.Constant.BASE_URL;

public class UserProfileActivity extends BaseActivity {

    CircularImageView mImg;
    ImageView mSubscribe, mBack, mShare;
    CustomTextView mName, mAbout;
    RecyclerView mAllViews;
    VideoAdapter yourReviewAdapter;
    GridLayoutManager linearLayoutManager;
    private ArrayList<Video> videos = new ArrayList<>();
    ;
    private LinearLayout no_review;
    private String userId;
    private User userData;
    private boolean isSubscribe = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userId = getIntent().getStringExtra("userId");
        isSubscribe = getIntent().getBooleanExtra("isSubscribed", false);

        mImg = findViewById(R.id.img);
        mBack = findViewById(R.id.back);
        mShare = findViewById(R.id.share);
        mShare.setVisibility(View.GONE);
        mSubscribe = findViewById(R.id.subscribe);
        mName = findViewById(R.id.name);
        mAbout = findViewById(R.id.about);
        mAllViews = findViewById(R.id.videosView);
        no_review = findViewById(R.id.no_review);
        linearLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mAllViews.setLayoutManager(linearLayoutManager);

        if (isSubscribe) {
            mSubscribe.setImageResource(R.drawable.tick);
        }

        mBack.setOnClickListener(v -> {
            Intent i = new Intent();
            i.putExtra("isSubscribed" , isSubscribe);
            setResult(RESULT_OK,i);
            finish();
        });
        mSubscribe.setOnClickListener(v -> {
            if (isSubscribe) {
                unSubscribeUser(userId);
            } else {
                subscribeUser(userId);
            }
        });

        getOtherUserProfile(userId);
    }

    private void setData() {
        if( userData.getProfilePic() != null && userData.getProfilePic().trim() != ""){
            Glide.with(this).load(BASE_URL + userData.getProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(mImg);
        }else{
            Glide.with(this).load(R.drawable.people)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(mImg);
        }

        mName.setText(userData.getUserName());
        mAbout.setText(userData.getName());
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.GET_OTHER_USER_PROFILE) {
                JSONObject object = o.getJSONObject("data");
                userData = new Gson().fromJson(object.toString(), User.class);
                setData();
                getOtherUserVideos(userId);
            }
            if (type == ApiCall.GET_OTHER_USER_VIDEOS) {
                videos.clear();
                JSONArray jArray = o.getJSONArray("data");

                Gson gson = new Gson();
                for (int i = 0; i < jArray.length(); i++) {
                    Video video = gson.fromJson(jArray.getJSONObject(i).toString(), Video.class);
                    videos.add(video);
                }
                if (videos.size() > 0) {
                    no_review.setVisibility(View.GONE);
                    mAllViews.setVisibility(View.VISIBLE);
                    yourReviewAdapter = new VideoAdapter(this, videos);
                    mAllViews.setAdapter(yourReviewAdapter);

                } else {
                    no_review.setVisibility(View.VISIBLE);
                    mAllViews.setVisibility(View.GONE);
                }
            }

            if (type == ApiCall.SUBSCRIBE_USER) {
                isSubscribe = true;
                mSubscribe.setImageResource(R.drawable.tick);
            }
            if (type == ApiCall.UNSUBSCRIBE_USER) {
                isSubscribe = false;
                mSubscribe.setImageResource(R.drawable.add);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("isSubscribed" , isSubscribe);
        setResult(RESULT_OK,i);
        finish();
    }
}

package com.glowteam.Ui.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.potyvideo.library.AndExoPlayerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.CustomViews.CustomViewPager;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.VideoLikeCommentsFragment;
import com.glowteam.Ui.Fragments.VideoPlayFragment;
import com.potyvideo.library.AndExoPlayerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.glowteam.Constant.Constant.BASE_URL;


public class VideoPlayActivity extends AppCompatActivity{

    ArrayList<Video> videoArrayList = new ArrayList<>();
        ViewPager viewPager;
    ScreenSlidePagerAdapter pagerAdapter;
    int startingPos = 0;
//    private VideoView videoview;
//    private VideoLikeCommentsFragment videoLikeCommentsFragment;
//    ImageView mBack;
//    CircleImageView mImg;
//    CustomTextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoArrayList = (ArrayList<Video>) getIntent().getSerializableExtra("videoData");
        startingPos = getIntent().getIntExtra("pos", 0);

//        videoview = findViewById(R.id.videoview);
//        mBack = findViewById(R.id.back);
        viewPager = findViewById(R.id.viewPager);
//        mImg = findViewById(R.id.img);
//        mName = findViewById(R.id.name);
//        videoLikeCommentsFragment = VideoLikeCommentsFragment.newInstance(videoArrayList.get(startingPos));
//        videoview.setVideoPath(Constant.BASE_URL + videoArrayList.get(startingPos).getVideoUrl());
//        videoview.requestFocus();
//        videoview.setOnPreparedListener(mp -> videoview.start());
//        if (!videoLikeCommentsFragment.isAdded()) {
//            videoLikeCommentsFragment.show(getSupportFragmentManager(), videoLikeCommentsFragment.getTag());
//        }

//        Glide.with(this).load(BASE_URL + videoArrayList.get(startingPos).getUserId().getProfilePic())
//                .diskCacheStrategy(DiskCacheStrategy.ALL).into(mImg);
//        if (!TextUtils.isEmpty(videoArrayList.get(startingPos).getUserId().getName())) {
//            mName.setText(videoArrayList.get(startingPos).getUserId().getName());
//        } else {
//            mName.setText("Unknown");
//        }
//
//        mBack.setOnClickListener(v -> {
//            finish();
//        });

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(startingPos);

//        viewPager.arrowScroll(2);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }




    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return new VideoPlayFragment(position, videoArrayList,viewPager);
        }

        @Override
        public int getCount() {
            return videoArrayList.size();
        }

    }
}


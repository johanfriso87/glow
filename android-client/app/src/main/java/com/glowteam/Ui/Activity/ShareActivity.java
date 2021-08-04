  package com.glowteam.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.CustomViews.ViewP;
import com.glowteam.Interfaces.Interface;
import com.glowteam.R;

import com.glowteam.CustomViews.CustomViewPager;
import com.glowteam.Adapters.ShareAdapter;
import com.glowteam.Ui.Fragments.BottomSheetDialogInstagram;
import com.potyvideo.library.AndExoPlayerView;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.glowteam.Constant.Constant.BASE_URL;

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private ViewP viewPager;
    private TextView done;
    private LinearLayout shareBtn;
    private TextView shareTxt;
    private LinearLayout link;
    private LinearLayout save, saveSticker;
    private LinearLayout more;
    private String path;
    int temp = 0;
//    String[] sharearray = new String[]{"Share on Your Story", "Share on Instagram", "Share on Snapchat", "Share on Message", "Share on Line", "Share on Facebook", "Share on Twitter"};
//    int[] shareIcon = new int[]{R.drawable.iv_instagram, R.drawable.iv_instagram, R.drawable.iv_snapchat,
//            R.drawable.iv_message, R.drawable.ic_line_app_logo, R.drawable.iv_facebook, R.drawable.ic_twitter};

//    int[] shareFrame = new int[]{R.drawable.iv_insta_story_4, R.drawable.iv_instagram_frame1, R.drawable.iv_snapchat_frame1,
//            R.drawable.iv_message_frame, R.drawable.iv_line_frame, R.drawable.iv_facebook_frame1, R.drawable.ic_twitter_frame2};

    int[] shareFrame = new int[]{R.drawable.ic_instagram_story_f, R.drawable.ic_instagram_feed_f, R.drawable.iv_snapchat_frame1,
            R.drawable.ic_message_f, R.drawable.iv_line_frame, R.drawable.ic_facebook_f, R.drawable.ic_twitter_f};

    int[] shareButton = new int[]{R.drawable.btn_instagram, R.drawable.btn_instagram_feed, R.drawable.btn_snapchat,
            R.drawable.btn_whatsapp, R.drawable.btn_line, R.drawable.btn_facebook, R.drawable.btn_twitter};
    //    int newWidth = 828, newHeight = 1100;
//    int newWidth = 450, newHeight = 777;

    // original
//    int newWidth = 225, newHeight = 388;
//    int marginBottom = 35;
//    int firstHeight = 270, secondHeight = 290, thirdHeight = 270;

    //Second
    int newWidth = 181, newHeight = 290;
    int marginBottom = 15;
    int firstMarginTop = 9;
//    int firstHeight = 378, secondHeight = 372, thirdHeight = 270;
    int firstHeight = 420, secondHeight = 290, thirdHeight = 270;

//    int newWidth = 195, newHeight = 388;
//    int marginBottom = 15;
//    int firstMarginTop = 10;
//    int firstHeight = 270, secondHeight = 290, thirdHeight = 270;

    //    int firstHeight = 183, secondHeight = 233;
    //    int firstHeight = 550,secondHeight= 750;
    boolean isOver = true, isFromVideo = false, isShare = false;
    VideoView videoView;
    //    AndExoPlayerView videoView;
    //    UniversalVideoView videoView;
    ShareAdapter shareAdapter;
    CustomTextView progress_count;
    RelativeLayout mProgressView;
    TextRoundCornerProgressBar textRoundCornerProgressBar;
    String stickerUrl;
    CustomTextView mTextTitle;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Call callbackManager.onActivityResult to pass login result to the LoginManager via callbackManager.
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setImageHeight(int height){
        Log.e("layout height height",String.valueOf(height));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
        Log.e("videoview height",String.valueOf(layoutParams.height));
        Log.e("layout height height",String.valueOf(height));
        Log.e("videoview height",String.valueOf(layoutParams.height));

        this.firstHeight = height;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.sns_color));
        }
        setContentView(R.layout.activity_share);
        path = getIntent().getStringExtra("path");
        stickerUrl = getIntent().getStringExtra("stickerUrl");
//        path = "http://52.66.242.48:3333/videos/KsOagC_8P_video_1596761713925.mp4";
        initView();
        initListener();
        initAdapter();

        videoView = findViewById(R.id.videoView);
        mProgressView = findViewById(R.id.progressView);
        textRoundCornerProgressBar = findViewById(R.id.progress_bar);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, callback);
        mProgressView.setOnClickListener(v -> {

        });

        newWidth = (int) convertDpToPixel(newWidth);
        newHeight = (int) convertDpToPixel(newHeight);

        marginBottom = (int) convertDpToPixel(marginBottom);
        firstMarginTop = (int) convertDpToPixel(firstMarginTop);

        firstHeight = (int) convertDpToPixel(firstHeight);
        secondHeight = (int) convertDpToPixel(secondHeight);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//        layoutParams.width = newWidth;
//        layoutParams.height = layoutParams.height;
//        layoutParams.topMargin = firstMarginTop;
//        videoView.setLayoutParams(layoutParams);

        videoView.setVideoPath(path);

        videoView.requestFocus();
        videoView.start();

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.requestFocus();
            videoView.start();
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                if (i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                    //first frame was bufered - do your stuff here
//                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                    Log.e("test",String.valueOf(mediaPlayer.getVideoHeight()));
//                    firstHeight = mediaPlayer.getVideoHeight()-20;
//                    newHeight = mediaPlayer.getVideoHeight()*290/420;
                }
                return false;
            }
        });
    }

    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.e("success", "Successfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.e("error", "Cancel occurred");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("fb Error", error.getMessage());
            Log.e("fb localize error Error", error.getLocalizedMessage());

            Toast.makeText(ShareActivity.this, "App is under Development.", Toast.LENGTH_SHORT).show();
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };

    public float convertDpToPixel(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public float convertPixelsToDp(float px) {
        return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private boolean mIsRight = true;
    private float lastOffset = 0;
    private boolean scrollStarted, checkDirection;
    public int currentHeight;
    private void initAdapter() {
        shareAdapter = new ShareAdapter(ShareActivity.this, path, shareFrame);
        viewPager.setAdapter(shareAdapter);
        viewPager.setOffscreenPageLimit(0);
//        viewPager.setPageTransformer(true,new PageTransformer(),View.LAYER_TYPE_NONE);
//        Log.e("width",String.valueOf(shareAdapter.layout.getLayoutParams().width));
//        Log.e("height",String.valueOf(shareAdapter.layout.getLayoutParams().height));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //                Log.e("positionOffset : ", String.valueOf(positionOffset));
//                Log.e("positionOffsetPixels : ", String.valueOf(positionOffsetPixels));
                isOver = false;
                mIsRight = viewPager.isSwipeRight;
//                Log.e("swiper direction right",String.valueOf(viewPager.isSwipeRight) );
//                Log.e("swiper direction left",String.valueOf(!viewPager.isSwipeRight) );
//                Log.e("position : ", String.valueOf(position));
//                Log.e("position 1:1 ", String.valueOf(position));
//                Log.e("temp 1:1 ", String.valueOf(temp));


                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        /*
                        * positionOffset => get offset how much user has swipe
                        *                   problem is when user leave half way swipe than we did not get possition offset it is 0
                        *                   so it can't be zoom out.
                        *                   so i set default height on page selected call back (onPageSelected)
                        *                   this is the issue.
                        * */

                        //                Log.e("positionOffset : ", String.valueOf(positionOffset));
//                Log.e("positionOffsetPixels : ", String.valueOf(positionOffsetPixels));
                        isOver = false;
                        mIsRight = viewPager.isSwipeRight;

//                        Log.e("position : ", String.valueOf(position));
//                        Log.e("position : ", String.valueOf(positionOffset));
//                        Log.e("mIsRight : ", String.valueOf(mIsRight));


                        if (positionOffset != 0) {
                            if (temp == 0) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
                                }
                            } else if (temp == 1) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{

                                    Log.e("position : ", String.valueOf(position));
                                    Log.e("position offset: ", String.valueOf(positionOffset));
                                    Log.e("mIsRight : ", String.valueOf(mIsRight));
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
//                                    if(positionOffset > 0.5){
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
//                                        layoutParams.height = (int) (firstHeight - minH);
//                                        currentHeight = (int) (firstHeight - minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }else{
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
//                                        layoutParams.height = (int) (currentHeight + minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }
                                }
                            } else if (temp == 2) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{

                                    Log.e("position : ", String.valueOf(position));
                                    Log.e("position offset: ", String.valueOf(positionOffset));
                                    Log.e("mIsRight : ", String.valueOf(mIsRight));
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
//                                    if(positionOffset > 0.5){
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
//                                        layoutParams.height = (int) (firstHeight - minH);
//                                        currentHeight = (int) (firstHeight - minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }else{
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
//                                        layoutParams.height = (int) (currentHeight + minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }
                                }
                            } else if (temp == 3) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{

                                    Log.e("position : ", String.valueOf(position));
                                    Log.e("position offset: ", String.valueOf(positionOffset));
                                    Log.e("mIsRight : ", String.valueOf(mIsRight));
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
//                                    if(positionOffset > 0.5){
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
//                                        layoutParams.height = (int) (firstHeight - minH);
//                                        currentHeight = (int) (firstHeight - minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }else{
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
//                                        layoutParams.height = (int) (currentHeight + minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }
                                }
                            } else if (temp == 4) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{

                                    Log.e("position : ", String.valueOf(position));
                                    Log.e("position offset: ", String.valueOf(positionOffset));
                                    Log.e("mIsRight : ", String.valueOf(mIsRight));
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
//                                    if(positionOffset > 0.5){
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
//                                        layoutParams.height = (int) (firstHeight - minH);
//                                        currentHeight = (int) (firstHeight - minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }else{
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
//                                        layoutParams.height = (int) (currentHeight + minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }
                                }
                            } else if (temp == 5) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{

                                    Log.e("position : ", String.valueOf(position));
                                    Log.e("position offset: ", String.valueOf(positionOffset));
                                    Log.e("mIsRight : ", String.valueOf(mIsRight));
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
//                                    if(positionOffset > 0.5){
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
//                                        layoutParams.height = (int) (firstHeight - minH);
//                                        currentHeight = (int) (firstHeight - minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }else{
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
//                                        layoutParams.height = (int) (currentHeight + minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }
                                }
                            } else if (temp == 6) {
                                if (mIsRight) {
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }

                                }else{

                                    Log.e("position : ", String.valueOf(position));
                                    Log.e("position offset: ", String.valueOf(positionOffset));
                                    Log.e("mIsRight : ", String.valueOf(mIsRight));
                                    if(positionOffset < 0.5){
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
                                        layoutParams.height = (int) (firstHeight - minH);
                                        currentHeight = (int) (firstHeight - minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }else{
                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                                        int minW = (int) ((newWidth - 800) * positionOffset);
                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
                                        layoutParams.height = (int) (currentHeight + minH);
                                        videoView.setLayoutParams(layoutParams);
                                    }
//                                    if(positionOffset > 0.5){
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * positionOffset);
//                                        layoutParams.height = (int) (firstHeight - minH);
//                                        currentHeight = (int) (firstHeight - minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }else{
//                                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
//                                        int minW = (int) ((newWidth - 800) * positionOffset);
//                                        int minH = (int) (( firstHeight - newHeight) * (positionOffset-0.5));
//                                        layoutParams.height = (int) (currentHeight + minH);
//                                        videoView.setLayoutParams(layoutParams);
//                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
//                isOver = true;
//                if (scrollState == 2) {
                        shareTxt.setText("Share Video");
                        shareBtn.setBackground(ContextCompat.getDrawable(ShareActivity.this, shareButton[position]));
//                    temp = position;
//                }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        Log.e("onPageScroll : ", String.valueOf(state));
                        if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                            scrollStarted = true;
                            checkDirection = true;
                        } else {
                            scrollStarted = false;
                        }
                        if (state == ViewPager.SCROLL_STATE_IDLE) {
                            temp = viewPager.getCurrentItem();
                        }
                    }
                });
            }


            @Override
            public void onPageSelected(int position) {
//                isOver = true;
//                if (scrollState == 2) {
//                shareTxt.setText(sharearray[position]);
//                shareBtn.setBackground(ContextCompat.getDrawable(ShareActivity.this, shareButton[position]));
//                setName(position);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                layoutParams.height = firstHeight;
                videoView.setLayoutParams(layoutParams);
//                    temp = position;
//                }
                setName(position);

                // this function is called when page selected
                // if i remove it then video size stay small => a issue that you mention.
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onPageScroll : ", String.valueOf(state));
                if(state == ViewPager.SCROLL_STATE_SETTLING){

                }
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
                if (state == ViewPager.SCROLL_STATE_IDLE) {

                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
                    layoutParams.height = firstHeight;
                    videoView.setLayoutParams(layoutParams);
                    temp = viewPager.getCurrentItem();
                }
            }
        });
    }

    private void setName(int no) {
        switch (no) {
            case 0:
                mTextTitle.setText("Instagram Stories");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case 1:
                mTextTitle.setText("Instagram");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case 2:
                mTextTitle.setText("Snapchat");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case 3:
                mTextTitle.setText("Message");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case 4:
                mTextTitle.setText("Line");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case 5:
                mTextTitle.setText("Facebook");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
            case 6:
                mTextTitle.setText("Twitter");
                shareTxt.setBackgroundResource(0);
                shareTxt.setText("Share Video");
                shareTxt.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;
        }

    }

    private void initListener() {
        done.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        link.setOnClickListener(this);
        save.setOnClickListener(this);
        more.setOnClickListener(this);
        saveSticker.setOnClickListener(this);

    }

    private void initView() {
        mTextTitle = findViewById(R.id.textTitle);
        viewPager = findViewById(R.id.viewPager);
        done = findViewById(R.id.done);
        shareBtn = findViewById(R.id.sharebBtn);
        shareTxt = findViewById(R.id.shareTxt);
        link = findViewById(R.id.link);
        save = findViewById(R.id.save);
        more = findViewById(R.id.more);
        saveSticker = findViewById(R.id.saveSticker);
        progress_count = findViewById(R.id.progress_count);
//        shareTxt.setText(sharearray[0]);
        setName(0);
    }

    public class PageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {

            if (position < -1) {

            } else if (position <= 1) {
                // [-1,1]: the page is "centered"
            } else {
                mIsRight = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                if (Interface.mOnProductAdded != null) {
                    Interface.mOnProductAdded.OnProductAdded();
                }
                finish();
                break;
            case R.id.sharebBtn:
//                mProgressView.setVisibility(View.VISIBLE);
//                textRoundCornerProgressBar.setMax(100);
//                textRoundCornerProgressBar.setProgress(0);
//                int p = 0;
//                do {
//                    textRoundCornerProgressBar.setProgress(p++);
//                } while (p != 100);
//
//                CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
//                    @Override
//                    public void onTick(long l) {
//                        long j = 3000 - (l / 1000);
//                        float p = j * 100 / l;
//                        textRoundCornerProgressBar.setProgress(p);
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        mProgressView.setVisibility(View.GONE);
//                        share();
//                    }
//                };
//                countDownTimer.start();
                isShare = true;
                isFromVideo = true;
                if (isPermissionGranted()) {
                    if (isValidURL(path)) {
                        downloadVideoOrImage(path, true);
                    } else {
//                        showToast("Saved");
                        share(new File(path));
                    }
                }
                break;
            case R.id.link:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Revie Link", path);
                clipboard.setPrimaryClip(clip);
                showToast("Revideo vidoe link copied!");
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
//                startActivity(browserIntent);

                break;
            case R.id.save:
                isShare = false;
                isFromVideo = true;
                if (isPermissionGranted()) {
                    if (isValidURL(path)) {
                        downloadVideoOrImage(path, true);
                    } else {
                        showToast("Saved");
                    }
                }
                break;
            case R.id.saveSticker:
                isShare = false;
                isFromVideo = false;
                if (isPermissionGranted()) {
                    if (isValidURL(path)) {
                        getProductSticker(stickerUrl);
                    } else {
                        downloadVideoOrImage(stickerUrl, false);
                    }
                }
                break;
            case R.id.more:
                Intent shareIntent = new Intent();
                shareIntent.setAction("android.intent.action.SEND");
                shareIntent.setType("video/*");
//                String extraText = getResources().getString(R.string.app_name) + " Created By :";
//                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                if (isValidURL(path)) {
                    shareIntent.putExtra(Intent.EXTRA_TEXT, path + "\n");
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, path + "\n" + extraText + "\n" + url);
                } else {
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, extraText + "\n" + url);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(path));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                startActivity(Intent.createChooser(shareIntent, "Share with..."));
                break;
        }
    }

    private void customeShgaring(File file) {
        Uri stickerAssetUri = Uri.fromFile(file);
//        String attributionLinkUrl = "https://www.my-aweseome-app.com/p/BhzbIOUBval/";
//        String sourceApplication = "com.example.myapplication";

// Instantiate implicit intent with ADD_TO_STORY action,
// sticker asset, background colors, and attribution link
        Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
        intent.putExtra("source_application", getPackageName());

//        intent.setType(MEDIA_TYPE_JPEG);
        intent.setType("video/*");
//        intent.putExtra("interactive_asset_uri", stickerAssetUri);
//        intent.setDataAndType(stickerAssetUri, "video/*");
        intent.putExtra(Intent.EXTRA_STREAM, stickerAssetUri);
//        intent.putExtra("content_url", attributionLinkUrl);
//        intent.putExtra("top_background_color", "#33FF33");
//        intent.putExtra("bottom_background_color", "#FF00FF");

// Instantiate activity and verify it will resolve implicit intent
        Activity activity = this;
        activity.grantUriPermission(
                "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0);
        }
    }
    public void customeSharingToFeed() {
        Uri stickerAssetUri = Uri.fromFile(this.file);
//        String attributionLinkUrl = "https://www.my-aweseome-app.com/p/BhzbIOUBval/";
//        String sourceApplication = "com.example.myapplication";

// Instantiate implicit intent with ADD_TO_STORY action,
// sticker asset, background colors, and attribution link
        Intent intent = new Intent("com.instagram.share.ADD_TO_FEED");
        intent.putExtra("source_application", getPackageName());

//        intent.setType(MEDIA_TYPE_JPEG);
        intent.setType("video/*");
//        intent.putExtra("interactive_asset_uri", stickerAssetUri);
        intent.putExtra(Intent.EXTRA_STREAM, stickerAssetUri);


//        intent.putExtra("content_url", attributionLinkUrl);
//        intent.putExtra("top_background_color", "#33FF33");
//        intent.putExtra("bottom_background_color", "#FF00FF");

// Instantiate activity and verify it will resolve implicit intent
        Activity activity = this;
        activity.grantUriPermission(
                "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0);
        }
    }

    public void customeSharingToStory() {
        Uri stickerAssetUri = Uri.fromFile(this.file);
        Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");

        intent.setDataAndType(stickerAssetUri, "video/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

// Instantiate activity and verify it will resolve implicit intent
        Activity activity = this;
//        Activity activity = getActivity();
        if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0);
        }
    }

    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    public File file;
    public void share(File file) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent shareIntent1 = new Intent();
        shareIntent1.setAction("android.intent.action.SEND");
        shareIntent1.setType("video/*");
        shareIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String extraText1 = getResources().getString(R.string.app_name) + " Created By :";
        String url1 = "https://play.google.com/store/apps/details?id=" + getPackageName();
        shareIntent1.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
//        if (isValidURL(path)) {
//            shareIntent1.putExtra(Intent.EXTRA_TEXT, path + "\n" + extraText1 + "\n" + url1);
//        } else {
        shareIntent1.putExtra(Intent.EXTRA_TEXT, extraText1 + "\n" + url1);
        Uri uri1 = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        shareIntent1.putExtra(Intent.EXTRA_STREAM, uri1);
//        }
        switch (temp) {
            case 0:
                this.file = file;


                customeSharingToStory();
                break;
            case 1:
                this.file = file;
                BottomSheetDialogInstagram bottomSheet = new BottomSheetDialogInstagram();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                break;
            case 2:
                shareIntent1.setPackage("com.snapchat.android");
                try {
                    startActivity(shareIntent1);
                } catch (ActivityNotFoundException e2) {
                    Toast.makeText(this, "Snapchat not installed !!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.putExtra("sms_body", path + "\n" + extraText1 + "\n" + url1);
                Uri data = Uri.parse("sms:");
                intent.setData(data);
                startActivity(intent);
//                        shareIntent1.setPackage("jp.naver.line.android");
//                        try {
//                            startActivity(shareIntent1);
//                        } catch (ActivityNotFoundException e2) {
//                            Toast.makeText(this, "Line not installed !!!", Toast.LENGTH_SHORT).show();
//                        }
                break;
            case 4:
                shareIntent1.setPackage("jp.naver.line.android");
                try {
                    startActivity(shareIntent1);
                } catch (ActivityNotFoundException e2) {
                    Toast.makeText(this, "Line is not installed !!!", Toast.LENGTH_SHORT).show();
                }


//                openLine(path + "\n" + extraText1 + "\n" + url1);
                break;
            case 5:
//                shareIntent1.setPackage("com.facebook.katana");
//                try {
//                    startActivity(shareIntent1);
//                } catch (ActivityNotFoundException e2) {
//                    Toast.makeText(this, "Facebook not installed !!!", Toast.LENGTH_SHORT).show();
//                }
                try {
                    ShareVideo video = new ShareVideo.Builder()
                            .setLocalUrl(uri1)
                            .build();
                    ShareVideoContent content = new ShareVideoContent.Builder()
                            .setVideo(video)
                            .build();
                    shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                    Toast.makeText(this, "blah", Toast.LENGTH_SHORT).show();
                } catch (Exception e3) {
                    Toast.makeText(this, e3.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
            case 6:
                PackageManager packManager = getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(shareIntent1, PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for (ResolveInfo resolveInfo : resolvedInfoList) {
                    if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                        shareIntent1.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name);
                        resolved = true;
                        break;
                    }
                }
                if (resolved) {
                    startActivity(shareIntent1);
                } else {
                    Toast.makeText(this, "Twitter not installed !!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.GET_PRODUCT_STICKER) {
                String productSticker = o.getString("data");
                downloadVideoOrImage(Constant.BASE_URL + productSticker, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPermissionGranted() {
        int write_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (read_storage == PERMISSION_GRANTED && write_storage == PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length >= 2 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
                if (isFromVideo) {
                    if (isValidURL(path)) {
                        downloadVideoOrImage(path, true);
                    } else {
                        showToast("Saved");
                    }
                } else {
                    if (isValidURL(path)) {
                        getProductSticker(stickerUrl);
                    } else {
                        downloadVideoOrImage(stickerUrl, false);
                    }
                }
            }
        }
    }

    private boolean isValidURL(String urlString) {
        return Patterns.WEB_URL.matcher(urlString.toLowerCase()).matches();
    }

    private void createInstagramIntent(String mediaPath) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("video/*");
        File media = new File(mediaPath);
//        Uri imageUri = FileProvider.getUriForFile(
//                ShareActivity.this,
//                "com.glowteam.provider", //(use your app signature + ".provider" )
//                media);
        Uri uri = Uri.fromFile(media);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share to"));
    }

    private void shareTwitter(String message, File file) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("video/*");
//        tweetIntent.setType("text/plain");

        Uri uri1 = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        tweetIntent.putExtra(Intent.EXTRA_STREAM, uri1);

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            tweetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(tweetIntent);
        } else {
//            Intent i = new Intent();
//            i.putExtra(Intent.EXTRA_TEXT, message);
//            i.setAction(Intent.ACTION_VIEW);
//            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            i.putExtra(Intent.EXTRA_STREAM, uri1);
//            i.setDataAndType(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)),"video/*");
//            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    public void openLine(String msg) {
        try {
            final String appName = "jp.naver.line.android";

            final boolean isAppInstalled = isPackageInstalled(appName, getPackageManager());

            if (isAppInstalled) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("line://msg/text/" + msg));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Line not Installed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ERROR LINE", e.toString());
            Toast.makeText(this, "Line not installed in your device", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        if (Interface.mOnProductAdded != null) {
            Interface.mOnProductAdded.OnProductAdded();
        }
        super.onBackPressed();
    }

    private File getOutputFile() {

        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        File file = new File(temp, "Glow");
        File file = new File(temp, "Glow/Video");

        if (!file.exists()) {
            file.mkdirs();
//            file = new File(temp, "Video");
//            if (!file.exists()) {
//                file.mkdirs();
//            }
        }

        return new File(file.getPath() + File.separator + "VID_" + System.currentTimeMillis() + ".mp4");
    }

    private File getOutputFileSticker() {

        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(temp, "Glow/Stickers");
        if (!file.exists()) {
            file.mkdirs();
//            file = new File(temp, "");
//            if (!file.exists()) {
//                file.mkdirs();
//            }
        }

        return new File(file.getPath() + File.separator + "Sticker_" + System.currentTimeMillis() + ".png");
    }

    private void downloadVideoOrImage(String url, boolean isVideo) {
//        progress_count.setVisibility(View.VISIBLE);
//        showProgressDialog();
        mProgressView.setVisibility(View.VISIBLE);
        textRoundCornerProgressBar.setMax(100);
        textRoundCornerProgressBar.setProgress(0);
        new Thread(() -> {
            URL u = null;
            InputStream is = null;

            try {
                u = new URL(url);
                is = u.openStream();
                HttpURLConnection huc = (HttpURLConnection) u.openConnection(); //to know the size of video
                int size = huc.getContentLength();
                File file;
                if (isVideo) {
                    file = getOutputFile();
                } else {
                    file = getOutputFileSticker();
                }
//                    String fileName = "FILE.mp4";
//                    String storagePath = Environment.getExternalStorageDirectory().toString();
//                    File f = new File(storagePath, fileName);

                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                int per = 0;
                if (is != null) {
                    while ((len1 = is.read(buffer)) > 0) {
                        total += len1;
                        if (size > 0) // only if total length is known
                            per = (int) (total * 100 / size);
                        int finalPer = per;
//                        runOnUiThread(() -> progress_count.setText(finalPer + "%"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textRoundCornerProgressBar.setProgress(finalPer);
                            }
                        });
                        fos.write(buffer, 0, len1);
                    }
                }
                runOnUiThread(() -> {
//                    dismissProgressDialog();
//                    progress_count.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.GONE);
                    if (isVideo) {
                        showToast("Video Saved");
                    } else {
                        showToast("Sticker Saved");
                    }
                    if (isShare) {
                        share(file);
                    }
                });
                fos.close();
            } catch (IOException mue) {
                runOnUiThread(() -> {
//                    dismissProgressDialog();
//                    progress_count.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.GONE);
                    showToast("Failed!");
                });
                mue.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ioe) {
                    // just going to ignore this one
                }
            }
        }).start();
    }
}

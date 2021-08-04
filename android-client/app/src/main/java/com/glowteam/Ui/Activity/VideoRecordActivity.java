
package com.glowteam.Ui.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CameraPreview;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.StickersItem;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.TutorialFragment;
import com.glowteam.Utils.Constants;
import com.glowteam.Utils.CountDownAnimation;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.abedelazizshe.lightcompressorlibrary.VideoCompressor.INSTANCE;
import static com.glowteam.Constant.Constant.costant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class VideoRecordActivity extends BaseActivity implements View.OnClickListener, CountDownAnimation.CountDownListener {

    private ImageView startVideoButton, normalVideoButton;
    private ImageView cameraSwitchButton;
    private ImageView flashButton, mBack;
    private TextView videoRecordingTimerTextView, mCount;
    private static final String TAG = "VideoRecordActivity";
    private boolean isCameraFlashOn = false;
    private MediaRecorder mediaRecorder;
    boolean isRecording = false;
    private int setOrientationHint;
    private boolean cameraFront = false;
    private String path;
    private ProgressBar progress;
    private Camera mCamera;
    private CameraPreview mPreview;
    private CountDownTimer count;
    private int mMod = 1;

    private LinearLayout mNormal, mHandFree, mCountdown, mFastForward ;
    private CustomTextView mNormaltext, mHandFreetext, mCountdowntext, mFastForwardtext, mProgress;
    private View v1, v2, v3, v4;
    private CountDownAnimation countDownAnimation;

    private LinearLayout mBottomView;
    private RelativeLayout mFirstView, mSecondView;
    private String fastForwardPath = "", mFinalPath;
    private Uri mFinalUri;
    private String productId;
    private String productName;
    private String productUrl;
    private String productThumbnailUrl;
    private int time = 0;
    private String productSticker;

    private RoundedHorizontalProgressBar roundCornerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        initid();
        tutorialDialog();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initid() {
        mNormal = findViewById(R.id.normal);
        mHandFree = findViewById(R.id.handsfree);
        mCountdown = findViewById(R.id.countdown);
        mFastForward = findViewById(R.id.fast_forward);
        mNormaltext = findViewById(R.id.normaltext);
        mHandFreetext = findViewById(R.id.handsfreetext);
        mCountdowntext = findViewById(R.id.countdowntext);
        mFastForwardtext = findViewById(R.id.fast_forwardtext);
        mBottomView = findViewById(R.id.bottomView);
        normalVideoButton = findViewById(R.id.start_video_button1);
        mCount = findViewById(R.id.textView);
        mFirstView = findViewById(R.id.firstView);
        mProgress = findViewById(R.id.progress_count);
        mSecondView = findViewById(R.id.secondView);
        roundCornerProgressBar = findViewById(R.id.progress_bar);
        roundCornerProgressBar.setMax(100);

        productId = getIntent().getStringExtra("productId");
        productName = getIntent().getStringExtra("productName");
        productUrl = getIntent().getStringExtra("productUrl");
        productThumbnailUrl = getIntent().getStringExtra("productThumbnailUrl");
        Log.d("Data", "onCreate: ====>" + productId + " " + productName + " " + productThumbnailUrl + " " + productUrl);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);

        startVideoButton = findViewById(R.id.start_video_button);
        startVideoButton.setOnClickListener(this);
        normalVideoButton.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isRecording) {
                        if (prepareVideoRecorder()) {
                            normalVideoButton.setColorFilter(ContextCompat.getColor(this, R.color.purple));
                            increaseAnimation(normalVideoButton);
//                            normalVideoButton.setImageDrawable(ContextCompat.getDrawable(VideoRecordActivity.this, R.drawable.play));
                            mediaRecorder.start();
                            isRecording = true;
                            initTimer();
                        } else {
                            releaseMediaRecorder();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isRecording) {
                        normalVideoButton.setColorFilter(ContextCompat.getColor(this, R.color.white));
                        decreaseAnimation(normalVideoButton);
                        try {
                            mediaRecorder.stop();
                        } catch (RuntimeException stopException) {
                            // handle cleanup here
                        }
                        count.cancel();
                        count = null;
                        videoRecordingTimerTextView.setVisibility(View.GONE);
                        releaseMediaRecorder();
                        isRecording = false;
                        initDialog();
                    }
                    break;
            }
            return true;
        });

        cameraSwitchButton = findViewById(R.id.camera_switchbutton);
        cameraSwitchButton.setOnClickListener(this);
        mBack = findViewById(R.id.back);
        flashButton = findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        videoRecordingTimerTextView = findViewById(R.id.timer_textview);
        mCamera = getCameraInstance();
//        int cameraId = findBackFacingCamera();
//        int o = getVideoOrientationAngle(this,cameraId);
//        setCameraDisplayOrientation(this,cameraId,mCamera);
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // Get the pointer ID
//                mCamera = Camera.open();
                Camera.Parameters params = mCamera.getParameters();
                int action = event.getAction();


                if (event.getPointerCount() > 1) {
                    // handle multi-touch events
                    if (action == MotionEvent.ACTION_POINTER_DOWN) {
                        mDist = getFingerSpacing(event);
                    } else if (action == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                        mCamera.cancelAutoFocus();
                        handleZoom(event, params);
                    }
                } else {
                    // handle single touch events
                    if (action == MotionEvent.ACTION_UP) {
                        handleFocus(event, params);
                    }
                }
                return true;
            }
        });

        mBack.setOnClickListener(this);

        initCountDownAnimation();

        mNormal.setOnClickListener(view -> setMode(1));
        mHandFree.setOnClickListener(view -> setMode(2));
        mCountdown.setOnClickListener(view -> setMode(3));
        mFastForward.setOnClickListener(view -> setMode(4));
        setMode(1);
    }

    private void increaseAnimation(View v) {
//        ValueAnimator va = ValueAnimator.ofFloat(getResources().getDimension(R.dimen._60sdp), getResources().getDimension(R.dimen._70sdp));
//        va.setDuration(400);
//        va.addUpdateListener(animation -> {
//            Integer value = (Integer) animation.getAnimatedValue();
//            v.getLayoutParams().width = value.intValue();
//            v.requestLayout();
//        });
//        ValueAnimator va1 = ValueAnimator.ofFloat(getResources().getDimension(R.dimen._60sdp), getResources().getDimension(R.dimen._70sdp));
//        va1.setDuration(400);
//        va1.addUpdateListener(animation -> {
//            Integer value = (Integer) animation.getAnimatedValue();
//            v.getLayoutParams().height = value.intValue();
//            v.requestLayout();
//        });
//        va.start();
//        va1.start();
        Animation anim = new ScaleAnimation(
                1f, 1.2f, // Start and end values for the X axis scaling
                1f, 1.2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        v.startAnimation(anim);
        hideAllButton();
    }

    private void decreaseAnimation(View v) {
//        ValueAnimator va = ValueAnimator.ofFloat(getResources().getDimension(R.dimen._70sdp), getResources().getDimension(R.dimen._60sdp));
//        va.setDuration(400);
//        va.addUpdateListener(animation -> {
//            Integer value = (Integer) animation.getAnimatedValue();
//            v.getLayoutParams().width = value.intValue();
//            v.requestLayout();
//        });
//        ValueAnimator va1 = ValueAnimator.ofFloat(getResources().getDimension(R.dimen._70sdp), getResources().getDimension(R.dimen._60sdp));
//        va1.setDuration(400);
//        va1.addUpdateListener(animation -> {
//            Integer value = (Integer) animation.getAnimatedValue();
//            v.getLayoutParams().height = value.intValue();
//            v.requestLayout();
//        });
//        va.start();
//        va1.start();
        Animation anim = new ScaleAnimation(
                1.2f, 1f, // Start and end values for the X axis scaling
                1.2f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.6f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.6f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        v.startAnimation(anim);
        showAllButton();
    }


    private void setMode(int mod) {
        mMod = mod;
        mNormaltext.setTextColor(ContextCompat.getColor(this, R.color.white));
        v1.setVisibility(View.GONE);
        mHandFreetext.setTextColor(ContextCompat.getColor(this, R.color.white));
        v2.setVisibility(View.GONE);
        mCountdowntext.setTextColor(ContextCompat.getColor(this, R.color.white));
        v3.setVisibility(View.GONE);
        mFastForwardtext.setTextColor(ContextCompat.getColor(this, R.color.white));
        v4.setVisibility(View.GONE);

        switch (mod) {
            case 1:
                mNormaltext.setTextColor(ContextCompat.getColor(this, R.color.purple));
                mSecondView.setVisibility(View.GONE);
                mFirstView.setVisibility(View.VISIBLE);
                v1.setVisibility(View.VISIBLE);
                break;

            case 2:
                mSecondView.setVisibility(View.VISIBLE);
                mFirstView.setVisibility(View.GONE);
                mHandFreetext.setTextColor(ContextCompat.getColor(this, R.color.purple));
                startVideoButton.setImageResource(R.drawable.stop_button);
                v2.setVisibility(View.VISIBLE);
                break;

            case 3:
                mSecondView.setVisibility(View.VISIBLE);
                mFirstView.setVisibility(View.GONE);
                mCountdowntext.setTextColor(ContextCompat.getColor(this, R.color.purple));
                startVideoButton.setImageResource(R.drawable.start_countdown);
                v3.setVisibility(View.VISIBLE);
                break;

            case 4:
                mSecondView.setVisibility(View.VISIBLE);
                mFirstView.setVisibility(View.GONE);
                mFastForwardtext.setTextColor(ContextCompat.getColor(this, R.color.purple));
                startVideoButton.setImageResource(R.drawable.start_fast_forward);
                v4.setVisibility(View.VISIBLE);
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_video_button:
                if (mMod == 3 && !isRecording) {
                    startCountDownAnimation();
                } else {
                    if (isRecording) {
                        startVideoButton.setColorFilter(ContextCompat.getColor(this, R.color.white));
                        decreaseAnimation(startVideoButton);
                        try {
                            mediaRecorder.stop();
                        } catch (RuntimeException stopException) {
                            // handle cleanup here
                        }
                        count.cancel();
                        count = null;
                        videoRecordingTimerTextView.setVisibility(View.GONE);
                        releaseMediaRecorder();
                        isRecording = false;
                        initDialog();
                    } else {
                        if (prepareVideoRecorder()) {
                            startVideoButton.setColorFilter(ContextCompat.getColor(this, R.color.purple));
                            increaseAnimation(startVideoButton);
                            mediaRecorder.start();
                            isRecording = true;
                            initTimer();
                        /*startVideoButton.setOnClickListener(null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startVideoButton.setOnClickListener(VideoRecordActivity.this);
                            }
                        }, 3000);*/
                        } else {
                            releaseMediaRecorder();
                        }
                    }
                }
                break;
            case R.id.flash_button:
                if (isFlashAvailable(this)) {
                    if (isCameraFlashOn) {
                        isCameraFlashOn = false;
                        setFlashOnOff(false);
                        flashButton.setImageDrawable(ContextCompat.getDrawable(VideoRecordActivity.this, R.drawable.flash_off));
                    } else {
                        setFlashOnOff(true);
                        isCameraFlashOn = true;
                        flashButton.setImageDrawable(ContextCompat.getDrawable(VideoRecordActivity.this, R.drawable.flash_on));
                    }
                } else {
                    Log.d("flash not available", "flash not here");
                }
                break;
            case R.id.camera_switchbutton:
                switchCamera();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void initTimer() {
        time = 0;
        videoRecordingTimerTextView.setVisibility(View.VISIBLE);
        count = new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
                videoRecordingTimerTextView.setText((millisUntilFinished / 1000) + " Second Left");
                time += 1;
                int p = (100 * time) / 300;
                if (p != 0) {
                    roundCornerProgressBar.animateProgress(500, p);
                }
            }

            public void onFinish() {
                isRecording = false;
                if (mMod == 1) {
                    normalVideoButton.setColorFilter(ContextCompat.getColor(VideoRecordActivity.this, R.color.purple));
                } else {
                    startVideoButton.setColorFilter(ContextCompat.getColor(VideoRecordActivity.this, R.color.purple));
                }
                decreaseAnimation(startVideoButton);
                try {
                    mediaRecorder.stop();
                } catch (RuntimeException stopException) {
                }
                releaseMediaRecorder();
                videoRecordingTimerTextView.setVisibility(View.GONE);
                initDialog();
            }

        }.start();
    }

    private void initCountDownAnimation() {
        countDownAnimation = new CountDownAnimation(mCount, 3);
        countDownAnimation.setCountDownListener(this);
    }

    private void startCountDownAnimation() {

        startVideoButton.setEnabled(false);
        hideAllButton();
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        countDownAnimation.setAnimation(animationSet);
        countDownAnimation.setStartCount(3);
        countDownAnimation.start();
    }

    private void hideAllButton() {
        mBottomView.setVisibility(View.INVISIBLE);
        mBack.setVisibility(View.GONE);
        flashButton.setVisibility(View.GONE);
        cameraSwitchButton.setVisibility(View.GONE);
    }

    private void showAllButton() {
        mBottomView.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        flashButton.setVisibility(View.VISIBLE);
        cameraSwitchButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCountDownEnd(CountDownAnimation animation) {
        startVideoButton.setEnabled(true);
        if (prepareVideoRecorder()) {
            increaseAnimation(startVideoButton);
            startVideoButton.setColorFilter(ContextCompat.getColor(this, R.color.purple));
            mediaRecorder.start();
            isRecording = true;
            initTimer();
        } else {
            releaseMediaRecorder();
        }
    }

    private void initDialog() {

        if (time >= 10) {
            MediaScannerConnection.scanFile(this,
                    new String[]{path}, null,
                    (path, uri) -> runOnUiThread(() -> new AlertDialog.Builder(VideoRecordActivity.this)
                            .setTitle("Upload Video")
                            .setMessage("Are you sure you want to upload this Video?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                if (isFlashAvailable(this)) {
                                    if (isCameraFlashOn) {
                                        isCameraFlashOn = false;
                                        setFlashOnOff(false);
                                        flashButton.setImageDrawable(ContextCompat.getDrawable(VideoRecordActivity.this, R.drawable.flash_off));
                                    }
                                }
                                hideAllButton();
                                mFinalPath = path;
                                mFinalUri = uri;

                                if (mMod == 4) {
                                    showProgressDialog();
                                    String imageFileName = "VID_" + System.currentTimeMillis() + ".mp4";
                                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                                    File file;
                                    try {
                                        file = File.createTempFile(imageFileName, Constants.VIDEO_FORMAT, storageDir);
                                        fastForwardPath = file.getAbsolutePath();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mProgress.setVisibility(View.VISIBLE);
                                    mProgress.setText("0%");
                                    EpEditor.changePTS(path, fastForwardPath, 2.0f, EpEditor.PTS.ALL, new OnEditorListener() {
                                        @Override
                                        public void onSuccess() {
                                            getProductSticker(productThumbnailUrl);
                                        }

                                        @Override
                                        public void onFailure() {
                                            runOnUiThread(() -> {
                                                dismissProgressDialog();
                                                mProgress.setVisibility(View.GONE);
                                                showToast("Please try again later!");
                                            });
                                        }

                                        @Override
                                        public void onProgress(float progress) {
                                            int p = (int) (progress * 100);
                                            if (p > 100) {
                                                p = 100;
                                            }
                                            int finalP = p;
                                            runOnUiThread(() -> mProgress.setText(finalP + "%"));
                                        }
                                    });
                                } else {
                                    getProductSticker(productThumbnailUrl);
//                                    startActivity(new Intent(VideoRecordActivity.this, ShareActivity.class).
//                                            putExtra("path", path));
                                }
                            })
                            .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                            .show()));
        } else {
            showToast("Please make sure that video is atleast 10 seconds long!");
        }

    }

    public String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null,
                null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor
                        .getColumnIndex(MediaStore.Video.VideoColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {

                result = "";
            }
            cursor.close();
        }
        return result;
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.UPLOAD_VIDEO) {
                if (costant != null) {
                    costant.getAllVideo();
                }
                finish();
            }
            if (type == ApiCall.GET_PRODUCT_STICKER) {
                productSticker = o.getString("data");
                getStickers();
            }
            if (type == ApiCall.GET_STICKERS) {
                ArrayList<StickersItem> stickersItems = new ArrayList<>();
                JSONArray array = o.getJSONArray("data");
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    StickersItem item = gson.fromJson(array.getJSONObject(i).toString(), StickersItem.class);
                    stickersItems.add(item);
                }

                if (mMod == 4) {
                    MediaScannerConnection.scanFile(VideoRecordActivity.this,
                            new String[]{fastForwardPath}, null,
                            (path, uri) ->
                                    runOnUiThread(() -> {
                                        dismissProgressDialog();
                                        mProgress.setVisibility(View.GONE);
                                        startActivity(new Intent(VideoRecordActivity.this, VideoEditActivity.class)
                                                .putExtra("path", path)
                                                .putExtra("productId", productId)
                                                .putExtra("product_img", Constant.BASE_URL + productSticker)
                                                .putExtra("productName", productName)
                                                .putExtra("productUrl", productUrl)
                                                .putExtra("productThumbnailUrl", productThumbnailUrl)
                                                .putExtra("stickers", stickersItems)
                                                .putExtra("uri", uri.toString()));
                                        finish();
                                    }));
                } else {
                    File file = getDefaultVideoFile();
                    File filename1 = new File(file.getAbsolutePath(), "glow_" + System.currentTimeMillis() + ".mp4");
                    showProgressBarDialog();
                    CustomTextView textView = mProgressBarDialog.findViewById(R.id.progress);
                    textView.setText("Processing");
                    Transcoder.into(filename1.getAbsolutePath())
                            .addDataSource(mFinalPath)
                            .setListener(new TranscoderListener() {
                                public void onTranscodeProgress(double progress) {
                                    runOnUiThread(() -> {
                                        TextRoundCornerProgressBar p = mProgressBarDialog.findViewById(R.id.progress_bar);
                                        p.setProgress((float) progress * 100);
                                        Log.e("Transrog: ", String.valueOf(progress));
                                    });
                                }

                                public void onTranscodeCompleted(int successCode) {
                                    MediaScannerConnection.scanFile(VideoRecordActivity.this,
                                            new String[]{filename1.getAbsolutePath()}, null,
                                            (path, uri) ->
                                                    runOnUiThread(() -> {
                                                        dismissProgressBarDialog();
                                                        startActivity(new Intent(VideoRecordActivity.this, VideoEditActivity.class)
                                                                .putExtra("path", path)
                                                                .putExtra("productId", productId)
                                                                .putExtra("product_img", Constant.BASE_URL + productSticker)
                                                                .putExtra("productName", productName)
                                                                .putExtra("productUrl", productUrl)
                                                                .putExtra("productThumbnailUrl", productThumbnailUrl)
                                                                .putExtra("stickers", stickersItems)
                                                                .putExtra("uri", uri.toString()));
                                                        finish();
                                                    }));
                                }

                                public void onTranscodeCanceled() {
                                    runOnUiThread(() -> {
                                        dismissProgressBarDialog();
                                    });
                                }

                                public void onTranscodeFailed(@NonNull Throwable exception) {
                                    runOnUiThread(() -> {
                                        dismissProgressBarDialog();
                                    });
                                }
                            }).transcode();

//                    INSTANCE.start(mFinalPath, filename1.getAbsolutePath(), new CompressionListener() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            MediaScannerConnection.scanFile(VideoRecordActivity.this,
//                                    new String[]{filename1.getAbsolutePath()}, null,
//                                    (path, uri) ->
//                                            runOnUiThread(() -> {
//                                                dismissProgressBarDialog();
//                                                startActivity(new Intent(VideoRecordActivity.this, VideoEditActivity.class)
//                                                        .putExtra("path", path)
//                                                        .putExtra("productId", productId)
//                                                        .putExtra("product_img", Constant.BASE_URL + productSticker)
//                                                        .putExtra("productName", productName)
//                                                        .putExtra("productUrl", productUrl)
//                                                        .putExtra("productThumbnailUrl", productThumbnailUrl)
//                                                        .putExtra("stickers", stickersItems)
//                                                        .putExtra("uri", uri.toString()));
//                                                finish();
//                                            }));
//
//                        }
//
//                        @Override
//                        public void onFailure() {
//                            runOnUiThread(() -> {
//                                dismissProgressBarDialog();
//                            });
//                        }
//
//                        @Override
//                        public void onProgress(float v) {
//                            runOnUiThread(() -> {
//                                TextRoundCornerProgressBar p = mProgressBarDialog.findViewById(R.id.progress_bar);
//                                p.setProgress(v);
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled() {
//                            runOnUiThread(() -> {
//                                dismissProgressBarDialog();
//                            });
//                        }
//                    }, VideoQuality.HIGH, false);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void switchCamera() {
        if (!isRecording) {
            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                releaseCamera();
                chooseCamera();
            }
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*releaseMediaRecorder();
        releaseCamera();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void chooseCamera() {
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mCamera.lock();
                mCamera.setDisplayOrientation(90);
                try {
                    mCamera.setPreviewDisplay(mPreview.getHolder());
                    mCamera.startPreview();
                    flashButton.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId); // change switch camera icon image
                mCamera.lock();
                mCamera.setDisplayOrientation(90);
                try {
                    mCamera.setPreviewDisplay(mPreview.getHolder());
                    mCamera.startPreview();
                    if (isCameraFlashOn) {
                        isCameraFlashOn = false;
                        flashButton.setImageDrawable(ContextCompat.getDrawable(VideoRecordActivity.this, R.drawable.flash_off));
                    }
                    flashButton.setEnabled(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    float mDist;

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }

    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//            mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean b, Camera camera) {
//                    // currently set to auto-focus on single touch
//                }
//            });

        }

    }





    /**
     * Determine the space between the first two fingers
     */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                setOrientationHint = 270;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras(); // get the number of cameras
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                setOrientationHint = 90;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public static boolean isFlashAvailable(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void setFlashOnOff(boolean flash) {
        if (flash) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
        } else {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
        }
    }

    private boolean prepareVideoRecorder() {
//        mCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        int cameraId = findBackFacingCamera();
//        int o = getVideoOrientationAngle(this,cameraId);
        mediaRecorder.setOrientationHint(90);

//        switch (0) {
//            case 0:
//                mediaRecorder.setOrientationHint(90);
//                break;
//            case 90:
//                mediaRecorder.setOrientationHint(180);
//                break;
//            case 180:
//                mediaRecorder.setOrientationHint(270);
//                break;
//            case 270:
//                mediaRecorder.setOrientationHint(0);
//                break;
//        }
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.audioCodec = MediaRecorder.AudioEncoder.AAC;
        profile.videoCodec = MediaRecorder.VideoEncoder.DEFAULT;
        mediaRecorder.setProfile(profile);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        path = getOutputFile(MEDIA_TYPE_VIDEO).toString();
        mediaRecorder.setOutputFile(path);
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public int getVideoOrientationAngle(Activity activity, int cameraId) { //The param cameraId is the number of the camera.
        int angle;
        Display display = activity.getWindowManager().getDefaultDisplay();
        int degrees = display.getRotation();
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        switch (degrees) {
            case Surface.ROTATION_0:
                angle = 90;
                break;
            case Surface.ROTATION_90:
                angle = 0;
                break;
            case Surface.ROTATION_180:
                angle = 270;
                break;
            case Surface.ROTATION_270:
                angle = 180;
                break;
            default:
                angle = 90;
                break;
        }
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            angle = (angle + 180) % 360;

        return angle;
    }

    public static int setCameraDisplayOrientation(Activity activity,
                                                  int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        return result;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {

            c = Camera.open(0);
            Camera.Parameters params = c.getParameters();
            if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            c.setParameters(params);
            c.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private File getOutputFile(int mediaTypeVideo) {

        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(temp, "Glow");
        if (!file.exists()) {
            file.mkdirs();
        }

//       int c =  file.listFiles().length;

//        File file = Environment.getExternalStorageDirectory();
        if (mediaTypeVideo == MEDIA_TYPE_VIDEO) {
            return new File(file.getPath() + File.separator + "VID_" + System.currentTimeMillis() + ".mp4");
        } else {
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (count != null) {
            count.cancel();
            count = null;
        }
    }


    private void tutorialDialog() {
        Dialog dialog = new Dialog(this, R.style.WideDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tutorial);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ViewPager mViewPager;
        ScreenSlidePagerAdapter pagerAdapter;
        Button mLetsGo;
        mViewPager = dialog.findViewById(R.id.viewpager);
        mLetsGo = dialog.findViewById(R.id.lets_go);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        mViewPager.setAdapter(pagerAdapter);

        SmartTabLayout viewPagerTab = dialog.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(mViewPager);

        mLetsGo.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private class ScreenSlidePagerAdapter extends PagerAdapter {
        LayoutInflater inflater;

        public ScreenSlidePagerAdapter(Activity activity) {
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup view, int position) {
            View view1 = inflater.inflate(R.layout.fragment_tutorial, view, false);
            ImageView mProductImage;
            CustomTextView mTitle;
            LinearLayout mView1, mView2, mView3;

            mProductImage = view1.findViewById(R.id.p_image);
            mTitle = view1.findViewById(R.id.p_name);
            mView1 = view1.findViewById(R.id.view1);
            mView2 = view1.findViewById(R.id.view2);
            mView3 = view1.findViewById(R.id.view3);
            Glide.with(getApplicationContext()).load(productThumbnailUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(mProductImage);
            mTitle.setText("You are about to review " + productName);


            switch (position) {
                case 0:
                    mView1.setVisibility(View.VISIBLE);
                    mView2.setVisibility(View.GONE);
                    mView3.setVisibility(View.GONE);
                    break;
                case 1:
                    mView2.setVisibility(View.VISIBLE);
                    mView1.setVisibility(View.GONE);
                    mView3.setVisibility(View.GONE);
                    break;
                case 2:
                    mView3.setVisibility(View.VISIBLE);
                    mView2.setVisibility(View.GONE);
                    mView1.setVisibility(View.GONE);
                    break;
            }

            view.addView(view1);
            return view1;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "Tutorial";
                case 2:
                    return "Tips";
                default:
                    return "Product";
            }
        }
    }

}

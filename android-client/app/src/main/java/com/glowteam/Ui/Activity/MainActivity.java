package com.glowteam.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Interfaces.Interface;
import com.glowteam.Models.User;
import com.glowteam.Models.VideoData;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.DropsFragment;
import com.glowteam.Ui.Fragments.MakeReview;
import com.glowteam.Ui.Fragments.WatchFragment;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.glowteam.Constant.Constant.BASE_URL;

public class MainActivity extends BaseActivity {

    LinearLayout mWatch, mDrops;
    CustomTextView watchTxt, dropsTxt;
    View watchLine, dropsline;
    private static final String TAG = "MainActivity";

    public static CircularImageView mProfImg;
    private User user;
    private CardView make_review;
    private int read_storage, write_storage;
    private int read_camera;
    private int read_audio;
    private String frag_name;
    private MakeReview make_review_frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProfImg = findViewById(R.id.img);
        make_review = findViewById(R.id.make_review);
        getProfile();
        mProfImg.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        make_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* read_storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                read_camera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                read_audio = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);
                if (read_storage == PERMISSION_GRANTED && read_camera == PERMISSION_GRANTED && read_audio == PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, VideoRecordActivity.class));
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 101);
                }*/
//                frag_name = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
                make_review_frag = MakeReview.newInstance("make_review");
                if (!make_review_frag.isAdded()) {
                    make_review_frag.show(getSupportFragmentManager(), make_review_frag.getTag());
                }
            }
        });
        setUpTabs();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                write_storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                read_storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                read_camera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                read_audio = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO);
                if (read_storage == PERMISSION_GRANTED && write_storage == PERMISSION_GRANTED && read_camera == PERMISSION_GRANTED &&
                        read_audio == PERMISSION_GRANTED) {
//                    startActivity(new Intent(this, VideoRecordActivity.class));
                    if (Interface.mOnPermissionGranted != null) {
                        Interface.mOnPermissionGranted.OnPermissionGranted();
                    }
                } else {
                    Toast.makeText(this, "Allow Camera and storage permission for recording review...!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 102:
                write_storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                read_storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (read_storage == PERMISSION_GRANTED && write_storage == PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Allow storage permission for downloading sticker...!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        super.OnResponse(o, type);
        if (type == ApiCall.GET_PROFILE) {
            try {
                Log.d(TAG, "OnResponse: ====>" + o.toString());
                JSONObject object = o.getJSONObject("data");
                setMainUser(object.toString());
                user = getMainUser();
                Glide.with(this).load(BASE_URL + user.getProfilePic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(mProfImg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (type == ApiCall.GET_USER_VIDEOS) {
            try {
                JSONArray jArray = o.getJSONArray("data");
                ArrayList<VideoData> videos = new ArrayList<>();
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        VideoData video = new Gson().fromJson(jArray.getJSONObject(i).toString(), VideoData.class);
                        videos.add(video);
                    }
                }
                for (int i = 0; i < videos.size(); i++) {
                    Log.d(TAG, "OnResponse: ====>" + videos.get(i).getVideoUrl());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpTabs() {
        mWatch = findViewById(R.id.watch);
        mDrops = findViewById(R.id.drops);
        watchTxt = findViewById(R.id.watchTxt);
        dropsTxt = findViewById(R.id.dropsTxt);
        watchLine = findViewById(R.id.watchLine);
        dropsline = findViewById(R.id.dropsLine);
        selectTab(0);

        mWatch.setOnClickListener(v -> {
            selectTab(0);
        });
        mDrops.setOnClickListener(v -> {
            selectTab(1);
        });
    }


    private void selectTab(int pos) {
        if (pos == 0) {
            watchTxt.setTextColor(ContextCompat.getColor(this, R.color.tabTextColor));
            watchLine.setVisibility(View.VISIBLE);
            dropsline.setVisibility(View.GONE);
            dropsTxt.setTextColor(ContextCompat.getColor(this, R.color.tabTextUnSelected));
            getSupportFragmentManager().beginTransaction().replace(R.id.frameH, new WatchFragment()).commit();
        } else if (pos == 1) {
            watchTxt.setTextColor(ContextCompat.getColor(this, R.color.tabTextUnSelected));
            watchLine.setVisibility(View.GONE);
            dropsline.setVisibility(View.VISIBLE);
            dropsTxt.setTextColor(ContextCompat.getColor(this, R.color.tabTextColor));
            getSupportFragmentManager().beginTransaction().replace(R.id.frameH, new DropsFragment()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = getMainUser();
        Glide.with(this).load(BASE_URL + user.getProfilePic()).placeholder(R.drawable.user)
                .error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL).into(mProfImg);
    }
}

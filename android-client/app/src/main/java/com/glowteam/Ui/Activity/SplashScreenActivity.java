package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.glowteam.R;

import org.buffer.android.thumby.ThumbyActivity;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            if (getMainUser() != null) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
//                Uri resultUri = Uri.parse("file:///storage/emulated/0/DCIM/img_1598893112740.mp4");
//                startActivityForResult(ThumbyActivity.Companion.getStartIntent(this, resultUri, 0), 2);
                finish();
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivityNew.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}

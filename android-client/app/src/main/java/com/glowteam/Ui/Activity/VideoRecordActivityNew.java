package com.glowteam.Ui.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.glowteam.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class VideoRecordActivityNew extends CameraVideoFragment {

    boolean isRecording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        mTextureView = findViewById(R.id.mTextureView);

        ImageView startVideoButton = findViewById(R.id.start_video_button);
        startVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording){
                    isRecording = false;
                    try {
                        stopRecordingVideo();
                        initDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    isRecording = true;
                    startRecordingVideo();
                }
            }
        });
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }


    private void initDialog() {

        MediaScannerConnection.scanFile(this,
                new String[]{getCurrentFile().getAbsolutePath()}, null,
                (path, uri) -> runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setTitle("Upload Video")
                        .setMessage("Are you sure you want to upload this Video?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            startActivity(new Intent(this, VideoEditActivity.class)
                                    .putExtra("path", path)
                                    .putExtra("product_img", getIntent().getStringExtra("product_img"))
                                    .putExtra("uri", uri.toString()));
                            finish();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                        .show()));

    }
}

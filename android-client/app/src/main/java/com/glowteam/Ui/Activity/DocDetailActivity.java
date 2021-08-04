package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.FeaturedArticle;
import com.glowteam.R;

import java.io.File;

import static com.glowteam.Constant.Constant.BASE_URL;

public class DocDetailActivity extends BaseActivity {

    FeaturedArticle featuredArticle;
    WebView webview;
    ImageView mImg, mBack, mShare;
    CustomTextView mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);

        featuredArticle = (FeaturedArticle) getIntent().getSerializableExtra("docData");

        mImg = findViewById(R.id.img);
        mBack = findViewById(R.id.back);
        mShare = findViewById(R.id.share);
        mTitle = findViewById(R.id.title);
        webview = findViewById(R.id.webview);

        mTitle.setText(featuredArticle.getTitle());
        Glide.with(this).load(BASE_URL + featuredArticle.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(mImg);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL("", featuredArticle.getContent(), "text/html", "UTF-8", "");


        mBack.setOnClickListener(v -> {
            finish();
        });

        mShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setType("video/*");
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            String extraText = featuredArticle.getTitle() + "\n Read full Article in our app \n" + url;
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, extraText + "\n");
            startActivity(Intent.createChooser(shareIntent, "Share with..."));
        });

    }
}

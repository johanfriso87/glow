package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.R;


public class SplashImageFragment extends BaseFragment {


    ImageView mImg;
    LinearLayout mTitle;
    int num;

    public SplashImageFragment(int num) {
        this.num = num;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash_image, container, false);

        mImg = view.findViewById(R.id.img);
        mTitle = view.findViewById(R.id.title);

        switch (num) {
            case 0:
                mImg.setImageResource(R.drawable.iv_appicon);
                mImg.setVisibility(View.GONE);
                mTitle.setVisibility(View.VISIBLE);
                break;
            case 1:
                mImg.setImageResource(R.drawable.splash_1);
                break;
            case 2:
                mImg.setImageResource(R.drawable.splash_1);
                break;
            case 3:
                mImg.setImageResource(R.drawable.splash_1);
                break;
        }

        return view;
    }
}

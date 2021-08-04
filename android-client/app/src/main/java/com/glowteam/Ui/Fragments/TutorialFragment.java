package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.R;

public class TutorialFragment extends BaseFragment {


    ImageView mProductImage;
    CustomTextView mTitle;
    String productName, productThumbnailUrl;
    private int num;
    LinearLayout mView1, mView2, mView3;


    public TutorialFragment(String productName, String productThumbnailUrl, int num) {
        this.productName = productName;
        this.productThumbnailUrl = productThumbnailUrl;
        this.num = num;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        mProductImage = view.findViewById(R.id.p_image);
        mTitle = view.findViewById(R.id.p_name);
        Glide.with(this).load(Constant.BASE_URL + productThumbnailUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(mProductImage);
        mTitle.setText("You are about to review " + productName);


        switch (num) {
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

        return view;
    }
}

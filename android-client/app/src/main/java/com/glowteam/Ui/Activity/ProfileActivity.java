package com.glowteam.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Adapters.ReviewTypeAdapter;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Interfaces.Interface;
import com.glowteam.Models.CategoryItem;
import com.glowteam.Models.User;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.MakeReview;
import com.glowteam.Ui.Fragments.ReviewsFragments.LikedReviews;
import com.glowteam.Ui.Fragments.ReviewsFragments.SavedProductsFragment;
import com.glowteam.Ui.Fragments.ReviewsFragments.SubscriptionFragment;
import com.glowteam.Ui.Fragments.ReviewsFragments.YourReviewsFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.glowteam.Constant.Constant.BASE_URL;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    String[] strings = new String[]{"Your Reviews", "Liked Reviews", "Subscriptions", "Saved Products"};
    ViewPager mViewPager;
    PagerAdapter pagerAdapter;
    RecyclerView mCategoryView;
    ReviewTypeAdapter categoryAdapter;
    ArrayList<CategoryItem> categoryItemArrayList = new ArrayList<>();
    private ImageView setting;
    private LinearLayout make_review;
    public static CircularImageView img;
    private User user;
    private ImageView back_profile;
    private LinearLayout edit_profile;
    private int read_storage;
    private int read_camera;
    private int read_audio;
    private CustomTextView title, mShareId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_profile);

        openDialogue();

        initView();
        initListener();
    }




    private void initListener() {
        setting.setOnClickListener(this);
        make_review.setOnClickListener(this);
        back_profile.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
    }

    private void initView() {
        setting = findViewById(R.id.setting);
        title = findViewById(R.id.title);
        back_profile = findViewById(R.id.back_profile);
        img = findViewById(R.id.img);
        make_review = findViewById(R.id.make_review);
        edit_profile = findViewById(R.id.edit_profile);
        mShareId = findViewById(R.id.share_id);

    }


    public void addItems() {
        for (String s : strings) {
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setName(s);
            categoryItemArrayList.add(categoryItem);
        }
        categoryItemArrayList.get(0).setSelected(true);
    }

    private void openDialogue() {
        mViewPager = findViewById(R.id.pager);
        mCategoryView = findViewById(R.id.categoryView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mCategoryView.setLayoutManager(linearLayoutManager);
        addItems();
        categoryAdapter = new ReviewTypeAdapter(this, categoryItemArrayList);
        mCategoryView.setAdapter(categoryAdapter);
        categoryAdapter.SetOnItemClickListener(onItemClickListener);
        pagerAdapter = new ScreenSlidePagerAdapter1(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (CategoryItem item : categoryItemArrayList) {
                    item.setSelected(false);
                }
                categoryItemArrayList.get(position).setSelected(true);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    ReviewTypeAdapter.OnItemClickListener onItemClickListener = new ReviewTypeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            for (CategoryItem item : categoryItemArrayList) {
                item.setSelected(false);
            }
            categoryItemArrayList.get(position).setSelected(true);
            categoryAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.make_review:
//                read_storage = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                read_camera = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA);
//                read_audio = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.RECORD_AUDIO);
//                if (read_storage == PERMISSION_GRANTED && read_camera == PERMISSION_GRANTED && read_audio == PERMISSION_GRANTED) {
//                    startActivity(new Intent(this, VideoRecordActivity.class));
//                } else {
//                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 101);
//                }

                MakeReview make_review_frag = MakeReview.newInstance("make_review");
                if (!make_review_frag.isAdded()) {
                    make_review_frag.show(getSupportFragmentManager(), make_review_frag.getTag());
                }
                break;
            case R.id.back_profile:
                onBackPressed();
                break;
            case R.id.edit_profile:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                read_storage = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                read_camera = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA);
                read_audio = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.RECORD_AUDIO);
                if (read_storage == PERMISSION_GRANTED && read_camera == PERMISSION_GRANTED && read_audio == PERMISSION_GRANTED) {
//                    startActivity(new Intent(this, VideoRecordActivity.class));
                    if (Interface.mOnPermissionGranted != null) {
                        Interface.mOnPermissionGranted.OnPermissionGranted();
                    }
                } else {
                    Toast.makeText(this, "Allow All Permission...!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private class ScreenSlidePagerAdapter1 extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter1(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new LikedReviews();
                case 2:
                    return new SubscriptionFragment();
                case 3:
                    return new SavedProductsFragment();
                default:
                    return new YourReviewsFragment();
            }
        }

        @Override
        public int getCount() {
            return strings.length;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = getMainUser();
        Glide.with(this).load(BASE_URL + user.getProfilePic()).placeholder(R.drawable.user)
                .error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        if (!TextUtils.isEmpty(user.getName())) {
            title.setText(user.getName());
            mShareId.setText("glow.at/" + user.getName().replace(" ",""));
        }
    }
}

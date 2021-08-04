package com.glowteam.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.glowteam.R;

public class SplashActivity extends BaseActivity {

    //    ViewPager mViewPager;
//    private PagerAdapter pagerAdapter;
//    DotsIndicator dotsIndicator;
    Button mSignUp;
    TextView mLoginTxt;

//    int currentPage = 0;
//    Timer timer;
//    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
//    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        mViewPager = findViewById(R.id.viewPager);
        mSignUp = findViewById(R.id.sign_up);
        mLoginTxt = findViewById(R.id.loginTxt);
//        dotsIndicator = findViewById(R.id.dots_indicator);
//        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(pagerAdapter);
//        dotsIndicator.setViewPager(mViewPager);

//        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        mSignUp.setOnClickListener(v -> {
            if (getMainUser() != null) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, LoginActivityNew.class);
                i.putExtra("signIn", false);
                startActivity(i);
            }
        });
        mLoginTxt.setOnClickListener(v -> {
            if (getMainUser() != null) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, LoginActivityNew.class);
                i.putExtra("signIn", true);
                startActivity(i);
            }
        });

//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == 3) {
//                    currentPage = 0;
//                }
//                mViewPager.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
    }


//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 2:
//                    return new SplashImageFragment(1);
//                case 3:
//                    return new SplashImageFragment(2);
//                case 4:
//                    return new SplashImageFragment(3);
//                default:
//                    return new SplashImageFragment(0);
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//    }


}

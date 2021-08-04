package com.glowteam.Adapters;

import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.glowteam.R;
import com.glowteam.Ui.Activity.ShareActivity;
import com.potyvideo.library.AndExoPlayerView;

import static com.glowteam.Constant.Constant.BASE_URL;
import static ly.img.android.pesdk.backend.decoder.ImageSource.getResources;

public class ShareAdapter extends PagerAdapter {
    ShareActivity activity;
    String path;
    int[] shareFrame;

    public ShareAdapter(ShareActivity shareActivity, String path, int[] shareFrame) {
        activity = shareActivity;
        this.path = path;
        this.shareFrame = shareFrame;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
//    public ViewGroup layout;

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_share, collection, false);
//        VideoView videoView = layout.findViewById(R.id.videoView);
//        AndExoPlayerView andExoPlayerView = layout.findViewById(R.id.videoView);
//        ImageView app_icon = layout.findViewById(R.id.app_icon);
        ImageView frame = layout.findViewById(R.id.frame);
//        RelativeLayout mIconView = layout.findViewById(R.id.iconView);
        frame.setImageDrawable(ContextCompat.getDrawable(activity, shareFrame[position]));
        int newWidth = 225, newHeight = 388;
        newWidth = (int) convertDpToPixel(newWidth);
        newHeight = (int) convertDpToPixel(newHeight);
//       FrameLayout.LayoutParams layout1 = (FrameLayout.LayoutParams) mIconView.getLayoutParams();
//       layout1.width = newWidth;

//        videoView.setVideoPath(path);
//        videoView.setOnPreparedListener(mp -> {
//            mp.setLooping(true);
//            videoView.start();
//        });
//        andExoPlayerView.setSource(BASE_URL +path);
//        andExoPlayerView.setPlayWhenReady(true);
//        app_icon.setImageDrawable(ContextCompat.getDrawable(activity, shareIcon[position]));


        ViewTreeObserver vto = frame.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if(!isCalled){
                    isCalled = true;
                frame.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = frame.getMeasuredHeight();
                int finalWidth = frame.getMeasuredWidth();
                Log.e("final height",String.valueOf(finalHeight));
                Log.e("final height",String.valueOf(finalWidth));

                    activity.setImageHeight(finalHeight);

                }

                return true;
            }
        });




        collection.addView(layout);
        return layout;
    }
    public boolean isCalled = false;
    public float convertDpToPixel(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return shareFrame.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}

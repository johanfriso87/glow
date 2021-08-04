package com.glowteam.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.ProductSearchUrl;
import com.glowteam.Models.User;
import com.glowteam.Models.Video;
import com.glowteam.Models.VideoData;
import com.glowteam.R;
import com.glowteam.Ui.Activity.Product_Activity;
import com.glowteam.Ui.Activity.VideoPlayActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.universalvideoview.UniversalVideoView;
import com.whygraphics.gifview.gif.GIFView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.glowteam.Constant.Constant.BASE_URL;

public class YourReviewAdapter extends RecyclerView.Adapter<YourReviewAdapter.ViewHolder> {

    private ArrayList<Video> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    User mainUser;
    String TAG = "YourReviewAdapter";

    public YourReviewAdapter(Context context, ArrayList<Video> Items, User mainUser) {
        mContext = context;
        this.mainUser = mainUser;
        mItems = Items;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_your_review, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Glide.with(mContext).load(BASE_URL + mItems.get(holder.getAdapterPosition()).getThumbnailUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image_view);
//        holder.mTitle.setText(mainUser.getName());
        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getProductThumbnailUrl())) {
            Glide.with(mContext).load( mItems.get(holder.getAdapterPosition()).getProductThumbnailUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iameg_profile);
        } else {
            Glide.with(mContext).load(R.drawable.people).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iameg_profile);
        }
        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getProductName())) {
            holder.mTitle.setText(mItems.get(holder.getAdapterPosition()).getProductName());
        }
        holder.mViews.setText(mItems.get(holder.getAdapterPosition()).getViews() + " Views");
        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getGif())) {
            holder.gifView.setGifResource("url:" + Constant.BASE_URL + mItems.get(holder.getAdapterPosition()).getGif());
            holder.gifView.setOnSettingGifListener(new GIFView.OnSettingGifListener() {
                @Override
                public void onSuccess(GIFView view, Exception e) {
                    holder.image_view.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(GIFView view, Exception e) {

                }
            });
        }

//        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getVideoUrl())) {
//            holder.andExoPlayerView.setVideoPath(BASE_URL + mItems.get(holder.getAdapterPosition()).getVideoUrl());
//            holder.andExoPlayerView.requestFocus();
//            holder.andExoPlayerView.start();
//            holder.andExoPlayerView.setOnPreparedListener(mp -> {
//                holder.image_view.setVisibility(View.GONE);
//                mp.setLooping(true);
//                holder.andExoPlayerView.requestFocus();
//                holder.andExoPlayerView.start();
//            });
////            holder.andExoPlayerView.setVideoPath(BASE_URL +mItems.get(holder.getAdapterPosition()).getVideoUrl());
////            holder.andExoPlayerView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
////                @Override
////                public void onScaleChange(boolean isFullscreen) {
////
////                }
////
////                @Override
////                public void onPause(MediaPlayer mediaPlayer) { // Video pause
////                    Log.d(TAG, "onPause UniversalVideoView callback");
////                }
////
////                @Override
////                public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
////                    Log.d(TAG, "onStart UniversalVideoView callback");
////                    holder.andExoPlayerView.setVisibility(View.VISIBLE);
////                    holder.image_view.setVisibility(View.GONE);
////                }
////
////                @Override
////                public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
////                    Log.d(TAG, "onBufferingStart UniversalVideoView callback");
////                }
////
////                @Override
////                public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
////                    Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
////                }
////            });
//        } else {
//            holder.andExoPlayerView.setVisibility(View.GONE);
//            holder.image_view.setVisibility(View.VISIBLE);
//        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iameg_profile;
        ImageView image_view;
        TextView mTitle;
        CustomTextView mViews;
        GIFView gifView;
//        VideoView andExoPlayerView;

        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.name);
            image_view = itemView.findViewById(R.id.image_view);
            iameg_profile = itemView.findViewById(R.id.iameg_profile);
            gifView = itemView.findViewById(R.id.gif_view);
            mViews = itemView.findViewById(R.id.views);
//            andExoPlayerView = itemView.findViewById(R.id.andExoPlayerView);
//            itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VideoPlayActivity.class).putExtra("videoData", mItems).putExtra("pos", getAdapterPosition())));
            gifView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VideoPlayActivity.class).
                    putExtra("videoData",  mItems).putExtra("pos", getAdapterPosition())));
            iameg_profile.setOnClickListener(v -> {
                ProductSearchUrl productSearchUrl = new ProductSearchUrl();
                productSearchUrl.setTitle(mItems.get(getAdapterPosition()).getProductName());
                productSearchUrl.setAsin(mItems.get(getAdapterPosition()).getAsin());
                productSearchUrl.setUrl(mItems.get(getAdapterPosition()).getProductUrl());
                ArrayList<String> images = new ArrayList<>();
                images.add(mItems.get(getAdapterPosition()).getProductThumbnailUrl());
                productSearchUrl.setImages(images);

                mContext.startActivity(new Intent(mContext, Product_Activity.class).
                        putExtra("product", productSearchUrl));
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
//        holder.andExoPlayerView.stopPlayback();
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        holder.andExoPlayerView.resume();
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
//        holder.andExoPlayerView.pause();
//        holder.image_view.setVisibility(View.VISIBLE);
        super.onViewDetachedFromWindow(holder);
    }
}

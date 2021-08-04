package com.glowteam.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.Models.ProductSearchUrl;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.glowteam.Ui.Activity.Product_Activity;
import com.glowteam.Ui.Activity.VideoPlayActivity;
import com.whygraphics.gifview.gif.GIFView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.glowteam.Constant.Constant.BASE_URL;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Video> videoArrayList;
    LayoutInflater inflater;
    String TAG = "VideoAdapter";
    ArrayList<Bitmap> bitmaps = new ArrayList<>();

    public VideoAdapter(Context mContext, ArrayList<Video> videoArrayList) {
        this.mContext = mContext;
        this.videoArrayList = videoArrayList;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(BASE_URL + videoArrayList.get(holder.getAdapterPosition()).getThumbnailUrl()).into(holder.image_view);
        if (!TextUtils.isEmpty(videoArrayList.get(holder.getAdapterPosition()).getProductThumbnailUrl())) {
            Glide.with(mContext).load(videoArrayList.get(holder.getAdapterPosition()).getProductThumbnailUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_user);
        } else if (!TextUtils.isEmpty(videoArrayList.get(holder.getAdapterPosition()).getUserId().getProfilePic())) {
            Glide.with(mContext).load(BASE_URL + videoArrayList.get(holder.getAdapterPosition()).getUserId().getProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_user);
        } else {
            Glide.with(mContext).load(R.drawable.people).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_user);
        }
        if (!TextUtils.isEmpty(videoArrayList.get(holder.getAdapterPosition()).getProductName())) {
            holder.name.setText(videoArrayList.get(holder.getAdapterPosition()).getProductName());
        } else {
            holder.name.setText(videoArrayList.get(holder.getAdapterPosition()).getUserId().getName());
        }
        if (!TextUtils.isEmpty(videoArrayList.get(holder.getAdapterPosition()).getUserId().getProfilePic())) {
            Glide.with(mContext).load(BASE_URL + videoArrayList.get(holder.getAdapterPosition()).getUserId().getProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image_user);
        }else{
            Glide.with(mContext).load(R.drawable.people).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image_user);
        }
        holder.user_name.setText(videoArrayList.get(holder.getAdapterPosition()).getUserId().getName());

        if (!TextUtils.isEmpty(videoArrayList.get(holder.getAdapterPosition()).getGif())) {
            holder.gifView.setGifResource("url:" + Constant.BASE_URL + videoArrayList.get(holder.getAdapterPosition()).getGif());
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


//        new DownloadImage(holder.image_view, BASE_URL + videoArrayList.get(holder.getAdapterPosition()).getVideoUrl()).execute();

//        if (!TextUtils.isEmpty(videoArrayList.get(holder.getAdapterPosition()).getVideoUrl())) {
//            holder.andExoPlayerView.setVideoPath(BASE_URL + videoArrayList.get(holder.getAdapterPosition()).getVideoUrl());
//            holder.andExoPlayerView.requestFocus();
//            holder.andExoPlayerView.start();
//            holder.andExoPlayerView.setOnPreparedListener(mp -> {
//                holder.image_view.setVisibility(View.GONE);
//                mp.setLooping(true);
//                holder.andExoPlayerView.requestFocus();
//                holder.andExoPlayerView.start();
//            });
//
////            holder.andExoPlayerView.setSource(BASE_URL + videoArrayList.get(position).getVideoUrl());
////            holder.andExoPlayerView.setOnPlayerListener(new AndExoPlayerView.OnPlayerListener() {
////                @Override
////                public void OnStart() {
//////                    holder.andExoPlayerView.setVisibility(View.VISIBLE);
//////                    holder.image_view.setVisibility(View.GONE);
////                }
////
////                @Override
////                public void OnStop() {
////                }
////            });
////            holder.andExoPlayerView.setPlayWhenReady(true);
////            holder.andExoPlayerView.setVideoPath(BASE_URL +
////                    videoArrayList.get(holder.getAdapterPosition()).getVideoUrl()).setFingerprint(holder.getAdapterPosition());
////            holder.andExoPlayerView.getPlayer().getVideoInfo().setLooping(true);
////            holder.andExoPlayerView.getPlayer().getVideoInfo().setShowTopBar(false);
////            holder.andExoPlayerView.getPlayer().setPlayerListener(new PlayerListener() {
////                @Override
////                public void onPrepared(GiraffePlayer giraffePlayer) {
////                    Log.e("onPrepared", "true");
////                    holder.image_view.setVisibility(View.GONE);
////                }
////
////                @Override
////                public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
////
////                }
////
////                @Override
////                public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
////                    return false;
////                }
////
////                @Override
////                public void onCompletion(GiraffePlayer giraffePlayer) {
////                   giraffePlayer.start();
////                }
////
////                @Override
////                public void onSeekComplete(GiraffePlayer giraffePlayer) {
////
////                }
////
////                @Override
////                public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
////                    return false;
////                }
////
////                @Override
////                public void onPause(GiraffePlayer giraffePlayer) {
////
////                }
////
////                @Override
////                public void onRelease(GiraffePlayer giraffePlayer) {
////
////                }
////
////                @Override
////                public void onStart(GiraffePlayer giraffePlayer) {
////
////                }
////
////                @Override
////                public void onTargetStateChange(int oldState, int newState) {
////
////                }
////
////                @Override
////                public void onCurrentStateChange(int oldState, int newState) {
////                    Log.e("onCurrentStateChange", oldState + " : " + newState);
////                }
////
////                @Override
////                public void onDisplayModelChange(int oldModel, int newModel) {
////
////                }
////
////                @Override
////                public void onPreparing(GiraffePlayer giraffePlayer) {
////
////                }
////
////                @Override
////                public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {
////
////                }
////
////                @Override
////                public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {
////                    Log.e("onLazyLoadProgress", String.valueOf(progress));
////                }
////
////                @Override
////                public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {
////
////                }
////            });
////            holder.andExoPlayerView.getPlayer().start();
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


    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String url;
        long time = 0;

        public DownloadImage(ImageView bmImage, String url) {
            this.bmImage = bmImage;
            this.url = url;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap myBitmap = null;
            MediaMetadataRetriever mMRetriever = null;
            try {
                mMRetriever = new MediaMetadataRetriever();
                mMRetriever.setDataSource(url, new HashMap<>());
                do {
                    time += 1000;
                    myBitmap = mMRetriever.getFrameAtTime(time);
                    bitmaps.add(myBitmap);
                    Bitmap finalMyBitmap = myBitmap;
                    ((Activity) mContext).runOnUiThread(() -> bmImage.setImageBitmap(finalMyBitmap));
                } while (time < 7000);
            } catch (Exception e) {
                e.printStackTrace();


            } finally {
                if (mMRetriever != null) {
                    mMRetriever.release();
                }
            }
            return myBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            final int[] i = {-1};
//            while (true) {
//                i++;
//                if (i == 7) {
//                    i = 0;
//                }
//                bmImage.setImageBitmap(bitmaps.get(i));
//            }

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    i[0]++;
                    if (i[0] == 7) {
                        i[0] = 0;
                    }
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bmImage.setImageBitmap(bitmaps.get(i[0]));
                            handler.postDelayed(this, 1000L);
                        }
                    });
                }
            };
            handler.post(runnable);
        }
    }


    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView user_name;
        ImageView profile_user;
        ImageView image_view;
        //        AndExoPlayerView andExoPlayerView;
//        VideoView andExoPlayerView;
        GIFView gifView;
        ImageView profile_image_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            profile_user = itemView.findViewById(R.id.profile_user);
            profile_user = itemView.findViewById(R.id.profile_user);
            profile_image_user = itemView.findViewById(R.id.profile_image_user);
            user_name = itemView.findViewById(R.id.user_name);
            name = itemView.findViewById(R.id.name);
            gifView = itemView.findViewById(R.id.gif_view);
//            andExoPlayerView = itemView.findViewById(R.id.andExoPlayerView);
            gifView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VideoPlayActivity.class).
                    putExtra("videoData", videoArrayList).putExtra("pos", getAdapterPosition())));
            profile_user.setOnClickListener(v -> {
                ProductSearchUrl productSearchUrl = new ProductSearchUrl();
                productSearchUrl.setTitle(videoArrayList.get(getAdapterPosition()).getProductName());
                productSearchUrl.setAsin(videoArrayList.get(getAdapterPosition()).getAsin());
                productSearchUrl.setUrl(videoArrayList.get(getAdapterPosition()).getProductUrl());
                ArrayList<String> images = new ArrayList<>();
                images.add(videoArrayList.get(getAdapterPosition()).getProductThumbnailUrl());
                productSearchUrl.setImages(images);

                mContext.startActivity(new Intent(mContext, Product_Activity.class).
                        putExtra("product", productSearchUrl));
            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
//        holder.andExoPlayerView.stopPlayback();
//        holder.andExoPlayerView.getPlayer().stop();
//        holder.andExoPlayerView.getPlayer().release();
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

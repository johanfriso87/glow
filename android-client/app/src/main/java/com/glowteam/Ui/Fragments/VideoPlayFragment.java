package com.glowteam.Ui.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.glowteam.Adapters.CommentsAdapter;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.CommentsItem;
import com.glowteam.Models.CommentsTempItem;
import com.glowteam.Models.UserId;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.glowteam.Ui.Activity.ShareActivity;
import com.glowteam.Ui.Activity.UserProfileActivity;
import com.glowteam.Ui.Activity.VideoPlayActivity;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.potyvideo.library.AndExoPlayerView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.markormesher.android_fab.FloatingActionButton;
import uk.co.markormesher.android_fab.SpeedDialMenuAdapter;
import uk.co.markormesher.android_fab.SpeedDialMenuItem;

import static android.app.Activity.RESULT_OK;
import static com.glowteam.Constant.Constant.BASE_URL;

public class VideoPlayFragment extends NewBaseFragment {
    int position;
    //    private VideoView videoview;
    private VideoLikeCommentsFragment videoLikeCommentsFragment;
    ArrayList<Video> videoArrayList;

    public AndExoPlayerView andExoPlayerView;
    ImageView mBack;
    CircularImageView mImg;
    CustomTextView mName;
    FloatingActionButton mFab;

    ImageView mPImage, mLike, mLike1;
    CustomTextView mBName, mPName, mCReplies, mSend,mSend1, mBuy,mMyself;
    EditText mCommentTxt,mCommentTxt1;
    CardView cmntViewold;
    RecyclerView mCommentsView;
    ArrayList<CommentsItem> commentsItemArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    CommentsAdapter commentsAdapter;
    Video mVideo;
    int mReplyPosition = -1;
    AnimateHorizontalProgressBar progressBar;
    long timeInmillisec = -1;
    boolean isLiked = false;
    boolean isSubscribe = false;
    LinearLayout mProfile;
    ImageView mArrow;
    SlidingUpPanelLayout mLayout;
    ImageView mSub;
//    VideoView videoView;

    //    private MKPlayer player;
//    boolean isComplete = false;
    View rootView;
    ViewPager viewPager;
    boolean isChanged = false;

    public VideoPlayFragment(int position, ArrayList<Video> videoArrayList, ViewPager viewPager) {
        this.position = position;
        this.videoArrayList = videoArrayList;
        this.viewPager = viewPager;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_play, container, false);


        mPImage = rootView.findViewById(R.id.p_img);
        mArrow = rootView.findViewById(R.id.arrow);
        mBName = rootView.findViewById(R.id.brand_name);
        mPName = rootView.findViewById(R.id.p_name);
        mProfile = rootView.findViewById(R.id.profileView);
        mCReplies = rootView.findViewById(R.id.c_count);
        mCommentsView = rootView.findViewById(R.id.commentsView);
        mBuy = rootView.findViewById(R.id.buy);
        mMyself = rootView.findViewById(R.id.my_shelf);
        mSub = rootView.findViewById(R.id.subscribe);
        mLike = rootView.findViewById(R.id.like);
        mLike1 = rootView.findViewById(R.id.like1);
        mSend = rootView.findViewById(R.id.send);
        mSend1 = rootView.findViewById(R.id.send1);
        mCommentTxt = rootView.findViewById(R.id.comment_txt);
        mCommentTxt1 = rootView.findViewById(R.id.comment_txt1);
        cmntViewold = rootView.findViewById(R.id.cmntViewold);
        mFab = rootView.findViewById(R.id.fab);
        FabAdapter fabAdapter = new FabAdapter();
        mFab.setSpeedDialMenuAdapter(fabAdapter);
        mFab.setContentCoverColour(0x00FFFFFF);

        mLayout = rootView.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d("sliding", "sliding");
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mArrow.setRotation(180);
                    cmntViewold.setVisibility(View.GONE);
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mArrow.setRotation(0);
                    cmntViewold.setVisibility(View.VISIBLE);
                }
            }
        });

//        videoView = rootView.findViewById(R.id.video_view);


        andExoPlayerView = rootView.findViewById(R.id.andExoPlayerView);
        andExoPlayerView.setOnPlayerListener(new AndExoPlayerView.OnPlayerListener() {
            @Override
            public void OnStart() {
                timeInmillisec = andExoPlayerView.getVideoDuration();
            }

            @Override
            public void OnStop() {
                Log.d("i am here", "i am her 2");
                Log.e("i am here", "i am her 2");
                progressBar.setProgress(100);
                progressBar.removeCallbacks(onEverySecond);
                if (!isChanged) {
                    isChanged = true;
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
//                videoStopEvent.videoStop(position);
            }
        });


//        play();
        mProfile.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), UserProfileActivity.class);
            i.putExtra("userId", mVideo.getUserId().getId());
            i.putExtra("isSubscribed", isSubscribe);
            startActivityForResult(i, 2121);
        });

        mSub.setOnClickListener(v -> {
            if (!isSubscribe) {
                subscribeUser(mVideo.getUserId().getId());
            } else {
                unSubscribeUser(mVideo.getUserId().getId());
            }
        });


        mArrow.setOnClickListener(v -> {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                cmntViewold.setVisibility(View.VISIBLE);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mArrow.setRotation(0);

            } else if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                cmntViewold.setVisibility(View.GONE);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                mArrow.setRotation(180);

            }
        });


//        videoLikeCommentsFragment = VideoLikeCommentsFragment.newInstance(videoArrayList.get(position));
        mVideo = videoArrayList.get(position);

        mBack = rootView.findViewById(R.id.back);
        mImg = rootView.findViewById(R.id.img);
        mName = rootView.findViewById(R.id.name);
//        videoLikeCommentsFragment = VideoLikeCommentsFragment.newInstance(videoArrayList.get(position));


//        andExoPlayerView.setPlayWhenReady(false);


        progressBar = rootView.findViewById(R.id.animate_progress_bar);
        progressBar.setMax(100);
        progressBar.postDelayed(onEverySecond, 1000);

        if (!TextUtils.isEmpty(videoArrayList.get(position).getUserId().getProfilePic())) {
            Glide.with(this).load(BASE_URL + videoArrayList.get(position).getUserId().getProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.user).error(R.drawable.user).into(mImg);
        } else {
            mImg.setImageResource(R.drawable.user);
        }
        if (!TextUtils.isEmpty(videoArrayList.get(position).getUserId().getName())) {
            mName.setText(videoArrayList.get(position).getUserId().getName());
        } else {
            mName.setText("Unknown");
        }

        mBack.setOnClickListener(v -> {
            getActivity().finish();
        });
        init();
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mCommentTxt.getWindowToken(), 0);
            }
        }

        return rootView;
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if (progressBar != null && timeInmillisec != -1) {
                long l = andExoPlayerView.getCurrentPlayerPosition();
//                long l = player.getCurrentPosition();
//                long l = videoView.getPlayer().getCurrentPosition();
                int p = (int) ((100 * l) / timeInmillisec);
                if (p != 0) {
                    progressBar.setProgressWithAnim(p);
                }
                progressBar.postDelayed(onEverySecond, 1000);
            }


        }
    };


    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mCommentsView.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(getActivity(), commentsItemArrayList);
        commentsAdapter.setOnReplyClickListener(onReplyClickListener);
        mCommentsView.setAdapter(commentsAdapter);

        if (mVideo != null) {
            if (!TextUtils.isEmpty(mVideo.getThumbnailUrl())) {
                Glide.with(getActivity()).load(mVideo.getProductThumbnailUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(mPImage);
            }
            if (!TextUtils.isEmpty(mVideo.getProductName())) {
                mPName.setText(mVideo.getProductName());
            }

            if (isNetworkAvailable(getActivity())) {
                showProgressDialog();
                getComments(mVideo.getId());
//                    getComments("5f1acf058722c76b0b381456");
            } else {
                showToast("No Internet connection!!!");
            }
        }


        mSend.setOnClickListener(v -> {
            String comment = mCommentTxt.getText().toString().trim();
            if (TextUtils.isEmpty(comment)) {
                showToast("Please enter comment!");
                return;
            }
            if (mVideo != null) {
                if (mReplyPosition != -1 && !TextUtils.isEmpty(commentsItemArrayList.get(mReplyPosition).getUserId().getName())
                        && comment.contains(commentsItemArrayList.get(mReplyPosition).getUserId().getName())) {
                    addCommentsReply(commentsItemArrayList.get(mReplyPosition).get_id(), comment);
                } else {
                    addComments(mVideo.getId(), comment);
                }
            }

        });

        mSend1.setOnClickListener(v -> {
            String comment = mCommentTxt1.getText().toString().trim();
            if (TextUtils.isEmpty(comment)) {
                showToast("Please enter comment!");
                return;
            }
            if (mVideo != null) {
                if (mReplyPosition != -1 && !TextUtils.isEmpty(commentsItemArrayList.get(mReplyPosition).getUserId().getName())
                        && comment.contains(commentsItemArrayList.get(mReplyPosition).getUserId().getName())) {
                    addCommentsReply(commentsItemArrayList.get(mReplyPosition).get_id(), comment);
                } else {
                    addComments(mVideo.getId(), comment);
                }
            }

        });
        mLike.setOnClickListener(v -> {
            if (!isLiked) {
                addLike(mVideo.getId());
            } else {
                removeLike(mVideo.getId());
            }
        });
        mLike1.setOnClickListener(v -> {
            if (!isLiked) {
                addLike(mVideo.getId());
            } else {
                removeLike(mVideo.getId());
            }
        });
        mBuy.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mVideo.getProductUrl()));
            try {
                startActivity(i);
            } catch (Exception e) {
                showToast("No app found to open this url!");
                e.printStackTrace();
            }
        });
        mMyself.setOnClickListener(v -> {
            String asin = mVideo.getAsin();
            String title = mVideo.getProductName();
            String url = mVideo.getProductUrl();
            String images = mVideo.getProductThumbnailUrl();
            String brand = "";
            addToMyself(asin,title,url,images,brand);
        });
        mFab.setOnClickListener(v -> {

        });
    }


//    private void play() {
//        showProgressDialog();
//        player = new MKPlayer(rootView, getActivity());
//        player.onComplete(() -> {
////            //callback when video is finish
////            Toast.makeText(getActivity(), "video play completed", Toast.LENGTH_SHORT).show();
//            isComplete = true;
//        }).onInfo((what, extra) -> {
//            switch (what) {
//                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
////                    if (!isComplete) {
////                        showProgressDialog();
////                    }
//                    //do something when buffering start
//                    break;
//                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
//
//                    //do something when buffering end
//                    break;
//                case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
//                    //download speed
//                    break;
//                case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                    dismissProgressDialog();
//                    //do something when video rendering
//                    break;
//            }
//        }).onError(new MKPlayer.OnErrorListener() {
//            @Override
//            public void onError(int what, int extra) {
//                Toast.makeText(getActivity(), "video play error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onResume() {
        Log.d("line 349", "lien 349");
        if (mVideo != null) {
            getViewed(mVideo.getId());
        }
        if (andExoPlayerView != null) {
            if (videoArrayList.get(position).getVideoUrl().contains(BASE_URL)) {
                andExoPlayerView.setSource(videoArrayList.get(position).getVideoUrl());
            } else {
                andExoPlayerView.setSource(BASE_URL + videoArrayList.get(position).getVideoUrl());
            }
            andExoPlayerView.setPlayWhenReady(true);
        }

//        if (videoView != null) {
//            videoView.setVideoPath(BASE_URL + videoArrayList.get(position).getVideoUrl()).getPlayer().start();
//        }

//        if (player != null) {
////            if (!TextUtils.isEmpty(player.url)) {
////                player.onResume();
////            } else {
//            play();
//            player.play(BASE_URL + videoArrayList.get(position).getVideoUrl());
////            }
//        }
        progressBar.postDelayed(onEverySecond, 1000);
//        if (!videoLikeCommentsFragment.isAdded()) {
//            videoLikeCommentsFragment.show(getActivity().getSupportFragmentManager(), videoLikeCommentsFragment.getTag());
//        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (andExoPlayerView != null) {
            andExoPlayerView.setPlayWhenReady(false);
        }

//        if (videoView != null) {
//            if (videoView.getPlayer().canPause()) {
//                videoView.getPlayer().pause();
//            }
//        }

//        if (player != null) {
//            player.onDestroy();
//        }
        progressBar.removeCallbacks(onEverySecond);
//        if (videoLikeCommentsFragment != null && videoLikeCommentsFragment.isAdded()) {
//            videoLikeCommentsFragment.dismiss();
//        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (andExoPlayerView != null) {
            andExoPlayerView.stopPlayer();
        }
//        if (videoView != null && videoView.getPlayer().getVideoInfo().getUri() != null) {
//            videoView.getPlayer().stop();
//            videoView.getPlayer().release();
//        }

//        if (player != null) {
//            player.onDestroy();
//        }
        progressBar.removeCallbacks(onEverySecond);
        super.onDestroy();
    }


    CommentsAdapter.OnReplyClickListener onReplyClickListener = new CommentsAdapter.OnReplyClickListener() {
        @Override
        public void OnReplyClickListener(int position) {
            mReplyPosition = position;
            String comment = mCommentTxt.getText().toString().trim();
            if (!TextUtils.isEmpty(commentsItemArrayList.get(position).getUserId().getName())) {
                if (!TextUtils.isEmpty(comment)) {
                    comment = "@" + commentsItemArrayList.get(position).getUserId().getName().replace(" ", "") + " " + comment;
                    mCommentTxt.setText(comment);
                    mCommentTxt.setSelection(comment.length());
                } else {
                    mCommentTxt.setText("@" + commentsItemArrayList.get(position).getUserId().getName().replace(" ", "") + " ");
                    mCommentTxt.setSelection(commentsItemArrayList.get(position).getUserId().getName().replace(" ", "").length() + 2);
                }
            }
        }
    };


    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        dismissProgressDialog();
        try {
            if (type == ApiCall.GET_COMMENTS) {
                commentsItemArrayList.clear();
                JSONArray array = o.getJSONArray("data");
                isLiked = o.getBoolean("isLiked");
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    CommentsItem item = gson.fromJson(array.getJSONObject(i).toString(), CommentsItem.class);
                    commentsItemArrayList.add(item);
                }
                commentsAdapter.notifyDataSetChanged();
                mCReplies.setText(commentsItemArrayList.size() + " Replies");
                if (isLiked) {
                    mLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.google_red));
                } else {
                    mLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_grey));
                }
            }

            if (type == ApiCall.ADD_COMMENT) {
                JSONObject array = o.getJSONObject("data");
                Gson gson = new Gson();
                CommentsTempItem item = gson.fromJson(array.toString(), CommentsTempItem.class);
                CommentsItem commentsItem = new CommentsItem();
                commentsItem.set_id(item.get_id());
                commentsItem.set__v(item.get__v());
                commentsItem.setComment(item.getComment());
                commentsItem.setReplay(item.getReplay());
                UserId userId = new UserId();
                userId.setId(item.getUserId());
                userId.setName(getMainUser().getUserName());
                userId.setProfilePic(getMainUser().getProfilePic());
                commentsItem.setUserId(userId);
                commentsItem.setVideoId(item.getVideoId());
                commentsItemArrayList.add(commentsItem);
                commentsAdapter.notifyDataSetChanged();
                mCReplies.setText(commentsItemArrayList.size() + " Replies");
            }
            if (type == ApiCall.ADD_COMMENT_REPLY) {
                JSONObject array = o.getJSONObject("data");
                Gson gson = new Gson();
                CommentsTempItem item = gson.fromJson(array.toString(), CommentsTempItem.class);
                CommentsItem commentsItem = new CommentsItem();
                commentsItem.set_id(item.get_id());
                commentsItem.set__v(item.get__v());
                commentsItem.setComment(item.getComment());
                commentsItem.setReplay(item.getReplay());
                UserId userId = new UserId();
                userId.setId(item.getUserId());
                userId.setName(getMainUser().getUserName());
                userId.setProfilePic(getMainUser().getProfilePic());
                commentsItem.setUserId(userId);
                commentsItem.setVideoId(item.getVideoId());
                commentsItemArrayList.get(mReplyPosition).getReplay().add(commentsItem);
                commentsAdapter.notifyDataSetChanged();
                mCReplies.setText(commentsItemArrayList.size() + " Replies");
            }
            if (type == ApiCall.ADD_LIKE) {
                isLiked = true;
                mLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.google_red));
            }
            if (type == ApiCall.REMOVE_LIKE) {
                isLiked = false;
                mLike.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_grey));
            }
            if (type == ApiCall.SUBSCRIBE_USER) {
                isSubscribe = true;
                FabAdapter fabAdapter = new FabAdapter();
                mFab.setSpeedDialMenuAdapter(fabAdapter);
                mSub.setImageResource(R.drawable.tick);
            }
            if (type == ApiCall.UNSUBSCRIBE_USER) {
                isSubscribe = false;
                FabAdapter fabAdapter = new FabAdapter();
                mFab.setSpeedDialMenuAdapter(fabAdapter);
                mSub.setImageResource(R.drawable.add);
            }
            if(type == ApiCall.ADD_TO_MYSELF){
                String msg = o.getString("msg");
                showToast(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2121 && resultCode == RESULT_OK && data != null) {
            isSubscribe = data.getBooleanExtra("isSubscribed", isSubscribe);
            if (isSubscribe) {
                FabAdapter fabAdapter = new FabAdapter();
                mFab.setSpeedDialMenuAdapter(fabAdapter);
            } else {
                FabAdapter fabAdapter = new FabAdapter();
                mFab.setSpeedDialMenuAdapter(fabAdapter);
            }
        }
    }

    public class FabAdapter extends SpeedDialMenuAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getBackgroundColour(int position) {
            return ContextCompat.getColor(getActivity(), R.color.white);
        }

        @Override
        public boolean onMenuItemClick(int position) {

            switch (position) {
                case 0:
                    startActivity(new Intent(getActivity(), ShareActivity.class)
                            .putExtra("path", BASE_URL + mVideo.getVideoUrl())
                            .putExtra("stickerUrl", mVideo.getProductThumbnailUrl()));
                    break;
                case 1:
                    if (!isSubscribe) {
                        subscribeUser(mVideo.getUserId().getId());
                    } else {
                        unSubscribeUser(mVideo.getUserId().getId());
                    }
                    break;
                case 2:
                    break;
            }

            return super.onMenuItemClick(position);
        }

        @NotNull
        @Override
        public SpeedDialMenuItem getMenuItem(@NotNull Context context, int i) {
            switch (i) {
                case 0:
                    return new SpeedDialMenuItem(context, R.drawable.share1, "Share Video");
                case 1:
                    return new SpeedDialMenuItem(context, isSubscribe ? R.drawable.tick : R.drawable.add, isSubscribe ? "Unsubscribe" : "Subscribe to user");
                case 2:
                    return new SpeedDialMenuItem(context, R.drawable.alert, "Report Video");
            }
            throw new IllegalArgumentException("No menu item: $position");
        }
    }
}

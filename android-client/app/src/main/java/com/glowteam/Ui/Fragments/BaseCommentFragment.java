package com.glowteam.Ui.Fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Adapters.CommentsAdapter;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.CommentsItem;
import com.glowteam.Models.CommentsTempItem;
import com.glowteam.Models.UserId;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.glowteam.Constant.Constant.BASE_URL;

public class BaseCommentFragment extends NewBaseFragment {


    ImageView mPImage, mLike;
    CustomTextView mBName, mPName, mCReplies, mSend;
    EditText mCommentTxt;
    RecyclerView mCommentsView;
    ArrayList<CommentsItem> commentsItemArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    CommentsAdapter commentsAdapter;
    Video mVideo;
    int mReplyPosition = -1;

    public static VideoLikeCommentsFragment newInstance(Video video) {
        VideoLikeCommentsFragment f = new VideoLikeCommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable("videoData", video);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mCommentsView.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(getActivity(), commentsItemArrayList);
        commentsAdapter.setOnReplyClickListener(onReplyClickListener);
        mCommentsView.setAdapter(commentsAdapter);

        if (getArguments() != null) {
            mVideo = (Video) getArguments().getSerializable("videoData");
            if (mVideo != null) {
                if (!TextUtils.isEmpty(mVideo.getThumbnailUrl())) {
                    Glide.with(getActivity()).load(BASE_URL + mVideo.getThumbnailUrl())
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
        mLike.setOnClickListener(v -> {

        });
        super.onViewCreated(view, savedInstanceState);
    }

    CommentsAdapter.OnReplyClickListener onReplyClickListener = new CommentsAdapter.OnReplyClickListener() {
        @Override
        public void OnReplyClickListener(int position) {
            mReplyPosition = position;
            String comment = mCommentTxt.getText().toString().trim();
            if (!TextUtils.isEmpty(commentsItemArrayList.get(position).getUserId().getName())) {
                if (!TextUtils.isEmpty(comment)) {
                    comment = "@" + commentsItemArrayList.get(position).getUserId().getName().replace(" ","") + " " + comment;
                    mCommentTxt.setText(comment);
                    mCommentTxt.setSelection(comment.length());
                } else {
                    mCommentTxt.setText("@" + commentsItemArrayList.get(position).getUserId().getName().replace(" ","") + " ");
                    mCommentTxt.setSelection(commentsItemArrayList.get(position).getUserId().getName().replace(" ","").length() + 2);
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
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    CommentsItem item = gson.fromJson(array.getJSONObject(i).toString(), CommentsItem.class);
                    commentsItemArrayList.add(item);
                }
                commentsAdapter.notifyDataSetChanged();
                mCReplies.setText(commentsItemArrayList.size() + " Replies");
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.glowteam.Ui.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

public class VideoLikeCommentsFragment extends BaseBottomFragment {

    private BottomSheetDialog bottomSheetDialog;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_like, container, false);

        mPImage = view.findViewById(R.id.p_img);
        mBName = view.findViewById(R.id.brand_name);
        mPName = view.findViewById(R.id.p_name);
        mCReplies = view.findViewById(R.id.c_count);
        mCommentsView = view.findViewById(R.id.commentsView);

        mLike = view.findViewById(R.id.like);
        mSend = view.findViewById(R.id.send);
        mCommentTxt = view.findViewById(R.id.comment_txt);

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

        return view;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        if (layoutParams != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            layoutParams.height = (int) (height * 0.8);
            behavior.setPeekHeight((int) (height * 0.15));
            behavior.setHideable(false);
        }
        bottomSheet.setLayoutParams(layoutParams);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.setCancelable(false);
//                bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

//        getView().post(() -> {
//            Window dialogWindow = bottomSheetDialog.getWindow();
//            // Make the dialog possible to be outside touch
//            dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            getView().invalidate();
//        });


        bottomSheetDialog.setOnKeyListener((arg0, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                getActivity().finish();
                bottomSheetDialog.dismiss();
            }
            return true;
        });
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

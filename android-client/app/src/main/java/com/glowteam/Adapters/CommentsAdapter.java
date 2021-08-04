package com.glowteam.Adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.CommentsItem;
import com.glowteam.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.glowteam.Constant.Constant.BASE_URL;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private ArrayList<CommentsItem> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private OnReplyClickListener onReplyClickListener;

    public CommentsAdapter(Context context, ArrayList<CommentsItem> Items) {
        mContext = context;
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
                .inflate(R.layout.item_comments, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getUserId().getProfilePic())) {
            Glide.with(mContext).load(BASE_URL + mItems.get(holder.getAdapterPosition()).getUserId().getProfilePic())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mImg);
        }
        String name;
        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getUserId().getName())) {
            name = "" + mItems.get(holder.getAdapterPosition()).getUserId().getName();
        } else {
            name = "Unknown";
        }
        String s = name + " "
                + mItems.get(holder.getAdapterPosition()).getComment();
        final SpannableStringBuilder sb = new SpannableStringBuilder(s);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(bss, 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.mComment.setText(sb);
        if (mItems.get(holder.getAdapterPosition()).getReplay() != null &&
                mItems.get(holder.getAdapterPosition()).getReplay().size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            holder.mRepliesView.setLayoutManager(linearLayoutManager);
            RepliesAdapter repliesAdapter = new RepliesAdapter(mContext, mItems.get(holder.getAdapterPosition()).getReplay());
            holder.mRepliesView.setAdapter(repliesAdapter);
        }
        holder.mReply.setOnClickListener(v->{
                 if (onReplyClickListener != null){
                     onReplyClickListener.OnReplyClickListener(holder.getAdapterPosition());
                 }
        });

    }

    public interface OnReplyClickListener{
        void OnReplyClickListener(int position);
    }
    public void setOnReplyClickListener(OnReplyClickListener onReplyClickListener){
        this.onReplyClickListener = onReplyClickListener;
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mComment, mReply;
        CircularImageView mImg;
        RecyclerView mRepliesView;
        LinearLayoutManager linearLayoutManager;

        ViewHolder(final View itemView) {
            super(itemView);
            mComment = itemView.findViewById(R.id.comment);
            mReply = itemView.findViewById(R.id.reply);
            mImg = itemView.findViewById(R.id.u_img);
            mRepliesView = itemView.findViewById(R.id.repliesView);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

package com.glowteam.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder> {

    private ArrayList<CommentsItem> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public RepliesAdapter(Context context, ArrayList<CommentsItem> Items) {
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
                .inflate(R.layout.item_reply, parent, false);
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
        String cmnt = mItems.get(holder.getAdapterPosition()).getComment();
        if (cmnt.contains("@")) {
            String[] names = cmnt.split("@");
            ArrayList<String> strings = new ArrayList<>();
            int i = name.length();
            for (String n : names) {
                if (!TextUtils.isEmpty(n)) {
                    if (n.contains(" ")) {
                        strings.add(n.substring(0, n.indexOf(" ")));
                    } else {
                        strings.add(n);
                    }
                }
            }
            for (String n : strings) {
                n = "@" + n;
                int d = s.indexOf(n);
                sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)), d, d + n.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        holder.mComment.setText(sb);

//        holder.mComment.setText(mItems.get(holder.getAdapterPosition()).getComment());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mComment, mReply;
        CircularImageView mImg;

        ViewHolder(final View itemView) {
            super(itemView);
            mComment = itemView.findViewById(R.id.comment);
            mReply = itemView.findViewById(R.id.reply);
            mImg = itemView.findViewById(R.id.u_img);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

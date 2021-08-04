package com.glowteam.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.FeaturedArticle;
import com.glowteam.R;
import com.glowteam.Ui.Activity.DocDetailActivity;

import java.util.ArrayList;

import static com.glowteam.Constant.Constant.BASE_URL;

public class FeaturedArticlesAdapter extends RecyclerView.Adapter<FeaturedArticlesAdapter.ViewHolder> {

    private ArrayList<FeaturedArticle> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private boolean isTitleShow;

    public FeaturedArticlesAdapter(Context context, ArrayList<FeaturedArticle> Items, boolean isTitleShow) {
        mContext = context;
        mItems = Items;
        this.isTitleShow = isTitleShow;
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
                .inflate(R.layout.item_featured_artical, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getTitle())) {
            holder.mDocTitle.setText(mItems.get(holder.getAdapterPosition()).getTitle());
        }else{
            holder.mDocTitle.setText("");
        }
        Glide.with(mContext).load(BASE_URL + mItems.get(holder.getAdapterPosition()).getPicture())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mImg);

        holder.mItemView.setOnClickListener(v->{
            Intent i = new Intent(mContext, DocDetailActivity.class);
            i.putExtra("docData",mItems.get(holder.getAdapterPosition()));
            mContext.startActivity(i);
        });


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mTitle, mDocTitle;
        ImageView mImg;
        LinearLayout mItemView;

        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mImg = itemView.findViewById(R.id.img);
            mItemView = itemView.findViewById(R.id.itemView);
            mDocTitle = itemView.findViewById(R.id.doc_title);
            if (isTitleShow) {
                mTitle.setVisibility(View.VISIBLE);
            } else {
                mTitle.setVisibility(View.GONE);
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

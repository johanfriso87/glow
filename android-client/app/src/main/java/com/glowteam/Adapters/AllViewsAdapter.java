package com.glowteam.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Models.AllViewsItem;
import com.glowteam.R;

import java.util.ArrayList;

public class AllViewsAdapter extends RecyclerView.Adapter<AllViewsAdapter.ViewHolder> {

    private ArrayList<AllViewsItem> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public AllViewsAdapter(Context context, ArrayList<AllViewsItem> Items) {
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
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allview, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (!TextUtils.isEmpty(mItems.get(holder.getAdapterPosition()).getTitleName())) {
            if(mItems.get(holder.getAdapterPosition()).getTitleName() == "article" ||
                    mItems.get(holder.getAdapterPosition()).getTitleName() == "Super great spotlight"
                    ||  mItems.get(holder.getAdapterPosition()).getTitleName() == "Your Subscription"
            ){
                holder.mTitle.setText("");
                holder.mTitle.setVisibility(View.GONE);

            }else{
                holder.mTitle.setText(mItems.get(holder.getAdapterPosition()).getTitleName());

            }
        } else {
            holder.mTitle.setVisibility(View.GONE);
        }
        holder.mNoData.setVisibility(View.GONE);
        if (mItems.get(holder.getAdapterPosition()).getFeaturedCreatorItems() != null
                && mItems.get(holder.getAdapterPosition()).getFeaturedCreatorItems().size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            FeaturedCreatorsAdapter featuredCreatorsAdapter = new FeaturedCreatorsAdapter(mContext, mItems.get(holder.getAdapterPosition()).getFeaturedCreatorItems());
            holder.recyclerView.setAdapter(featuredCreatorsAdapter);

        } else if (mItems.get(holder.getAdapterPosition()).getVideoList() != null && mItems.get(holder.getAdapterPosition()).getVideoList().size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            VideoAdapter videoAdapter = new VideoAdapter(mContext, mItems.get(holder.getAdapterPosition()).getVideoList());
            holder.recyclerView.setAdapter(videoAdapter);
        } else if (mItems.get(holder.getAdapterPosition()).getProductItemArrayList() != null && mItems.get(holder.getAdapterPosition()).getProductItemArrayList().size() != 0) {
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
//            holder.recyclerView.setLayoutManager(linearLayoutManager);
//            ProductsAdapter productsAdapter = new ProductsAdapter(mContext, mItems.get(holder.getAdapterPosition()).getProductItemArrayList());
//            holder.recyclerView.setAdapter(productsAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            VideoAdapter videoAdapter = new VideoAdapter(mContext, mItems.get(holder.getAdapterPosition()).getUniqueVideoList());
            holder.recyclerView.setAdapter(videoAdapter);
            Log.e("getAdapterPosition",String.valueOf(holder.getAdapterPosition()));
        } else if (mItems.get(holder.getAdapterPosition()).getFeaturedPlaylistItems() != null && mItems.get(holder.getAdapterPosition()).getFeaturedPlaylistItems().size() != 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false);
            holder.recyclerView.setLayoutManager(gridLayoutManager);
            FeaturedPlaylistsAdapter featuredPlaylistsAdapter = new FeaturedPlaylistsAdapter(mContext, mItems.get(holder.getAdapterPosition()).getFeaturedPlaylistItems());
            holder.recyclerView.setAdapter(featuredPlaylistsAdapter);
                        holder.recyclerView.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
        } else if (mItems.get(holder.getAdapterPosition()).getFeaturedArticleArrayList() != null
                && mItems.get(holder.getAdapterPosition()).getFeaturedArticleArrayList().size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(linearLayoutManager);
            FeaturedArticlesAdapter featuredArticlesAdapter = new FeaturedArticlesAdapter(mContext, mItems.get(holder.getAdapterPosition()).getFeaturedArticleArrayList(), false);
            holder.recyclerView.setAdapter(featuredArticlesAdapter);
            holder.recyclerView.setVisibility(View.GONE);
        } else {
//            holder.mNoData.setVisibility(View.VISIBLE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        RecyclerView recyclerView;
        LinearLayout mNoData;

        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.itemViews);
            mNoData = itemView.findViewById(R.id.no_review);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

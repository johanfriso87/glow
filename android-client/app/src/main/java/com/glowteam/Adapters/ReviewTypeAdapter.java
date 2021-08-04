package com.glowteam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Models.CategoryItem;
import com.glowteam.R;

import java.util.ArrayList;

public class ReviewTypeAdapter extends RecyclerView.Adapter<ReviewTypeAdapter.ViewHolder> {

    private ArrayList<CategoryItem> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ReviewTypeAdapter(Context context, ArrayList<CategoryItem> Items) {
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
                .inflate(R.layout.item_review_type, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.mTitle.setText(mItems.get(holder.getAdapterPosition()).getName());

        if (mItems.get(holder.getAdapterPosition()).isSelected()) {
            holder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        } else {
            holder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.light_text_color));
        }

        holder.mTitle.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.name);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

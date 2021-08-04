package com.glowteam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Models.DropsItem;
import com.glowteam.R;

import java.util.ArrayList;

public class DropsAdapter extends RecyclerView.Adapter<DropsAdapter.ViewHolder> {

    private ArrayList<DropsItem> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public DropsAdapter(Context context, ArrayList<DropsItem> Items) {
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
                .inflate(R.layout.item_drops, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


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

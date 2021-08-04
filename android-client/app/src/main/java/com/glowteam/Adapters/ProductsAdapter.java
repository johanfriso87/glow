package com.glowteam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.glowteam.Models.ProductItem;
import com.glowteam.R;

import java.util.ArrayList;

import static com.glowteam.Constant.Constant.BASE_URL;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<ProductItem> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ProductsAdapter(Context context, ArrayList<ProductItem> Items) {
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
                .inflate(R.layout.item_products, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
if(position == 0){
    Glide.with(mContext).load(R.drawable.img1).into(holder.maining);
}
if(position == 1){
    Glide.with(mContext).load(R.drawable.img2).into(holder.maining);
}
        if(position == 2){
            Glide.with(mContext).load(R.drawable.img3).into(holder.maining);
        }
        if(position == 3){
            Glide.with(mContext).load(R.drawable.img4).into(holder.maining);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView maining;
        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.name);
            maining = itemView.findViewById(R.id.mainimg);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

package com.glowteam.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.Models.User;
import com.glowteam.Models.Video;
import com.glowteam.Network.ApiClient;
import com.glowteam.Network.ApiService;
import com.glowteam.R;
import com.glowteam.Ui.Activity.UserProfileActivity;
import com.glowteam.Ui.Activity.VideoPlayActivity;
import com.whygraphics.gifview.gif.GIFView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.glowteam.Constant.Constant.BASE_URL;

public class SubscribeCreatorsAdapter extends RecyclerView.Adapter<SubscribeCreatorsAdapter.ViewHolder> {

    private ArrayList<User> mItems;
    private Context mContext;
    private FeaturedCreatorsAdapter.OnItemClickListener onItemClickListener;
    ApiService apiService;
    Dialog mProgressDialog, mProgressBarDialog;

    public SubscribeCreatorsAdapter(Context context, ArrayList<User> Items) {
        mContext = context;
        mItems = Items;
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubscribeCreatorsAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mItems.get(holder.getAdapterPosition()).getName());
        Log.d("Data", "onBindViewHolder: ====>" + mItems.get(holder.getAdapterPosition()).getName() + " " + mItems.get(holder.getAdapterPosition()).getProfilePic());
        if (mItems.get(holder.getAdapterPosition()).getProfilePic() == null) {
            Glide.with(mContext).load(R.drawable.people).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image);
        } else {
            Glide.with(mContext).load(BASE_URL + mItems.get(holder.getAdapterPosition()).getProfilePic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image);
        }


        holder.mainCard.setOnClickListener(v -> {
            //subscribe user click

            mContext.startActivity(new Intent(mContext, UserProfileActivity.class).
                    putExtra("userId",  mItems.get(holder.getAdapterPosition()).getId()).putExtra("isSubscribed", true));
//            Intent i = new Intent(mContext, UserProfileActivity.class);
//            i.putExtra("userId",  mItems.get(holder.getAdapterPosition()).getId());
//            i.putExtra("isSubscribed", false);
//            startActivityForResult(i, 2121);
        });
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public SubscribeCreatorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscribed_creator, parent, false);
        return new SubscribeCreatorsAdapter.ViewHolder(inflatedView);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        CircleImageView profile_image;
        CardView mainCard;

        Button subscribeButton;

        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.name);
            profile_image = itemView.findViewById(R.id.profile_image);
            subscribeButton = itemView.findViewById(R.id.subscribe);
            mainCard = itemView.findViewById(R.id.mainCard);
        }

    }
}

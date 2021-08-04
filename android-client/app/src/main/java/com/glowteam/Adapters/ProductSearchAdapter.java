package com.glowteam.Adapters;

import android.Manifest;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Interfaces.Interface;
import com.glowteam.Models.ProductAmazonSearch;
import com.glowteam.R;
import com.glowteam.Ui.Activity.Product_Activity;
import com.glowteam.Ui.Activity.VideoRecordActivity;

import java.util.ArrayList;
import android.os.Handler;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ViewHolder> {
    FragmentActivity activity;
    ArrayList<ProductAmazonSearch> productSearchUrls;
    LayoutInflater inflater;
    int mPosition = -1;
    public OnClickSticker onClickSticker;

    //    boolean isOpen = false;
    public ProductSearchAdapter(FragmentActivity activity, ArrayList<ProductAmazonSearch> productSearchUrls) {
        this.activity = activity;
        this.productSearchUrls = productSearchUrls;
        inflater = LayoutInflater.from(activity);

    }

    public void setListener() {
        Interface.SetOnPermissionGranted(onPermissionGranted);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.product_name.setText(productSearchUrls.get(holder.getAdapterPosition()).getTitle());
//        holder.product_dis.setText(productSearchUrls.get(holder.getAdapterPosition()).getPrice().getCurrent_price() + " " +
//                productSearchUrls.get(holder.getAdapterPosition()).getPrice().getCurrency());
        Glide.with(activity).load(productSearchUrls.get(holder.getAdapterPosition()).getThumbnail()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.product_image);
        holder.product_make.setOnClickListener(v -> {
            //disable button for now
            holder.product_make.setEnabled(false);
//                if(isOpen){
//                    Log.d("testing",String.valueOf(isOpen));
//                    Log.d("testing1","testing1");
//                    return;
//                }
//                isOpen = true;
            int write_storage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read_storage = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int read_camera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            int read_audio = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            if (read_storage == PERMISSION_GRANTED && write_storage == PERMISSION_GRANTED &&
                    read_camera == PERMISSION_GRANTED && read_audio == PERMISSION_GRANTED) {
                String sticker;
//                    if (!TextUtils.isEmpty(productSearchUrls.get(holder.getAdapterPosition()).getSticker())) {
//                        sticker = productSearchUrls.get(holder.getAdapterPosition()).getSticker();
//                    } else {
                sticker = productSearchUrls.get(holder.getAdapterPosition()).getThumbnail();
//                    }
//                    isOpen = false;
                activity.startActivity(new Intent(activity, VideoRecordActivity.class)
                        .putExtra("productId", productSearchUrls.get(holder.getAdapterPosition()).getAsin()).
                                putExtra("product_img", sticker).
                                putExtra("productName", productSearchUrls.get(holder.getAdapterPosition()).getTitle()).
                                putExtra("productUrl", productSearchUrls.get(holder.getAdapterPosition()).getUrl()).
                                putExtra("productThumbnailUrl", productSearchUrls.get(holder.getAdapterPosition()).getThumbnail()));
//                    activity.startActivity(new Intent(activity, VideoRecordActivityNew.class).putExtra("product_img",productSearchUrls.get(holder.getAdapterPosition()).getImage()));

            } else {
//                    isOpen = false;
                mPosition = holder.getAdapterPosition();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 101);
            }
            //Enable button after 1 second
            new Handler().postDelayed(() -> holder.product_make.setEnabled(true),1000);
        });
        holder.save_sticker.setOnClickListener(v -> {
            if (onClickSticker != null) {
                onClickSticker.OnClickStickerDownload(holder.getAdapterPosition());
            }
        });
        holder.product_review.setOnClickListener(v -> activity.startActivity(new Intent(activity, Product_Activity.class).
                putExtra("product", productSearchUrls.get(holder.getAdapterPosition()))));
    }

    Interface.OnPermissionGranted onPermissionGranted = new Interface.OnPermissionGranted() {
        @Override
        public void OnPermissionGranted() {
            if (mPosition != -1) {
                activity.startActivity(new Intent(activity, VideoRecordActivity.class)
                        .putExtra("productId", productSearchUrls.get(mPosition).getAsin()).
                                putExtra("product_img", productSearchUrls.get(mPosition).getThumbnail()).
                                putExtra("productName", productSearchUrls.get(mPosition).getTitle()).
                                putExtra("productUrl", productSearchUrls.get(mPosition).getUrl()).
                                putExtra("productThumbnailUrl", productSearchUrls.get(mPosition).getThumbnail()));
            }
        }
    };

    public interface OnClickSticker {
        void OnClickStickerDownload(int position);
    }

    public void setOnClickSticker(OnClickSticker onClickSticker) {
        this.onClickSticker = onClickSticker;
    }


    @Override
    public int getItemCount() {
        return productSearchUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name, product_dis, product_review;
        Button product_make;
        CustomTextView save_sticker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_dis = itemView.findViewById(R.id.product_dis);
            product_make = itemView.findViewById(R.id.product_make);
            product_review = itemView.findViewById(R.id.product_review);
            save_sticker = itemView.findViewById(R.id.save_sticker);
        }
    }
}

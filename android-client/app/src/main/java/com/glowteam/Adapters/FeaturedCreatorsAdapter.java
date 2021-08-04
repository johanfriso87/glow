package com.glowteam.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Constant.Constant;
import com.glowteam.Models.User;
import com.glowteam.Models.Video;
import com.glowteam.Network.ApiClient;
import com.glowteam.Network.ApiService;
import com.glowteam.R;
import com.glowteam.Ui.Activity.BaseActivity;
import com.glowteam.Ui.Activity.MainActivity;
import com.glowteam.Ui.Activity.UserProfileActivity;
import com.glowteam.Ui.Activity.VideoPlayActivity;
import com.glowteam.Ui.Fragments.NewBaseFragment;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.whygraphics.gifview.gif.GIFView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.glowteam.Constant.Constant.BASE_URL;

public class FeaturedCreatorsAdapter extends RecyclerView.Adapter<FeaturedCreatorsAdapter.ViewHolder> {

    private ArrayList<User> mItems;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    ApiService apiService;
    Dialog mProgressDialog, mProgressBarDialog;

    public FeaturedCreatorsAdapter(Context context, ArrayList<User> Items) {
        mContext = context;
        mItems = Items;
        apiService = ApiClient.getClient().create(ApiService.class);
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
                .inflate(R.layout.item_featured_creator, parent, false);
        return new ViewHolder(inflatedView);
    }

    public SharedPreferences getSharedPreferences() {
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public User getMainUser() {
        String user = getSharedPreferences().getString("UserData", "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, User.class);
        }
        return null;
    }

    public int selectedPosition;

    public void subscribeUser(String id) {
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("targetUserId", id);
        String mBearerToken = "Bearer " + userData.getToken();
        Log.e("id", id);

        Log.e("token", mBearerToken);

        Call<ResponseBody> call = apiService.subscribeUser(mBearerToken, hashMap);
        getApiCall(call, BaseActivity.ApiCall.SUBSCRIBE_USER);
    }

    public void OnResponse(JSONObject o, BaseActivity.ApiCall type) {
        try {
            String s = o.getString("status");
            showToast("User Subscribed!");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog != null) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    public void OnError(String msg, final BaseActivity.ApiCall type) {
        String s = msg;
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    public void getApiCall(Call<ResponseBody> call, final BaseActivity.ApiCall type) {

        showProgressDialog();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String s = responseBody.string();
                        JSONObject jsonObject = new JSONObject(s);
                        Log.d("api status", "onResponse: ====>" + jsonObject.toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            OnResponse(jsonObject, type);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("error")) {
                            showToast(jsonObject.getString("msg"));
                            OnError(jsonObject.getString("msg"), type);
                        }
                    } else {
                        showToast("Oops! Please try again.");
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    Log.e("TAG", "Error :- " + e.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                dismissProgressDialog();
                Log.e("TAG", t.toString());
            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mTitle.setText(mItems.get(holder.getAdapterPosition()).getName());
        Log.d("Data", "onBindViewHolder: ====>" + mItems.get(holder.getAdapterPosition()).getName() + " " + mItems.get(holder.getAdapterPosition()).getProfilePic());
        if (mItems.get(holder.getAdapterPosition()).getProfilePic() == null) {
            Glide.with(mContext).load(R.drawable.people).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image);
        } else {
            Glide.with(mContext).load(BASE_URL + mItems.get(holder.getAdapterPosition()).getProfilePic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_image);
        }


        ArrayList<Video> videoArrayList = mItems.get(holder.getAdapterPosition()).getVideos();
        if (videoArrayList != null && videoArrayList.size() != 0) {
            if (!TextUtils.isEmpty(videoArrayList.get(0).getGif())) {
                holder.gifView1.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VideoPlayActivity.class).
                        putExtra("videoData", mItems.get(holder.getAdapterPosition()).getVideos()).putExtra("pos", 0)));
                holder.gifView1.setGifResource("url:" + Constant.BASE_URL + videoArrayList.get(0).getGif());
                holder.gifView1.setOnSettingGifListener(new GIFView.OnSettingGifListener() {
                    @Override
                    public void onSuccess(GIFView view, Exception e) {
                        holder.image_view1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(GIFView view, Exception e) {

                    }
                });
            }
            if (videoArrayList.size() > 1) {
                if (!TextUtils.isEmpty(videoArrayList.get(1).getGif())) {
                    holder.gifView2.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VideoPlayActivity.class).
                            putExtra("videoData", mItems.get(holder.getAdapterPosition()).getVideos()).putExtra("pos", 1)));
                    holder.gifView2.setGifResource("url:" + Constant.BASE_URL + videoArrayList.get(1).getGif());
                    holder.gifView2.setOnSettingGifListener(new GIFView.OnSettingGifListener() {
                        @Override
                        public void onSuccess(GIFView view, Exception e) {
                            holder.image_view2.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(GIFView view, Exception e) {

                        }
                    });
                }
            }
            if (videoArrayList.size() > 2) {
                if (!TextUtils.isEmpty(videoArrayList.get(2).getGif())) {
                    holder.gifView3.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, VideoPlayActivity.class).
                            putExtra("videoData", mItems.get(holder.getAdapterPosition()).getVideos()).putExtra("pos", 2)));
                    holder.gifView3.setGifResource("url:" + Constant.BASE_URL + videoArrayList.get(2).getGif());
                    holder.gifView3.setOnSettingGifListener(new GIFView.OnSettingGifListener() {
                        @Override
                        public void onSuccess(GIFView view, Exception e) {
                            holder.image_view3.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(GIFView view, Exception e) {

                        }
                    });
                }
            }
        }

        holder.profile_image.setOnClickListener(v -> {
            //subscribe user click

            mContext.startActivity(new Intent(mContext, UserProfileActivity.class).
                    putExtra("userId",  mItems.get(holder.getAdapterPosition()).getId()).putExtra("isSubscribed", false));
//            Intent i = new Intent(mContext, UserProfileActivity.class);
//            i.putExtra("userId",  mItems.get(holder.getAdapterPosition()).getId());
//            i.putExtra("isSubscribed", false);
//            startActivityForResult(i, 2121);
        });
        holder.subscribeButton.setOnClickListener(v -> {
            //subscribe user click
            selectedPosition = position;
            subscribeUser(mItems.get(position).getId());
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        CircleImageView profile_image;
        Button subscribeButton;
        ImageView image_view1, image_view2, image_view3;
        GIFView gifView1, gifView2, gifView3;

        ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.name);
            profile_image = itemView.findViewById(R.id.profile_image);
            subscribeButton = itemView.findViewById(R.id.subscribe);
            image_view1 = itemView.findViewById(R.id.img_view1);
            image_view2 = itemView.findViewById(R.id.img_view2);
            image_view3 = itemView.findViewById(R.id.img_view3);
            gifView1 = itemView.findViewById(R.id.gif_view1);
            gifView2 = itemView.findViewById(R.id.gif_view2);
            gifView3 = itemView.findViewById(R.id.gif_view3);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

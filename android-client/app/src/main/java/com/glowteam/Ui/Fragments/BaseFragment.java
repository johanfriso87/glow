package com.glowteam.Ui.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.glowteam.Models.User;
import com.glowteam.Network.ApiClient;
import com.glowteam.Network.ApiService;
import com.glowteam.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseFragment extends Fragment {

    private Dialog mProgressDialog;
    private ApiService apiService;
    private static final String TAG = "BaseFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient().create(ApiService.class);
        createProgressDialog();
    }

    public User getMainUser() {
        String user = getSharedPreferences().getString("UserData", "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, User.class);
        }
        return null;
    }

    public SharedPreferences getSharedPreferences() {
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    public void setMainUser(String UserData) {
        SharedPreferences.Editor editor1 = getSharedPreferences().edit();
        editor1.putString("UserData", UserData);
        editor1.apply();
    }

    public void getDashboard() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.dashboardGet(mBearerToken);
        getApiCall(call, ApiCall.DASHBOARD_GET);
    }

    public void getUserVideos() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getUserVideo(mBearerToken);
        getApiCall(call, ApiCall.GET_USER_VIDEOS);
    }

    public void getUserLikedVideos() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getLikedVideo(mBearerToken);
        getApiCall(call, ApiCall.GET_USER_LIKED_VIDEOS);
    }

    public void getUSersMyselfProduct() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getMyselfProducts(mBearerToken);
        getApiCall(call, ApiCall.GET_USER_MYSELF_PRODUCTS);
    }
    public void getUserSubscribedVideos() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getSubscribedVideo(mBearerToken);
        getApiCall(call, ApiCall.GET_USER_SUBSCRIBED_VIDEOS);
    }

    public void getrSubscribedUse() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getSubscribedusers(mBearerToken);
        getApiCall(call, ApiCall.GET_SUBSCRIBED_USERS);
    }

    public void getBlogs() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getBlogs(mBearerToken);
        getApiCall(call, ApiCall.GET_BLOGS);
    }

    public enum ApiCall {
        DASHBOARD_GET, GET_USER_VIDEOS, VIEWED_VIDEO, GET_BLOGS, GET_USER_LIKED_VIDEOS ,GET_USER_SUBSCRIBED_VIDEOS,GET_USER_MYSELF_PRODUCTS,GET_SUBSCRIBED_USERS
    }

    public void getApiCall(Call<ResponseBody> call, final ApiCall type) {
        if (isNetworkAvailable(getActivity())) {
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
                            Log.d(TAG, "onResponse: ====>" + jsonObject.toString());
                            if (jsonObject.getString("status").replace(".", "").equalsIgnoreCase("success")) {
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
                        Log.e(TAG, "Error :- " + e.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    dismissProgressDialog();
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            open(call, type);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void createProgressDialog() {
        this.mProgressDialog = new Dialog(getActivity());
        this.mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.mProgressDialog.setContentView(R.layout.progress_dialog);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
//        this.mProgressDialogWhite.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                return keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
    }

    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            String s = o.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void OnError(String msg, final ApiCall type) {
        String s = msg;
    }


    public void showProgressDialog() {
        if (mProgressDialog != null) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
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

    private void open(Call<ResponseBody> call, final ApiCall type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(getString(R.string.noInternet));
        alertDialogBuilder.setPositiveButton(getString(R.string.Cancel), (dialog, arg1) -> dialog.dismiss());

        alertDialogBuilder.setNegativeButton(getString(R.string.Retry), (dialog, which) -> {
            dialog.dismiss();
            getApiCall(call, type);
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}

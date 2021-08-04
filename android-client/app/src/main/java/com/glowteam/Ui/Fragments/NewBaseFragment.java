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

import com.glowteam.Models.Search;
import com.glowteam.Models.User;
import com.glowteam.Network.ApiClient;
import com.glowteam.Network.ApiService;
import com.glowteam.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBaseFragment extends Fragment {
    private Dialog mProgressDialog;
    private ApiService apiService;
    private static final String TAG = "BaseFragment";
    private ApiService apiService1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient().create(ApiService.class);
        apiService1 = ApiClient.getAmzonClient().create(ApiService.class);
        createProgressDialog();
    }

    public User getMainUser() {
        String user = getSharedPreferences().getString("UserData", "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, User.class);
        }
        return null;
    }

    public String getNameFromURL(String mMainUrl) {
        URL url;
        try {
            url = new URL(mMainUrl);
            String host = url.getHost();
            String[] res = host.split("(\\.|//)+(?=\\w)");
            if (res.length == 4) {
                return res[3];
            } else {
                return res[2];
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCountryFromUrl(String type) {
        if (TextUtils.isEmpty(type) || type.equalsIgnoreCase("com")) {
            return "US";
        } else {
            return type.toUpperCase();
        }
    }

    public String getASIN(String url) {
        String Regx = ".*/([\\w-]+/)?(dp|gp/product)/(\\w+/)?(\\w{10})";
        Pattern regEx = Pattern.compile(Regx);
        Matcher m = regEx.matcher(url);
        if (m.find()) {
            return m.group(4);
        }
        return "";
    }

    public SharedPreferences getSharedPreferences() {
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    public void setMainUser(String UserData) {
        SharedPreferences.Editor editor1 = getSharedPreferences().edit();
        editor1.putString("UserData", UserData);
        editor1.apply();
    }

    public void productSearchUrl(String url) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Search search = new Search();
        search.setUrl(url);
//        Call<ResponseBody> call = apiService.productSearchUrl(mBearerToken, search);
//        getApiCall(call, ApiCall.PRODUCT_SEARCH_URL);
    }

    public void productSearch(String name) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
//        String mBearerToken = "Bearer U2FsdGVkX1/K9bvVTlxOyK4tT0elLBfz6NfJjS12fcdxWTlOokin8lO2tZu9GRaf";
        Search search = new Search();
        search.setQ(name);
        search.setPage("0");
        search.setDataPerPage(20);
        Call<ResponseBody> call = apiService.productSearch(mBearerToken, search);
        getApiCall(call, ApiCall.PRODUCT_SEARCH);

    }

    public void getComments(String videoId) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        HashMap<String, String> map = new HashMap<>();
        map.put("videoId", videoId);
        Call<okhttp3.ResponseBody> call = apiService.getComments(mBearerToken, map);
        getApiCall(call, ApiCall.GET_COMMENTS);
    }

    public void addComments(String videoId, String comment) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        HashMap<String, String> map = new HashMap<>();
        map.put("videoId", videoId);
        map.put("comment", comment);
        Call<okhttp3.ResponseBody> call = apiService.addComments(mBearerToken, map);
        getApiCall(call, ApiCall.ADD_COMMENT);
    }

    public void addCommentsReply(String videoId, String comment) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        HashMap<String, String> map = new HashMap<>();
        map.put("commentId", videoId);
        map.put("comment", comment);
        Call<okhttp3.ResponseBody> call = apiService.addCommentsReply(mBearerToken, map);
        getApiCall(call, ApiCall.ADD_COMMENT_REPLY);
    }

    public void addLike(String videoId) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        HashMap<String, String> map = new HashMap<>();
        map.put("videoId", videoId);
        Call<okhttp3.ResponseBody> call = apiService.addLike(mBearerToken, map);
        getApiCall(call, ApiCall.ADD_LIKE);
    }

    public void removeLike(String videoId) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        HashMap<String, String> map = new HashMap<>();
        map.put("videoId", videoId);
        Call<okhttp3.ResponseBody> call = apiService.removeLike(mBearerToken, map);
        getApiCall(call, ApiCall.REMOVE_LIKE);
    }

    public void getViewed(String id) {
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("_id", id);
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.viewVideo(mBearerToken, hashMap);
        getApiCall(call, ApiCall.VIEWED_VIDEO);
    }

    public void subscribeUser(String id) {
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("targetUserId", id);
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.subscribeUser(mBearerToken, hashMap);
        getApiCall(call, ApiCall.SUBSCRIBE_USER);
    }

    public void addToMyself(String asin,String title,String url,String images,String brand){
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("asin", asin);
        hashMap.put("title", title);
        hashMap.put("url", url);
        hashMap.put("images", images);
        hashMap.put("brand", brand);
        String mBearerToken = "Bearer " + userData.getToken();
        Log.e("token",mBearerToken);
        Call<ResponseBody> call = apiService.addToMyself(mBearerToken, hashMap);
        getApiCall(call, ApiCall.ADD_TO_MYSELF);
    }

    public void unSubscribeUser(String id) {
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("targetUserId", id);
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.unSubscribeUser(mBearerToken, hashMap);
        getApiCall(call, ApiCall.UNSUBSCRIBE_USER);
    }

    public void productSearchAmazon(String name) {
        String locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getActivity().getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            locale = getActivity().getResources().getConfiguration().locale.getCountry();
        }
        Call<ResponseBody> call = apiService1.productSearchAmazon(locale, name);
        getApiCall(call, ApiCall.PRODUCT_SEARCH_AMAZON);
    }

    public void productDetailAmazon(String url) {
        String type = getNameFromURL(url);
        String country = getCountryFromUrl(type);
        String asin = getASIN(url);
        Call<ResponseBody> call = apiService1.productDetailAmazon(country, asin);
        getApiCall(call, ApiCall.PRODUCT_DETAIL_AMAZON);
    }

    public enum ApiCall {
        SUBSCRIBE_USER,ADD_TO_MYSELF, UNSUBSCRIBE_USER, VIEWED_VIDEO, PRODUCT_SEARCH_URL, PRODUCT_SEARCH, PRODUCT_SEARCH_AMAZON, PRODUCT_DETAIL_AMAZON, GET_COMMENTS, ADD_COMMENT, ADD_COMMENT_REPLY, ADD_LIKE, REMOVE_LIKE
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
//                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            OnResponse(jsonObject, type);
//                            } else if (jsonObject.getString("status").equalsIgnoreCase("error")) {
//                                showToast(jsonObject.getString("msg"));
//                                OnError(jsonObject.getString("msg"), type);
//                            }
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

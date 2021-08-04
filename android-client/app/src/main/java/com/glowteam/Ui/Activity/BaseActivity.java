package com.glowteam.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.glowteam.Models.User;
import com.glowteam.Network.ApiClient;
import com.glowteam.Network.ApiService;
import com.glowteam.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    Dialog mProgressDialog, mProgressBarDialog;
    private String TAG = "BaseActivity";
    ApiService apiService;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient().create(ApiService.class);
        createProgressDialog();
        createProgressBar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

//    public File getDefaultFile(){
//        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        File file = new File(temp, "Glow");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        return file;
//    }

    public File getDefaultFile_(){
        File temp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(temp, "Glow");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getDefaultVideoFile(){
        File temp = getDefaultFile_();
        File file = new File(temp, "Video");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getDefaultStickerFile(){
        File temp = getDefaultFile_();
        File file = new File(temp, "Stickers");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public SharedPreferences getSharedPreferences() {
        return androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
    }

    public User getMainUser() {
        String user = getSharedPreferences().getString("UserData", "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, User.class);
        }
        return null;
    }

    public void setMainUser(String UserData) {
        SharedPreferences.Editor editor1 = getSharedPreferences().edit();
        editor1.putString("UserData", UserData);
        editor1.apply();
    }

    public void showAlertDialog(String title, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
//            if(googleApiAvailability.isUserResolvableError(status)) {
//                googleApiAvailability.getErrorDialog(this, status, 2404).show();
//            }
            return false;
        }
        return true;
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

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void open(Call<ResponseBody> call, final ApiCall type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.noInternet));
        alertDialogBuilder.setPositiveButton(getString(R.string.Cancel), (dialog, arg1) -> dialog.dismiss());

        alertDialogBuilder.setNegativeButton(getString(R.string.Retry), (dialog, which) -> {
            dialog.dismiss();
            getApiCall(call, type);
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void signUp() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appleId", "test");
        hashMap.put("name", "parth godhani");
        Call<ResponseBody> call = apiService.signUp(hashMap);
        getApiCall(call, ApiCall.SIGN_UP_WITH_APPLE);
    }

    public void signUpEmail(String email, String password) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
//        hashMap.put("name", name);
        hashMap.put("password", password);
        Call<ResponseBody> call = apiService.signUpEmail(hashMap);
        getApiCall(call, ApiCall.SIGN_UP_WITH_EMAIL);
    }

    public void forgotPassword(String email) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        Call<ResponseBody> call = apiService.forgotPassword(hashMap);
        getApiCall(call, ApiCall.FORGOT_PASSWORD);
    }

    public void signInEmail(String email, String name) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("password", name);
        Call<ResponseBody> call = apiService.signInEmail(hashMap);
        getApiCall(call, ApiCall.SIGN_IN_WITH_EMAIL);
    }

    public void verifyOTP(String email, String otp) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("otp", otp);
        Call<ResponseBody> call = apiService.verifyOTP(hashMap);
        getApiCall(call, ApiCall.VERIFY_OTP);
    }

    public void signUpGoogle(String id,String name) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("googleId", id);
        hashMap.put("name", name);
        Call<ResponseBody> call = apiService.signUpGoogle(hashMap);
        getApiCall(call, ApiCall.SIGN_UP_WITH_GOOGLE);
    }

    public void signUpFb(String id,String name) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("facebookId", id);
        hashMap.put("name", name);
        Call<ResponseBody> call = apiService.signUpFb(hashMap);
        getApiCall(call, ApiCall.SIGN_UP_WITH_FB);
    }

    public void getProfile() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getProfile(mBearerToken);
        getApiCall(call, ApiCall.GET_PROFILE);
    }

    public void getOtherUserProfile(String userId) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);

        Call<ResponseBody> call = apiService.getOtherUserProfile(mBearerToken, hashMap);
        getApiCall(call, ApiCall.GET_OTHER_USER_PROFILE);
    }

    public void getOtherUserVideos(String userId) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);

        Call<ResponseBody> call = apiService.getOtherUserVideos(mBearerToken, hashMap);
        getApiCall(call, ApiCall.GET_OTHER_USER_VIDEOS);
    }

    public void updateProfile(HashMap<String, String> object) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.updateProfile(mBearerToken, object);
        getApiCall(call, ApiCall.UPDATE_PROFILE);
    }

    public void getStickers() {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.getStickers(mBearerToken);
        getApiCall(call, ApiCall.GET_STICKERS);
    }

    public void getProductSticker(String productThumbnailUrl) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("productThumbnailUrl", productThumbnailUrl);
        Call<ResponseBody> call = apiService.getProductSticker(mBearerToken, hashMap);
        getApiCall(call, ApiCall.GET_PRODUCT_STICKER);
    }

    public void getProductReviews(String asin) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("asin", asin);
        Call<ResponseBody> call = apiService.getProductReviews(mBearerToken, hashMap);
        getApiCall(call, ApiCall.GET_PRODUCT_REVIEWS);
    }

    public void uploadImages(String path) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        File file = new File(path);
        RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagesParts = MultipartBody.Part.createFormData("profile", file.getName(), imgBody);
        Call<okhttp3.ResponseBody> call = apiService.uploadImages(mBearerToken, imagesParts);
        getApiCall(call, ApiCall.UPLOAD_IMAGE);
    }

    public void uploadVideo(Uri videoPath, Uri thumbnailPath, String id, String name, String image, String url) {
        User userData = getMainUser();
        String mBearerToken = "Bearer " + userData.getToken();
        File file = new File(getRealPathFromURI(videoPath));
        RequestBody imgBody = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part imagesParts = MultipartBody.Part.createFormData("video", file.getName(), imgBody);

        File file1 = new File(getRealPathFromURI(thumbnailPath));
        RequestBody imgBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
        MultipartBody.Part imagesParts1 = MultipartBody.Part.createFormData("thumbnail", file1.getName(), imgBody1);

        RequestBody pId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody pName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody pImage = RequestBody.create(MediaType.parse("text/plain"), image);
        RequestBody pUrl = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody newApp = RequestBody.create(MediaType.parse("text/plain"), "true");

        Call<okhttp3.ResponseBody> call = apiService.uploadVideo(mBearerToken, imagesParts, imagesParts1, pId, pName, pImage, pUrl,newApp);
        getApiCall(call, ApiCall.UPLOAD_VIDEO);
    }

    public void subscribeUser(String id) {
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("targetUserId", id);
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.subscribeUser(mBearerToken, hashMap);
        getApiCall(call, ApiCall.SUBSCRIBE_USER);
    }

    public void unSubscribeUser(String id) {
        User userData = getMainUser();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("targetUserId", id);
        String mBearerToken = "Bearer " + userData.getToken();
        Call<ResponseBody> call = apiService.unSubscribeUser(mBearerToken, hashMap);
        getApiCall(call, ApiCall.UNSUBSCRIBE_USER);
    }


    public enum ApiCall {
        SIGN_UP_WITH_EMAIL, VERIFY_OTP, SIGN_IN_WITH_EMAIL, FORGOT_PASSWORD, SIGN_UP_WITH_APPLE, SIGN_UP_WITH_GOOGLE, SIGN_UP_WITH_FB, GET_PROFILE, UPDATE_PROFILE, UPLOAD_IMAGE,
        UPLOAD_VIDEO, GET_USER_VIDEOS, DASHBOARD_GET, GET_STICKERS, SUBSCRIBE_USER, UNSUBSCRIBE_USER,
        GET_PRODUCT_STICKER, GET_PRODUCT_REVIEWS, GET_OTHER_USER_PROFILE, GET_OTHER_USER_VIDEOS
    }


    public void getApiCall(Call<ResponseBody> call, final ApiCall type) {
        if (isNetworkAvailable(this)) {
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

    private void createProgressDialog() {
        this.mProgressDialog = new Dialog(this);
        this.mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.mProgressDialog.setContentView(R.layout.progress_dialog);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
//        this.mProgressDialogWhite.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                return keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
    }

    private void createProgressBar() {
        this.mProgressBarDialog = new Dialog(this);
        this.mProgressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mProgressBarDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.mProgressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.mProgressBarDialog.setContentView(R.layout.progress_bar_dialog);
        this.mProgressBarDialog.setCancelable(false);
        this.mProgressBarDialog.setCanceledOnTouchOutside(false);
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

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    public void showProgressBarDialog() {
        if (mProgressBarDialog != null) {
            if (!mProgressBarDialog.isShowing()) {
                mProgressBarDialog.show();
            }
        }
    }

    public void dismissProgressBarDialog() {
        if (mProgressBarDialog != null) {
            mProgressBarDialog.dismiss();
        }
    }

}

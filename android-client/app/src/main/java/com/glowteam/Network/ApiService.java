package com.glowteam.Network;

import com.glowteam.Models.Search;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login_with_apple")
    Call<ResponseBody> signUp(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("auth/login_with_email")
    Call<ResponseBody> signUpEmail(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("auth/forgot_password")
    Call<ResponseBody> forgotPassword(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("auth/login_with_email_password")
    Call<ResponseBody> signInEmail(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("auth/verify_otp")
    Call<ResponseBody> verifyOTP(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("auth/login_with_google")
    Call<ResponseBody> signUpGoogle(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST("auth/login_with_facebook")
    Call<ResponseBody> signUpFb(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST("profile/get")
    Call<ResponseBody> getProfile(@Header("Authorization") String authHeader);


    @Headers("Content-Type: application/json")
    @POST("profile/update")
    Call<ResponseBody> updateProfile(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("video/view")
    Call<ResponseBody> viewVideo(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("relation/add_to_favorite")
    Call<ResponseBody> subscribeUser(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("product/add_to_myself")
    Call<ResponseBody> addToMyself(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);




    @Headers("Content-Type: application/json")
    @POST("product/add_to_my_product")
    Call<ResponseBody> addToMyProduct(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("product/get_myself_product")
    Call<ResponseBody> getMyProducts(@Header("Authorization") String authHeader);


    @Headers("Content-Type: application/json")
    @POST("relation/remove_from_favorite")
    Call<ResponseBody> unSubscribeUser(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST("video/get_user_video")
    Call<ResponseBody> getUserVideo(@Header("Authorization") String authHeader);

    @Multipart
    @POST("profile/add-profile-picture")
    Call<ResponseBody> uploadImages(@Header("Authorization") String authHeader, @Part MultipartBody.Part images);

    @Multipart
    @POST("video/upload")
    Call<ResponseBody> uploadVideo(@Header("Authorization") String authHeader,
                                   @Part MultipartBody.Part video,
                                   @Part MultipartBody.Part thumbnail,
                                   @Part("asin") RequestBody productId,
                                   @Part("productName") RequestBody productName,
                                   @Part("productThumbnailUrl") RequestBody productThumbnailUrl,
                                   @Part("productUrl") RequestBody productUrl,
                                   @Part("isNew") RequestBody newApp);

    @Headers("Content-Type: application/json")
    @POST("dashboard/get")
    Call<ResponseBody> dashboardGet(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("product/search")
    Call<ResponseBody> productSearch(@Header("Authorization") String authHeader, @Body Search body);

    @Headers({"x-rapidapi-host: amazon-product-reviews-keywords.p.rapidapi.com", "x-rapidapi-key:e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3"})
    @GET("product/search")
    Call<ResponseBody> productSearchAmazon(@Query("country") String country, @Query("keyword") String keyword);

    @Headers({"x-rapidapi-host: amazon-product-reviews-keywords.p.rapidapi.com", "x-rapidapi-key:e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3"})
    @GET("product/details")
    Call<ResponseBody> productDetailAmazon(@Query("country") String country, @Query("asin") String keyword);

    @Headers({"x-rapidapi-host: amazon-price1.p.rapidapi.com", "x-rapidapi-key:e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3"})
    @GET("upcToAsin")
    Call<ResponseBody> barcodeToAsin(@Query("marketplace") String country,@Query("upc") String keyword);


    @Headers({"x-rapidapi-host: product-data1.p.rapidapi.com", "x-rapidapi-key:e2d8634ee1msh7520587499e31d3p1767c1jsnea1a994c8cb3"})
    @GET("lookup")
    Call<ResponseBody> barcodeLookUp(@Query("upc") String keyword);

    @Headers("Content-Type: application/json")
    @POST("product/get_from_url")
    Call<ResponseBody> productSearchUrl(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("comment/get")
    Call<ResponseBody> getComments(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("comment/add")
    Call<ResponseBody> addComments(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("comment/add_replay")
    Call<ResponseBody> addCommentsReply(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("like/add")
    Call<ResponseBody> addLike(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST("like/remove")
    Call<ResponseBody> removeLike(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("stickers/get")
    Call<ResponseBody> getStickers(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("stickers/product_stickers_add")
    Call<ResponseBody> getProductSticker(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST("product/get_reviews")
    Call<ResponseBody> getProductReviews(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("blog/get")
    Call<ResponseBody> getBlogs(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("like/get_liked_video")
    Call<ResponseBody> getLikedVideo(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("product/get_my_product")
    Call<ResponseBody> getMyselfProducts(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("relation/get-favorite-video")
    Call<ResponseBody> getSubscribedVideo(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("relation/get-favorite-users")
    Call<ResponseBody> getSubscribedusers(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("profile/get-target-profile")
    Call<ResponseBody> getOtherUserProfile(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("video/get-target-user-video")
    Call<ResponseBody> getOtherUserVideos(@Header("Authorization") String authHeader, @Body HashMap<String, String> body);


}

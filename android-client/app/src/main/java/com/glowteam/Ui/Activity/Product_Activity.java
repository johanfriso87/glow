package com.glowteam.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glowteam.Adapters.VideoAdapter;
import com.glowteam.CustomViews.CustomTextView;
import com.glowteam.Models.ProductAmazonSearch;
import com.glowteam.Models.ProductSearchUrl;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Product_Activity extends BaseActivity {

    private Object product;
    private ProductAmazonSearch productAmazonSearch;
    private ProductSearchUrl productSearchUrl;
    private ImageView p_image;
    private TextView p_name;
    private TextView p_dis;
    private ImageView back;
    private CustomTextView mBuy, mReview, mShare;
    private RecyclerView mReviewsView;
    private VideoAdapter videoAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout mNoItems;
    private ArrayList<Video> videoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        product = getIntent().getSerializableExtra("product");
        if (product instanceof ProductAmazonSearch) {
            productAmazonSearch = (ProductAmazonSearch) product;
        } else {
            productSearchUrl = (ProductSearchUrl) product;
        }
        initView();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(v -> onBackPressed());

        mBuy.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (productAmazonSearch != null) {
                i.setData(Uri.parse(productAmazonSearch.getUrl()));
            } else {
                i.setData(Uri.parse(productSearchUrl.getUrl()));
            }
            try {
                startActivity(i);
            } catch (Exception e) {
                showToast("No app found to open this url!");
                e.printStackTrace();
            }
        });

        mReview.setOnClickListener(v -> {
            if (productSearchUrl != null) {
                startActivity(new Intent(this, VideoRecordActivity.class)
                        .putExtra("productId", productSearchUrl.getAsin()).
                                putExtra("product_img", productSearchUrl.getImages().get(0)).
                                putExtra("productName", productSearchUrl.getTitle()).
                                putExtra("productUrl", productSearchUrl.getUrl()).
                                putExtra("productThumbnailUrl", productSearchUrl.getImages().get(0)));
            } else {
                startActivity(new Intent(this, VideoRecordActivity.class)
                        .putExtra("productId", productAmazonSearch.getAsin()).
                                putExtra("product_img", productAmazonSearch.getThumbnail()).
                                putExtra("productName", productAmazonSearch.getTitle()).
                                putExtra("productUrl", productAmazonSearch.getUrl()).
                                putExtra("productThumbnailUrl", productAmazonSearch.getThumbnail()));
            }
        });
        mShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setType("video/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            if (productSearchUrl != null) {
                shareIntent.putExtra(Intent.EXTRA_TEXT, productSearchUrl.getUrl() + "\n");
            } else {
                shareIntent.putExtra(Intent.EXTRA_TEXT, productAmazonSearch.getUrl() + "\n");
            }
            startActivity(Intent.createChooser(shareIntent, "Share with..."));
        });
    }

    private void initView() {
        p_image = findViewById(R.id.p_image);
        back = findViewById(R.id.back);
        p_name = findViewById(R.id.p_name);
        p_dis = findViewById(R.id.p_dis);
        mBuy = findViewById(R.id.buy);
        mReview = findViewById(R.id.review);
        mShare = findViewById(R.id.share);
        mNoItems = findViewById(R.id.noItems);
        mReviewsView = findViewById(R.id.reviewsView);
        gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mReviewsView.setLayoutManager(gridLayoutManager);
        videoAdapter = new VideoAdapter(this, videoArrayList);
        mReviewsView.setAdapter(videoAdapter);

        if (productAmazonSearch != null) {
            Glide.with(this).load(productAmazonSearch.getThumbnail()).diskCacheStrategy(DiskCacheStrategy.ALL).into(p_image);
            p_name.setText(productAmazonSearch.getTitle());
            p_dis.setText(productAmazonSearch.getPrice().getCurrent_price() + " " + productAmazonSearch.getPrice().getCurrency());
        } else {
            Glide.with(this).load(productSearchUrl.getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.ALL).into(p_image);
            p_name.setText(productSearchUrl.getTitle());
            p_dis.setVisibility(View.GONE);
//            p_dis.setText(productSearchUrl.getPrice());
        }
        if (productAmazonSearch != null) {
            getProductReviews(productAmazonSearch.getAsin());
        } else {
            getProductReviews(productSearchUrl.getAsin());
        }
    }


    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.GET_PRODUCT_REVIEWS) {
                videoArrayList.clear();
                JSONArray jsonArray = o.getJSONArray("data");
                Gson g = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Video video = g.fromJson(jsonArray.getJSONObject(i).toString(), Video.class);
                    videoArrayList.add(video);
                }
                videoAdapter.notifyDataSetChanged();
                if (videoArrayList.size() == 0) {
                    mNoItems.setVisibility(View.VISIBLE);
                } else {
                    mNoItems.setVisibility(View.GONE);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

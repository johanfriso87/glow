package com.glowteam.Ui.Fragments.ReviewsFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.ProductSearchAdapter;
import com.glowteam.Adapters.ProductSearchAdapter1;
import com.glowteam.Adapters.VideoAdapter;
import com.glowteam.Models.ProductAmazonSearch;
import com.glowteam.Models.ProductSearchUrl;
import com.glowteam.Models.Video;
import com.glowteam.R;
import com.glowteam.Ui.Fragments.BaseFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedProductsFragment extends BaseFragment {
    RecyclerView mAllViews;
    ProductSearchAdapter1 yourReviewAdapter;
    GridLayoutManager linearLayoutManager;
    private ArrayList<ProductSearchUrl> products = new ArrayList<>();
    private LinearLayout no_review;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_review, container, false);
        mAllViews = view.findViewById(R.id.allViews);
        no_review = view.findViewById(R.id.no_review);
        linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        mAllViews.setLayoutManager(linearLayoutManager);

        return view;
    }


    @Override
    public void onResume() {
        getUSersMyselfProduct();
        super.onResume();
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        products.clear();
        if (type == ApiCall.GET_USER_MYSELF_PRODUCTS) {
            try {
                JSONArray jArray = o.getJSONArray("data");

                Gson gson = new Gson();
                for (int i = 1; i < jArray.length(); i++) {
                    if (jArray.get(i) != null) {
                        Log.e("image",jArray.get(i).toString());
                        ProductSearchUrl p = gson.fromJson(jArray.getJSONObject(i).toString(), ProductSearchUrl.class);
//                        p.setAsin(jArray.getJSONObject(i).getString("asin"));
//                        p.setBrand(jArray.getJSONObject(i).getString("brand"));
//                        p.setTitle(jArray.getJSONObject(i).getString("title"));
//                        p.setUrl(jArray.getJSONObject(i).getString("url"));
//                        ArrayList<String> list = new ArrayList<String>();
//                        list.add(jArray.getJSONObject(i).getJSONObject("images").getString("0"));
//                        p.setImages(list);
                        products.add(p);
                    }
                }
                if (products.size() > 0) {
                    no_review.setVisibility(View.GONE);
                    mAllViews.setVisibility(View.VISIBLE);
                    yourReviewAdapter = new ProductSearchAdapter1(getActivity(), products);
                    mAllViews.setAdapter(yourReviewAdapter);

                } else {
                    no_review.setVisibility(View.VISIBLE);
                    mAllViews.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.AllViewsAdapter;
import com.glowteam.Interfaces.Interface;
import com.glowteam.Models.AllViewsItem;
import com.glowteam.Models.Dashboard;
import com.glowteam.Models.FeaturedArticle;
import com.glowteam.Models.FeaturedPlaylistItem;
import com.glowteam.Models.ProductItem;
import com.glowteam.Models.User;
import com.glowteam.Models.Video;
import com.glowteam.Network.ApiService;
import com.glowteam.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeaturedFragment extends BaseFragment {

    RecyclerView mAllViews;
    ArrayList<AllViewsItem> allViewsItemArrayList = new ArrayList<>();
    AllViewsAdapter allViewsAdapter;
    LinearLayoutManager linearLayoutManager;
    private Dashboard dashboard;
    private ApiService apiService;
    public static boolean isVideoAdded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);

        setItems();
        getDashboard();
        mAllViews = view.findViewById(R.id.allViews);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mAllViews.setLayoutManager(linearLayoutManager);
        Interface.SetOnProductAdded(onProductAdded);

        return view;
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        super.OnResponse(o, type);
        if (type == ApiCall.DASHBOARD_GET) {
            try {
                JSONObject object = o.getJSONObject("data");
                dashboard = new Gson().fromJson(object.toString(), Dashboard.class);
                Log.e("uniquevideos",String.valueOf(dashboard.getUniqueVideos().get(0).getId()));
                allViewsItemArrayList.get(1).getVideoList().clear();
                allViewsItemArrayList.get(1).getUniqueVideoList().addAll(dashboard.getUniqueVideos());

                allViewsItemArrayList.get(2).getVideoList().clear();
                allViewsItemArrayList.get(2).getVideoList().addAll(dashboard.getVideos());

                allViewsItemArrayList.get(3).getFeaturedCreatorItems().clear();
                allViewsItemArrayList.get(3).getFeaturedCreatorItems().addAll(dashboard.getUsers());

                allViewsItemArrayList.get(4).getFeaturedArticleArrayList().clear();
                allViewsItemArrayList.get(4).getFeaturedArticleArrayList().addAll(dashboard.getBlogs());

                if (allViewsAdapter == null) {
                    allViewsAdapter = new AllViewsAdapter(getActivity(), allViewsItemArrayList);
                    mAllViews.setAdapter(allViewsAdapter);
                } else {
                    allViewsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    Interface.OnProductAdded onProductAdded = this::getDashboard;

    private void setItems() {
//        String[] strings = new String[]{"Super great spotlight", "Your Subscription", "Videos For you",
//                "Featured Creators", "Featured Playlists", "The Best Heat Styling Tools", "Featured Articles"};


        String[] strings = new String[]{"Super great spotlight", "フォロー中のクリエイター", "タイムライン",
                "注目のクリエイター","article"};

        for (int j = 0; j < strings.length; j++) {
            String s = strings[j];
            AllViewsItem item = new AllViewsItem();
            item.setTitleName(s);
            if (j == 1) {
                ArrayList<ProductItem> productItems = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    ProductItem item1 = new ProductItem();
                    productItems.add(item1);
                }
                item.setProductItemArrayList(productItems);
            }
//            if (j == 2) {
//                ArrayList<Video> videos = new ArrayList<>();
//                for (int i = 0; i < dashboard.getVideos().size(); i++) {
//                    videos.add(dashboard.getVideos().get(i));
//                }
//                item.setVideoList(videos);
//            }
//            if (j == 3) {
//                ArrayList<User> featuredCreatorItems = new ArrayList<>();
//                for (int i = 0; i < dashboard.getUsers().size(); i++) {
//                    featuredCreatorItems.add(dashboard.getUsers().get(i));
//                }
//                item.setFeaturedCreatorItems(featuredCreatorItems);
//            }
//            if (j == 4) {
//                ArrayList<FeaturedPlaylistItem> featuredPlaylistItems = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    FeaturedPlaylistItem item1 = new FeaturedPlaylistItem();
//                    featuredPlaylistItems.add(item1);
//                }
//                item.setFeaturedPlaylistItems(featuredPlaylistItems);
//            }
//            if (j == 5) {
//                ArrayList<ProductItem> productItems = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    ProductItem item1 = new ProductItem();
//                    productItems.add(item1);
//                }
//                item.setProductItemArrayList(productItems);
//            }
//            if (j == 6) {
//                ArrayList<FeaturedArticle> featuredArticles = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    FeaturedArticle item1 = new FeaturedArticle();
//                    featuredArticles.add(item1);
//                }
//                item.setFeaturedArticleArrayList(featuredArticles);
//            }
            allViewsItemArrayList.add(item);
        }
    }
}

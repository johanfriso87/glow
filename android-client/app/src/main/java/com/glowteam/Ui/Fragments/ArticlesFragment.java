package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.FeaturedArticlesAdapter;
import com.glowteam.Models.FeaturedArticle;
import com.glowteam.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArticlesFragment extends BaseFragment {

    RecyclerView mArticlesView;
    FeaturedArticlesAdapter featuredArticlesAdapter;
    ArrayList<FeaturedArticle> featuredArticleArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_articles, container, false);

        mArticlesView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mArticlesView.setLayoutManager(linearLayoutManager);
//        setItems();
        featuredArticlesAdapter = new FeaturedArticlesAdapter(getActivity(), featuredArticleArrayList, false);
        mArticlesView.setAdapter(featuredArticlesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        getBlogs();
        super.onResume();
    }

    @Override
    public void OnResponse(JSONObject o, ApiCall type) {
        try {
            if (type == ApiCall.GET_BLOGS) {
                featuredArticleArrayList.clear();
                JSONArray jsonArray = o.getJSONArray("data");
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    FeaturedArticle featuredArticle = gson.fromJson(jsonArray.getJSONObject(i).toString(), FeaturedArticle.class);
                    featuredArticleArrayList.add(featuredArticle);
                }
                featuredArticlesAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setItems() {
        for (int i = 0; i < 10; i++) {
            FeaturedArticle item1 = new FeaturedArticle();
            featuredArticleArrayList.add(item1);
        }
    }
}

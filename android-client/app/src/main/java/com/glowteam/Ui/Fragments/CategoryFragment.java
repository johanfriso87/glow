package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.AllViewsAdapter;
import com.glowteam.Models.AllViewsItem;
import com.glowteam.Models.FeaturedPlaylistItem;
import com.glowteam.Models.ProductItem;
import com.glowteam.R;

import java.util.ArrayList;

public class CategoryFragment extends BaseFragment {


    RecyclerView mAllViews;
    ArrayList<AllViewsItem> allViewsItemArrayList = new ArrayList<>();
    AllViewsAdapter allViewsAdapter;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mAllViews = view.findViewById(R.id.allViews);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mAllViews.setLayoutManager(linearLayoutManager);
        setItems();
        allViewsAdapter = new AllViewsAdapter(getActivity(), allViewsItemArrayList);
        mAllViews.setAdapter(allViewsAdapter);

        return view;
    }

    private void setItems() {
        String[] strings = new String[]{"Makeup", "", "Skincare",
                "", "Hair Products", "", "Scents", "", "Bath & Body", "", "Tools", "", "Reward Videos"};

        for (int j = 0; j < strings.length; j++) {
            String s = strings[j];
            AllViewsItem item = new AllViewsItem();
            item.setTitleName(s);
            if (j == 0 || j == 2 || j == 4 || j == 6 || j == 8 || j == 10 || j == 12) {
                ArrayList<ProductItem> productItems = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    ProductItem item1 = new ProductItem();
                    productItems.add(item1);
                }
                item.setProductItemArrayList(productItems);
            } else {
                ArrayList<FeaturedPlaylistItem> featuredPlaylistItems = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    FeaturedPlaylistItem item1 = new FeaturedPlaylistItem();
                    featuredPlaylistItems.add(item1);
                }
                item.setFeaturedPlaylistItems(featuredPlaylistItems);
            }

            allViewsItemArrayList.add(item);
        }
    }
}

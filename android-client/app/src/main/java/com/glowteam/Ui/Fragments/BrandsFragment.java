package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.BrandsAdapter;
import com.glowteam.Models.BrandItem;
import com.glowteam.R;

import java.util.ArrayList;

public class BrandsFragment extends BaseFragment {

    RecyclerView mBrandsView;
    BrandsAdapter brandsAdapter;
    ArrayList<BrandItem> brandItemArrayList = new ArrayList<>();
    GridLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_articles, container, false);

        mBrandsView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        mBrandsView.setLayoutManager(linearLayoutManager);
        setItems();
        brandsAdapter = new BrandsAdapter(getActivity(), brandItemArrayList);
        mBrandsView.setAdapter(brandsAdapter);
        return view;
    }

    private void setItems() {
        for (int i = 0; i < 10; i++) {
            BrandItem item1 = new BrandItem();
            brandItemArrayList.add(item1);
        }
    }
}

package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.DropsAdapter;
import com.glowteam.Models.DropsItem;
import com.glowteam.R;

import java.util.ArrayList;

public class DropsFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    DropsAdapter dropsAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DropsItem> dropsItemArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drops, container, false);

        mRecyclerView = view.findViewById(R.id.allViews);
        setItems();
        dropsAdapter = new DropsAdapter(getActivity(), dropsItemArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(dropsAdapter);


        return view;
    }

    private void setItems() {
        for (int i = 0; i < 10; i++) {
            DropsItem item1 = new DropsItem();
            dropsItemArrayList.add(item1);
        }
    }
}

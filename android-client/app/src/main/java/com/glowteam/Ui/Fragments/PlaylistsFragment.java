package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glowteam.Adapters.FeaturedPlaylistsAdapter;
import com.glowteam.Models.FeaturedPlaylistItem;
import com.glowteam.R;

import java.util.ArrayList;

public class PlaylistsFragment extends BaseFragment {

    RecyclerView mPlayListsView;
    FeaturedPlaylistsAdapter featuredPlaylistsAdapter;
    ArrayList<FeaturedPlaylistItem> featuredPlaylistItems = new ArrayList<>();
    GridLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_articles, container, false);

        mPlayListsView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new GridLayoutManager(getActivity(),2, RecyclerView.VERTICAL, false);
        mPlayListsView.setLayoutManager(linearLayoutManager);
        setItems();
        featuredPlaylistsAdapter = new FeaturedPlaylistsAdapter(getActivity(), featuredPlaylistItems);
        mPlayListsView.setAdapter(featuredPlaylistsAdapter);
        return view;
    }

    private void setItems(){
        for (int i = 0; i < 10; i++) {
            FeaturedPlaylistItem item1 = new FeaturedPlaylistItem();
            featuredPlaylistItems.add(item1);
        }
    }
}

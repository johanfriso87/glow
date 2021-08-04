package com.glowteam.Ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.glowteam.Adapters.CategoryAdapter;
import com.glowteam.Models.CategoryItem;
import com.glowteam.R;

import java.util.ArrayList;

public class WatchFragment extends BaseFragment {


//    String[] strings = new String[]{"Featured", "Articles", "Playlist", "Brands", "Category"};
    String[] strings = new String[]{"Featured"};
    ViewPager mViewPager;
    private PagerAdapter pagerAdapter;
    RecyclerView mCategoryView;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryItem> categoryItemArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_watch, container, false);

        mViewPager = view.findViewById(R.id.viewPager);
        mCategoryView = view.findViewById(R.id.categoryView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
//        mCategoryView.setLayoutManager(linearLayoutManager);


        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2, RecyclerView.VERTICAL, false);
        mCategoryView.setLayoutManager(linearLayoutManager);

        addItems();

        categoryAdapter = new CategoryAdapter(getActivity(), categoryItemArrayList);
        mCategoryView.setAdapter(categoryAdapter);
        categoryAdapter.SetOnItemClickListener(onItemClickListener);

        pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (CategoryItem item : categoryItemArrayList) {
                    item.setSelected(false);
                }
                categoryItemArrayList.get(position).setSelected(true);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;


    }

    public void addItems() {
        for (String s : strings) {
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setName(s);
            categoryItemArrayList.add(categoryItem);
        }
        categoryItemArrayList.get(0).setSelected(true);
    }

    CategoryAdapter.OnItemClickListener onItemClickListener = new CategoryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            for (CategoryItem item : categoryItemArrayList) {
                item.setSelected(false);
            }
            categoryItemArrayList.get(position).setSelected(true);
            categoryAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(position);
        }
    };


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new ArticlesFragment();
                case 2:
                    return new PlaylistsFragment();
                case 3:
                    return new BrandsFragment();
                case 4:
                    return new CategoryFragment();
                default:
                    return new FeaturedFragment();
            }
        }

        @Override
        public int getCount() {
            return strings.length;
        }
    }


}

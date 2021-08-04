package com.glowteam.Models;

import java.util.ArrayList;

public class AllViewsItem {

    private String titleName;
    private int viewType = 0;
    private ArrayList<ProductItem> productItemArrayList = new ArrayList<>();
    private ArrayList<User> featuredCreatorItems = new ArrayList<>();
    private ArrayList<FeaturedPlaylistItem> featuredPlaylistItems = new ArrayList<>();
    private ArrayList<FeaturedArticle> featuredArticleArrayList = new ArrayList<>();
    private ArrayList<Video> VideoList = new ArrayList<>();
    private ArrayList<Video> UniqueVideoList = new ArrayList<>();

    public ArrayList<ProductItem> getProductItemArrayList() {
        return productItemArrayList;
    }

    public ArrayList<Video> getVideoList() {
        return VideoList;
    }

    public ArrayList<Video> getUniqueVideoList() {

        return UniqueVideoList;
    }

    public void setVideoList(ArrayList<Video> videoList) {
        VideoList = videoList;
    }

    public void setProductItemArrayList(ArrayList<ProductItem> productItemArrayList) {
        this.productItemArrayList = productItemArrayList;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public ArrayList<User> getFeaturedCreatorItems() {
        return featuredCreatorItems;
    }

    public void setFeaturedCreatorItems(ArrayList<User> featuredCreatorItems) {
        this.featuredCreatorItems = featuredCreatorItems;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public ArrayList<FeaturedPlaylistItem> getFeaturedPlaylistItems() {
        return featuredPlaylistItems;
    }

    public void setFeaturedPlaylistItems(ArrayList<FeaturedPlaylistItem> featuredPlaylistItems) {
        this.featuredPlaylistItems = featuredPlaylistItems;
    }

    public ArrayList<FeaturedArticle> getFeaturedArticleArrayList() {
        return featuredArticleArrayList;
    }

    public void setFeaturedArticleArrayList(ArrayList<FeaturedArticle> featuredArticleArrayList) {
        this.featuredArticleArrayList = featuredArticleArrayList;
    }
}

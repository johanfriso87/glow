package com.glowteam.Models;


import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Dashboard {

    @SerializedName("videos")
    private List<Video> videos;
    @SerializedName("users")
    private List<User> users;

    @SerializedName("uniqueVideos")
    private List<Video> uniqueVideos;

    private List<FeaturedArticle> blogs;

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Video> getUniqueVideos() {
        return uniqueVideos;
    }

    public void setUniqueVideos(List<Video> videos) {
        this.uniqueVideos = videos;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<FeaturedArticle> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<FeaturedArticle> blogs) {
        this.blogs = blogs;
    }
}


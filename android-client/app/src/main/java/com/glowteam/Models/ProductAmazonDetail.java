package com.glowteam.Models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAmazonDetail {

    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("reviews")
    private Reviews reviews;
    @SerializedName("price")
    private Price price;
    @SerializedName("images")
    private List<String> images = null;
    @SerializedName("storeID")
    private String storeID;
    @SerializedName("brand")
    private String brand;
    @SerializedName("soldBy")
    private String soldBy;
    @SerializedName("fulfilledBy")
    private String fulfilledBy;
    @SerializedName("qtyPerOrder")
    private String qtyPerOrder;
    @SerializedName("badges")
    private Badges badges;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public String getFulfilledBy() {
        return fulfilledBy;
    }

    public void setFulfilledBy(String fulfilledBy) {
        this.fulfilledBy = fulfilledBy;
    }

    public String getQtyPerOrder() {
        return qtyPerOrder;
    }

    public void setQtyPerOrder(String qtyPerOrder) {
        this.qtyPerOrder = qtyPerOrder;
    }

    public Badges getBadges() {
        return badges;
    }

    public void setBadges(Badges badges) {
        this.badges = badges;
    }

}

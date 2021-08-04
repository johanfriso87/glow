package com.glowteam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductSearchUrl implements Serializable {

    private String asin;
    private String title;
    private String url;
    private ArrayList<String> images;
    private String brand;



    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    //    @SerializedName("name")
//    private String name;
//    @SerializedName("image")
//    private String image;
//    @SerializedName("shopUrl")
//    private String shopUrl;
//    @SerializedName("brand")
//    private String brand;
//    @SerializedName("_id")
//    private String id;
//    @SerializedName("__v")
//    private Integer v;
//    private String sticker;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getShopUrl() {
//        return shopUrl;
//    }
//
//    public void setShopUrl(String shopUrl) {
//        this.shopUrl = shopUrl;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Integer getV() {
//        return v;
//    }
//
//    public void setV(Integer v) {
//        this.v = v;
//    }
//
//    public String getSticker() {
//        return sticker;
//    }
//
//    public void setSticker(String sticker) {
//        this.sticker = sticker;
//    }
}
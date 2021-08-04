package com.glowteam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductAmazonSearch implements Serializable {

    @SerializedName("asin")
    private String asin;
    @SerializedName("discounted")
    private Boolean discounted;
    @SerializedName("sponsored")
    private Boolean sponsored;
    @SerializedName("amazonChoice")
    private Boolean amazonChoice;
    @SerializedName("bestSeller")
    private Boolean bestSeller;
    @SerializedName("amazonPrime")
    private Boolean amazonPrime;
//    @SerializedName("reviews")
//    private Integer reviews;
    @SerializedName("rating")
    private Double rating;

    @SerializedName("price")
    private Price price;

    @SerializedName("beforeDiscount")
    private String beforeDiscount;
    @SerializedName("title")
    private String title;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("url")
    private String url;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Boolean getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Boolean discounted) {
        this.discounted = discounted;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public Boolean getAmazonChoice() {
        return amazonChoice;
    }

    public void setAmazonChoice(Boolean amazonChoice) {
        this.amazonChoice = amazonChoice;
    }

    public Boolean getBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(Boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public Boolean getAmazonPrime() {
        return amazonPrime;
    }

    public void setAmazonPrime(Boolean amazonPrime) {
        this.amazonPrime = amazonPrime;
    }

//    public Integer getReviews() {
//        return reviews;
//    }
//
//    public void setReviews(Integer reviews) {
//        this.reviews = reviews;
//    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getBeforeDiscount() {
        return beforeDiscount;
    }

    public void setBeforeDiscount(String beforeDiscount) {
        this.beforeDiscount = beforeDiscount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public class Price implements Serializable{

        private boolean discounted;
        private double current_price;
        private String currency;
        private double before_price;
        private double savings_amount;
        private double savings_percent;

        public boolean isDiscounted() {
            return discounted;
        }

        public void setDiscounted(boolean discounted) {
            this.discounted = discounted;
        }

        public double getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(double current_price) {
            this.current_price = current_price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getBefore_price() {
            return before_price;
        }

        public void setBefore_price(double before_price) {
            this.before_price = before_price;
        }

        public double getSavings_amount() {
            return savings_amount;
        }

        public void setSavings_amount(double savings_amount) {
            this.savings_amount = savings_amount;
        }

        public double getSavings_percent() {
            return savings_percent;
        }

        public void setSavings_percent(double savings_percent) {
            this.savings_percent = savings_percent;
        }
    }

}
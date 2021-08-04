package com.glowteam.Models;

public class ReviewItem {
    private String userName;
    private String productName;
    private String userPic;
    private String productPic;
    private String review;
    private String storyPic;
    private String viewCount;


    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStoryPic() {
        return storyPic;
    }

    public void setStoryPic(String storyPic) {
        this.storyPic = storyPic;
    }
}

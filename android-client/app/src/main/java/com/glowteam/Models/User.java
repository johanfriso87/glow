package com.glowteam.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {

    @SerializedName("name")
    private String name;
    @SerializedName("userName")
    private String userName;
    @SerializedName("email")
    private String email;
    @SerializedName("about")
    private String about;
    @SerializedName("website")
    private String website;
    @SerializedName("skinCareInterests")
    private List<String> skinCareInterests = null;
    @SerializedName("hairCareInterests")
    private List<String> hairCareInterests = null;
    @SerializedName("appleId")
    private String appleId;
    @SerializedName("googleId")
    private String googleId;
    @SerializedName("facebookId")
    private String facebookId;
    @SerializedName("password")
    private String password;
    @SerializedName("phone")
    private String phone;
    @SerializedName("usertype")
    private String usertype;
    @SerializedName("isVerified")
    private Boolean isVerified;
    @SerializedName("status")
    private Integer status;
    @SerializedName("_id")
    private String id;
    @SerializedName("__v")
    private Integer v;
    @SerializedName("birthDate")
    private String birthDate;
    @SerializedName("eyeColor")
    private String eyeColor;
    @SerializedName("hairColor")
    private String hairColor;
    @SerializedName("hairTexture")
    private String hairTexture;
    @SerializedName("hairType")
    private String hairType;
    @SerializedName("skinTone")
    private String skinTone;
    @SerializedName("skinType")
    private String skinType;
    @SerializedName("profilePic")
    private String profilePic;
    @SerializedName("token")
    private String token;
    @SerializedName("videos")
    private ArrayList<Video> videos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getSkinCareInterests() {
        return skinCareInterests;
    }

    public void setSkinCareInterests(List<String> skinCareInterests) {
        this.skinCareInterests = skinCareInterests;
    }

    public List<String> getHairCareInterests() {
        return hairCareInterests;
    }

    public void setHairCareInterests(List<String> hairCareInterests) {
        this.hairCareInterests = hairCareInterests;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getHairTexture() {
        return hairTexture;
    }

    public void setHairTexture(String hairTexture) {
        this.hairTexture = hairTexture;
    }

    public String getHairType() {
        return hairType;
    }

    public void setHairType(String hairType) {
        this.hairType = hairType;
    }

    public String getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(String skinTone) {
        this.skinTone = skinTone;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
}
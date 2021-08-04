package com.glowteam.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserId  implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("_id")
    private String id;
    @SerializedName("profilePic")
    private String profilePic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}
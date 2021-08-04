package com.glowteam.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Badges {

    @SerializedName("amazonChoice")
    private Boolean amazonChoice;
    @SerializedName("amazonPrime")
    private Boolean amazonPrime;

    public Boolean getAmazonChoice() {
        return amazonChoice;
    }

    public void setAmazonChoice(Boolean amazonChoice) {
        this.amazonChoice = amazonChoice;
    }

    public Boolean getAmazonPrime() {
        return amazonPrime;
    }

    public void setAmazonPrime(Boolean amazonPrime) {
        this.amazonPrime = amazonPrime;
    }

}

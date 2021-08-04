package com.glowteam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

@SerializedName("current_price")
@Expose
private Double currentPrice;
@SerializedName("discounted")
@Expose
private Boolean discounted;
@SerializedName("before_price")
@Expose
private Double beforePrice;
@SerializedName("savings_amount")
@Expose
private Integer savingsAmount;
@SerializedName("savings_percent")
@Expose
private Integer savingsPercent;

public Double getCurrentPrice() {
return currentPrice;
}

public void setCurrentPrice(Double currentPrice) {
this.currentPrice = currentPrice;
}

public Boolean getDiscounted() {
return discounted;
}

public void setDiscounted(Boolean discounted) {
this.discounted = discounted;
}

public Double getBeforePrice() {
return beforePrice;
}

public void setBeforePrice(Double beforePrice) {
this.beforePrice = beforePrice;
}

public Integer getSavingsAmount() {
return savingsAmount;
}

public void setSavingsAmount(Integer savingsAmount) {
this.savingsAmount = savingsAmount;
}

public Integer getSavingsPercent() {
return savingsPercent;
}

public void setSavingsPercent(Integer savingsPercent) {
this.savingsPercent = savingsPercent;
}

}
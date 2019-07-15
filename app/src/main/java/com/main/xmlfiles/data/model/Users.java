package com.main.xmlfiles.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sellerRatingTotal")
    @Expose
    private Integer sellerRatingTotal;
    @SerializedName("bidderRatingTotal")
    @Expose
    private Integer bidderRatingTotal;
    @SerializedName("sellerVotes")
    @Expose
    private Integer sellerVotes;
    @SerializedName("bidderVotes")
    @Expose
    private Integer bidderVotes;
    @SerializedName("afm")
    @Expose
    private Integer afm;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("address")
    @Expose
    private String address;

    public String getUserId() {
        return user_id;
    }
    public void setUserId(String userId) {
        this.user_id = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public Integer getSellerRatingTotal() {
        return sellerRatingTotal;
    }
    public void setSellerRatingTotal(Integer sellerRatingTotal) {
        this.sellerRatingTotal = sellerRatingTotal;
    }
    public Integer getBidderRatingTotal() {
        return bidderRatingTotal;
    }
    public void setBidderRatingTotal(Integer bidderRatingTotal) {
        this.bidderRatingTotal = bidderRatingTotal;
    }
    public Integer getSellerVotes() {
        return sellerVotes;
    }
    public void setSellerVotes(Integer sellerVotes) {
        this.sellerVotes = sellerVotes;
    }
    public Integer getBidderVotes() {
        return bidderVotes;
    }
    public void setBidderVotes(Integer bidderVotes) {
        this.bidderVotes = bidderVotes;
    }
    public Integer getAfm() {
        return afm;
    }
    public void setAfm(Integer afm) {
        this.afm = afm;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}

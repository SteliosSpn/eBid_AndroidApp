package com.main.xmlfiles.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class Items implements Serializable {
    private Integer item_id;
    private Integer auction_id;
    private String name;
    private String description;
    private String country;
    private Double latitude;
    private Double longitude;
    private List<String>pictures;
    private List<String> tags;
    private Integer rec_score;
    private List<String> pictures_str;

    public List<String> getPictures_str() {
        return pictures_str;
    }

    public void setPictures_str(List<String> pictures_str) {
        this.pictures_str = pictures_str;
    }

    private Bitmap Convertedpictures;

    public Bitmap getConvertedpictures() {
        return Convertedpictures;
    }

    public void setConvertedpictures(Bitmap convertedpictures) {
        Convertedpictures = convertedpictures;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getRec_score() {
        return rec_score;
    }

    public void setRec_score(Integer rec_score) {
        this.rec_score = rec_score;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Integer getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(Integer auction_id) {
        this.auction_id = auction_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

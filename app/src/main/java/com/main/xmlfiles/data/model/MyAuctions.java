package com.main.xmlfiles.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.List;

public class MyAuctions implements Serializable {
    @Expose
    private Integer auction_id;
    @Expose
    private String name;
    @SerializedName("starts")
    @Expose
    private String starts;
    @SerializedName("ends")
    @Expose
    private String ends;
    @Expose
    private String auctioneer;
    @Expose
    private Double start_bid;
    @Expose
    private Double current_bid;
    @Expose
    private String highest_bidder;
    @Expose
    private boolean checked;
    @Expose
    private List<String>tags;
    @Expose
    private List<Items> items;

    public Integer getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(Integer auction_id) {
        this.auction_id = auction_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(String auctioneer) {
        this.auctioneer = auctioneer;
    }

    public Double getStart_bid() {
        return start_bid;
    }

    public void setStart_bid(Double start_bid) {
        this.start_bid = start_bid;
    }

    public Double getCurrent_bid() {
        return current_bid;
    }

    public void setCurrent_bid(Double current_bid) {
        this.current_bid = current_bid;
    }

    public String getHighest_bidder() {
        return highest_bidder;
    }

    public void setHighest_bidder(String highest_bidder) {
        this.highest_bidder = highest_bidder;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}

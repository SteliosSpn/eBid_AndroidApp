package com.main.xmlfiles.data.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Auctions implements Serializable {

    private Integer auction_id;
    private String name;
    private Timestamp starts;
    private Timestamp ends;
    private String auctioneer;
    private Double start_bid;
    private Double current_bid;
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(Integer auction_id) {
        this.auction_id = auction_id;
    }

    public Timestamp getStarts() {
        return starts;
    }

    public void setStarts(Timestamp starts) {
        this.starts = starts;
    }

    public Timestamp getEnds() {
        return ends;
    }

    public void setEnds(Timestamp ends) {
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
}

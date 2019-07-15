package com.main.xmlfiles.data.model;

public class Bid {
    private Integer auction_id;

    private String bidder_id;

    private Double bid;

    public Integer getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(Integer auction_id) {
        this.auction_id = auction_id;
    }

    public String getBidder_id() {
        return bidder_id;
    }

    public void setBidder_id(String bidder_id) {
        this.bidder_id = bidder_id;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }
}

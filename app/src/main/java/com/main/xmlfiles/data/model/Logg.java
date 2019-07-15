package com.main.xmlfiles.data.model;

import java.io.Serializable;

public class Logg implements Serializable {
    private String user_id;
    private Integer auction_id;
    private Integer visits_count;

    public Logg(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(Integer auction_id) {
        this.auction_id = auction_id;
    }

    public Integer getVisits_count() {
        return visits_count;
    }

    public void setVisits_count(Integer visits_count) {
        this.visits_count = visits_count;
    }
}

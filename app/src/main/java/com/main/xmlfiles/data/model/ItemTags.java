package com.main.xmlfiles.data.model;

import java.io.Serializable;

public class ItemTags implements Serializable {
    private Integer item_id;
    private String tag;

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

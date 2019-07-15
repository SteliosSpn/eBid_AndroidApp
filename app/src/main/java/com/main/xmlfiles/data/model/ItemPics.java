package com.main.xmlfiles.data.model;

import java.io.Serializable;

public class ItemPics implements Serializable {
    private Integer item_id;
    private Integer item_id_num;
    private byte[] picture;

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getItem_id_num() {
        return item_id_num;
    }

    public void setItem_id_num(Integer item_id_num) {
        this.item_id_num = item_id_num;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}

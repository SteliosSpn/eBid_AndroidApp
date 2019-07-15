package com.main.xmlfiles;

import android.graphics.Bitmap;

public class ListViewData {
    Bitmap imageUrl;
    String title;
    String price;
    public ListViewData(String title, String price, Bitmap imageUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
    }

    public Bitmap getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Bitmap imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

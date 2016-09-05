package com.flickrtest.data.flickr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlickrResult {

    @SerializedName("photos")
    @Expose
    private FlickrImages flickrImages;
    @SerializedName("stat")
    @Expose
    private String stat;

    public FlickrImages getFlickrImages() {
        return flickrImages;
    }

    public void setFlickrImages(FlickrImages flickrImages) {
        this.flickrImages = flickrImages;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
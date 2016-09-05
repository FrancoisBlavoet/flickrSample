package com.flickrtest.utils;


import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flickrtest.data.flickr.FlickrImage;

public class Bindings {

    @BindingAdapter("resource")
    public static void setSrc(ImageView view, @DrawableRes int res) {
        view.setImageResource(res);
    }


    @BindingAdapter("glide_flickr_photo")
    public static void setImageUrl(ImageView imageView, FlickrImage flickrImage) {
        Glide.with(imageView.getContext()).load(flickrImage).into(imageView);
    }
}

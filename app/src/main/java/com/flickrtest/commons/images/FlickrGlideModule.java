package com.flickrtest.commons.images;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.flickrtest.data.flickr.FlickrImage;

import java.io.InputStream;

/**
 * Module registered in the manifest allowing to configure Glide
 */
public class FlickrGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }


    @Override public void registerComponents(Context context, Glide glide) {
        glide.register(FlickrImage.class, InputStream.class, new FlickrImageLoader.Factory());
    }


}
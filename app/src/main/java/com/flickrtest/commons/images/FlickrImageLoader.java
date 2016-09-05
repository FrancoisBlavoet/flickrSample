package com.flickrtest.commons.images;


import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.flickrtest.data.flickr.FlickrImage;

import java.io.InputStream;

/**
 * Simple Custom ModelLoader for Flickr images
 */
public class FlickrImageLoader extends BaseGlideUrlLoader<FlickrImage> {


    public static class Factory implements ModelLoaderFactory<FlickrImage, InputStream> {

        @Override
        public ModelLoader<FlickrImage, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new FlickrImageLoader(factories.buildModelLoader(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public FlickrImageLoader(ModelLoader<GlideUrl, InputStream> urlLoader) {
        super(urlLoader);
    }

    @Override protected String getUrl(FlickrImage model, int width, int height) {
        return "http://farm" + model.getFarm() + ".static.flickr.com/"
                + model.getServer() + "/" + model.getId() + "_" + model.getSecret() + ".jpg";
    }
}
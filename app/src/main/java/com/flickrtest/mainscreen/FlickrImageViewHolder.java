package com.flickrtest.mainscreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.flickrtest.commons.list.ViewHolder;
import com.flickrtest.data.flickr.FlickrImage;
import com.flickrtest.databinding.ItemFlickrPhotoBinding;

public class FlickrImageViewHolder extends ViewHolder {


    private final ItemFlickrPhotoBinding binding;

    public static FlickrImageViewHolder create(LayoutInflater layoutInflater,
                                               ViewGroup parent) {
        ItemFlickrPhotoBinding binding = ItemFlickrPhotoBinding.inflate(layoutInflater, parent, false);
        return new FlickrImageViewHolder(binding);
    }

    public FlickrImageViewHolder(final ItemFlickrPhotoBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindTo(FlickrImage flickrImage) {
        binding.setFlickrImage(flickrImage);
    }


    @Override public boolean onFailedToRecycleView() {
        binding.image.clearAnimation();
        return true;
    }
}

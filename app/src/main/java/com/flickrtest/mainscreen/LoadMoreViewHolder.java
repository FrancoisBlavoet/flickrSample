package com.flickrtest.mainscreen;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flickrtest.R;
import com.flickrtest.commons.list.DummyViewHolder;


public class LoadMoreViewHolder extends DummyViewHolder {


    public static LoadMoreViewHolder create(LayoutInflater layoutInflater,
                                            ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_load_more,
                                           parent,
                                           false);
        return new LoadMoreViewHolder(view);
    }

    public LoadMoreViewHolder(View itemView) {
        super(itemView);
    }
}

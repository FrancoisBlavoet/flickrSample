package com.flickrtest.mainscreen;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.flickrtest.R;
import com.flickrtest.commons.list.EmptyListCallback;
import com.flickrtest.commons.list.InfiniteAdapterCallback;
import com.flickrtest.commons.list.StatefulAdapter;
import com.flickrtest.commons.list.ViewHolder;
import com.flickrtest.data.flickr.FlickrImage;

import java.util.List;

import static java.util.Collections.emptyList;


public class MainScreenAdapter extends StatefulAdapter {

    private static final String TAG = MainScreenAdapter.class.getSimpleName();
    private static final int LOAD_MORE_THRESHOLD = 5;

    private @NonNull List<FlickrImage> flickrImages = emptyList();
    private @NonNull EmptyListCallback emptyListCallback;
    private @NonNull InfiniteAdapterCallback infiniteAdapterCallback;

    private boolean isLoadingMoreItems = false;
    private int max = -1;

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public MainScreenAdapter(@NonNull EmptyListCallback emptyListCallback,
                             @NonNull InfiniteAdapterCallback infiniteAdapterCallback) {
        super(ContentState.EMPTY);
        this.emptyListCallback = emptyListCallback;
        this.infiniteAdapterCallback = infiniteAdapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, @IdRes int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case R.id.view_type_standard:
                return FlickrImageViewHolder.create(layoutInflater, parent);

            case R.id.view_type_loading:
                return buildLoadingViewHolder(layoutInflater, parent);

            case R.id.view_type_error:
                return buildErrorViewHolder(layoutInflater, parent, emptyListCallback);

            case R.id.view_type_empty:
                return buildEmptyViewHolder(layoutInflater, parent);

            case R.id.view_type_loading_more:
                return LoadMoreViewHolder.create(layoutInflater, parent);
            default:
                throw new IllegalStateException("the adapter is in an invalid state");
        }
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        switch (holder.getItemViewType()) {
            case R.id.view_type_standard:
                FlickrImageViewHolder viewHolder = (FlickrImageViewHolder) holder;
                viewHolder.bindTo(flickrImages.get(position));

                if (position > flickrImages.size() - LOAD_MORE_THRESHOLD
                        && canLoadMore()
                        && !isLoadingMoreItems) {
                    setIsLoadingMoreItems(true);
                    infiniteAdapterCallback.onNeedMoreItems();
                }

                break;


            case R.id.view_type_loading:
            case R.id.view_type_error: //todo
            case R.id.view_type_empty:
            case R.id.view_type_loading_more:
                // nothing to do for these
                break;

            default:
                throw new IllegalStateException("the adapter is in an invalid state");
        }

    }

    @Override public int getContentItemsCount() {
        return flickrImages.size() + (isLoadingMoreItems ? 1 : 0);
    }


    @Override public int getContentItemViewType(int position) {
        if (position >= flickrImages.size()) return R.id.view_type_loading_more;
        return super.getContentItemViewType(position);
    }

    public void setFlickrImages(List<FlickrImage> flickrImages) {
        this.flickrImages = flickrImages;
        if (flickrImages.size() == 0) setCurrentState(ContentState.EMPTY);
        else setCurrentState(ContentState.CONTENT, true);
    }

    public void setMax(int max) {
        this.max = max;
    }

    @UiThread
    private void setIsLoadingMoreItems(boolean isLoadingMoreItems) {
        if (isLoadingMoreItems == this.isLoadingMoreItems) return;
        this.isLoadingMoreItems = isLoadingMoreItems;
        if (getCurrentState() != ContentState.CONTENT) return;

        if (isLoadingMoreItems) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(getItemCount() - 1);
                }
            });
        } else {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(getItemCount());
                }
            });
        }

    }

    public void notifyInsertion(final int numberOfItemsAdded) {
        this.isLoadingMoreItems = false;
        mainThreadHandler.post(new Runnable() {
            @Override public void run() {
                int oldListEnd = flickrImages.size() - numberOfItemsAdded;
                notifyItemRemoved(oldListEnd);
                notifyItemRangeInserted(oldListEnd,
                                        numberOfItemsAdded);
            }
        });

    }

    private boolean canLoadMore() {
        return max > flickrImages.size();
    }

}

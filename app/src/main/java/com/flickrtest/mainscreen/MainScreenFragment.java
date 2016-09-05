package com.flickrtest.mainscreen;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flickrtest.R;
import com.flickrtest.application.LostApplication;
import com.flickrtest.commons.list.SpacesItemDecoration;
import com.flickrtest.commons.list.StatefulAdapter;
import com.flickrtest.data.flickr.FlickrImage;
import com.flickrtest.databinding.MainScreenFragmentBinding;

import java.util.List;

import javax.inject.Inject;

import static com.flickrtest.mainscreen.FlickrSearchRecentSuggestionsProvider.AUTHORITY;
import static com.google.common.base.Preconditions.checkNotNull;


public class MainScreenFragment extends Fragment implements MainScreenContract.View {

    private static final String TAG = MainScreenFragment.class.getSimpleName();

    @Inject MainScreenContract.Presenter presenter;
    private MainScreenAdapter adapter;
    private SearchRecentSuggestions suggestions;


    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }


    public MainScreenFragment() { }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FragmentActivity activity = getActivity();

        // Create the presenter
        DaggerMainScreenComponent.builder()
                .mainScreenPresenterModule(new MainScreenPresenterModule(this, savedInstanceState))
                .applicationComponent(((LostApplication) activity.getApplication()).getAppComponent())
                .build()
                .inject(this);

        suggestions = new SearchRecentSuggestions(this.getContext(),
                                                  AUTHORITY,
                                                  FlickrSearchRecentSuggestionsProvider.MODE);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final MainScreenFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_screen_fragment, container, false);
        binding.setPresenter(presenter);


        RecyclerView recyclerView = binding.recyclerView;
        Resources resources = getResources();
        final int span = resources.getInteger(R.integer.span);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), span);
        recyclerView.setLayoutManager(layoutManager);
        final int space = resources.getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(space, span));

        adapter = new MainScreenAdapter(presenter, presenter);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                switch (adapter.getCurrentState()) {
                    case LOADING:
                    case ERROR:
                    case EMPTY:
                        return span;

                    case CONTENT:
                        int itemViewType = adapter.getItemViewType(position);
                        switch (itemViewType) {
                            case R.id.view_type_standard:
                                return 1;
                            case R.id.view_type_loading_more:
                                return span;
                        }

                    default:
                        throw new RuntimeException("invalid state");
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        configureSearchView(binding);

        return binding.getRoot();
    }

    private void configureSearchView(final MainScreenFragmentBinding binding) {
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());

        binding.searchView.setSearchableInfo(searchableInfo);

        binding.searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override public boolean onSuggestionClick(int position) {
                // we don't want to have to couple with the intent sent to the activity,
                // so we retrieve the text ourselves :
                CursorAdapter suggestionsAdapter = binding.searchView.getSuggestionsAdapter();
                Cursor cursor = suggestionsAdapter.getCursor();
                int columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
                String val = cursor.getString(columnIndex);
                binding.searchView.setQuery(val, true);
                return true;
            }
        });
    }


    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.start();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        presenter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override public void setPresenter(@NonNull MainScreenContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }


    @Override public void setPhotos(@NonNull List<FlickrImage> flickrImages) {
        Log.d(TAG, "setFlickrImages() called with: " + flickrImages.size() + " photos");
        adapter.setFlickrImages(flickrImages);
    }

    @Override public void setState(StatefulAdapter.ContentState state) {
        adapter.setCurrentState(state);
    }


    @Override public void setMax(int max) {
        adapter.setMax(max);
    }

    @Override public void notifyInsertion(int size) {
        adapter.notifyInsertion(size);
    }

    @Override public void saveRecentQuery(String query) {
        suggestions.saveRecentQuery(query, null);
    }


}
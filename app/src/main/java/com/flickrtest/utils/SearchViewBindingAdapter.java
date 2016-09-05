package com.flickrtest.utils;


import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v7.widget.SearchView;

@BindingMethods({
        @BindingMethod(type = SearchView.class, attribute = "onQueryTextFocusChange", method = "setOnQueryTextFocusChangeListener"),
        @BindingMethod(type = SearchView.class, attribute = "onSearchClick", method = "setOnSearchClickListener"),
        @BindingMethod(type = SearchView.class, attribute = "onClose", method = "setOnCloseListener"),
})
public class SearchViewBindingAdapter {

    @BindingAdapter("onQueryTextChange")
    public static void setonQueryTextChangeListener(SearchView view, OnQueryTextChange listener) {
        setListener(view, null, listener);
    }

    @BindingAdapter("onQueryTextSubmit")
    public static void setOnQueryTextSubmitListener(SearchView view, OnQueryTextSubmit listener) {
        setListener(view, listener, null);
    }

    @BindingAdapter({"onQueryTextSubmit", "onQueryTextChange"})
    public static void setListener(SearchView view, final OnQueryTextSubmit submit,
                                   final OnQueryTextChange change) {
        if (submit == null && change == null) {
            view.setOnQueryTextListener(null);
        } else {
            view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (submit != null) {
                        return submit.onQueryTextSubmit(query);
                    } else {
                        return false;
                    }
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (change != null) {
                        return change.onQueryTextChange(newText);
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    @BindingAdapter("onSuggestionClick")
    public static void setListener(SearchView view, OnSuggestionClick listener) {
        setListener(view, null, listener);
    }

    @BindingAdapter("onSuggestionSelect")
    public static void setListener(SearchView view, OnSuggestionSelect listener) {
        setListener(view, listener, null);
    }

    @BindingAdapter({"onSuggestionSelect", "onSuggestionClick"})
    public static void setListener(SearchView view, final OnSuggestionSelect submit,
                                   final OnSuggestionClick change) {
        if (submit == null && change == null) {
            view.setOnSuggestionListener(null);
        } else {
            view.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    if (submit != null) {
                        return submit.onSuggestionSelect(position);
                    } else {
                        return false;
                    }
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    if (change != null) {
                        return change.onSuggestionClick(position);
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    public interface OnQueryTextSubmit {
        boolean onQueryTextSubmit(String query);
    }

    public interface OnQueryTextChange {
        boolean onQueryTextChange(String newText);
    }

    public interface OnSuggestionSelect {
        boolean onSuggestionSelect(int position);
    }

    public interface OnSuggestionClick {
        boolean onSuggestionClick(int position);
    }
}
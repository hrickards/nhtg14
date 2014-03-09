package com.harryrickards.nhtg14;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchFragment  extends Fragment {
    private Button searchButton;
    private OnSearchListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnSearchListener) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onManualSearch();
            }
        });
    }

    // Update button text to "search again"
    public void updateButtonText() {
        searchButton.setText(getString(R.string.search_again));
    }

    public void onSearchStarted() {
        searchButton.setEnabled(false);
    }

    public void onSearchStopped() {
        searchButton.setEnabled(true);
    }

    public interface OnSearchListener {
        public void onManualSearch();
    }
}

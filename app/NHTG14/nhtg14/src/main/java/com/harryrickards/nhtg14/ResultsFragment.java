package com.harryrickards.nhtg14;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ResultsFragment extends Fragment {
    // Data views
    TextView establishmentName;
    TextView establishmentRating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Find views
        establishmentName = (TextView) view.findViewById(R.id.establishmentName);
        establishmentRating = (TextView) view.findViewById(R.id.establishmentRating);
    }

    public void showEstablishment(Establishment establishment) {
        establishmentName.setText(establishment.establishmentName);
        establishmentRating.setText(establishment.getRatingString());
    }
}

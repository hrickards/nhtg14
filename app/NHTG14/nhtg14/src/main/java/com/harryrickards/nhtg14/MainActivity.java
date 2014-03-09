package com.harryrickards.nhtg14;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity
        implements MotionDetector.MotionDetectorInterface,
        Establishment.EstablishmentInterface,
        SearchFragment.OnSearchListener {

    // Fragments
    private SearchFragment searchFragment;
    private ResultsFragment resultsFragment;

    // Detectors
    private MotionDetector motionDetector;
    private LocationDetector locationDetector;

    // For getting establishment details
    private Establishment establishment;
    private boolean gettingEstablishmentDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get fragments
        searchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.searchFragment);
        resultsFragment = (ResultsFragment) getFragmentManager().findFragmentById(R.id.resultsFragment);

        // Hide resultsFragment as we have no results yet
        getFragmentManager().beginTransaction().hide(resultsFragment).commit();

        // Set up detectors
        motionDetector = new MotionDetector(this);
        locationDetector = new LocationDetector(this);
    }

    // Search initialisation
    public void onUserStopped() { onEstablishmentSearchRequested(); }
    public void onManualSearch() { onEstablishmentSearchRequested(); }


    // Begin a search
    public void onEstablishmentSearchRequested() {
        // Get most recent location
        Location location = locationDetector.getLocation();

        if (!gettingEstablishmentDetails && location != null) {
            getEstablishmentDetails(location);
        }
    }

    protected void getEstablishmentDetails(Location location) {
        gettingEstablishmentDetails = true;
        if (searchFragment != null) { searchFragment.onSearchStarted(); }

        // Get establishment details
        establishment = new Establishment(location, this);
    }

    public void onEstablishmentDetailsFound() {
        gettingEstablishmentDetails = false;
        if (searchFragment != null) { searchFragment.onSearchStopped(); }

        // Make sure resultsFragment is shown
        getFragmentManager().beginTransaction().show(resultsFragment).commit();
        // Change search button to "search again"
        searchFragment.updateButtonText();

        // Show results to user
        if (resultsFragment != null) { resultsFragment.showEstablishment(establishment); }

        // Vibrate based on rating
        VibratorWrapper.vibrateEstablishment(this, establishment);
    }

    // Default menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

package com.harryrickards.nhtg14;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

        // Show results to user
        if (resultsFragment != null) { resultsFragment.showEstablishment(establishment); }
    }

    // Default menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}

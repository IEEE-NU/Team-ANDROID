package com.ieee_ae.adam.wildhunt;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Button startButton;
    Button pauseButton;
    Chronometer timer;
    boolean first_time;
    boolean running;
    long elapsedMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);

        timer = (Chronometer) findViewById(R.id.timer);
        running = false;
        first_time = true;

        startButton = (Button) findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (first_time) {
                    elapsedMillis = SystemClock.elapsedRealtime();
                    first_time = false;
                } else {
                    elapsedMillis = SystemClock.elapsedRealtime() - elapsedMillis;
                }
                if (!running) {
                    timer.setBase(elapsedMillis);
                    timer.start();
                    running = true;
                }
            }
        });
        pauseButton = (Button) findViewById(R.id.btnPause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timer.stop();
                running = false;
                elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private void setUpMap() {
        LatLng NU = new LatLng(42.056457, -87.675436);
        LatLng rogersHouse = new LatLng(42.051600, -87.679243);
        LatLng lakeFill = new LatLng(42.055352, -87.670804);
        LatLng jacobsCenter = new LatLng(42.053954, -87.676683);
        LatLng blockMuseum = new LatLng(42.052397, -87.672767);
        LatLng henryCrown = new LatLng(42.059443, -87.672767);
        LatLng seabury = new LatLng(42.056954, -87.678201);
        LatLng tech = new LatLng(42.057856, -87.676173);
        LatLng dearborn = new LatLng(42.056697, -87.675057);
        LatLng norris = new LatLng(42.053323, -87.672890);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NU, 15));
        int rand = (int) Math.floor(Math.random() * 11) + 1;
        switch (rand) {
            case 1:
                mMap.addMarker(new MarkerOptions().position(NU).title("Northwestern University"));
                break;
            case 2:
                mMap.addMarker(new MarkerOptions().position(rogersHouse).title("Rogers House").snippet("Chris Chen lives here!"));
                break;
            case 3:
                mMap.addMarker(new MarkerOptions().position(lakeFill).title("Lake Fill"));
                break;
            case 4:
                mMap.addMarker(new MarkerOptions().position(jacobsCenter).title("Donald P. Jacobs Center"));
                break;
            case 5:
                mMap.addMarker(new MarkerOptions().position(blockMuseum).title("Block Museum"));
                break;
            case 6:
                mMap.addMarker(new MarkerOptions().position(henryCrown).title("Henry Crown Sports Pavilion"));
                break;
            case 7:
                mMap.addMarker(new MarkerOptions().position(seabury).title("Seabury Hall"));
                break;
            case 8:
                mMap.addMarker(new MarkerOptions().position(tech).title("Technological Institute"));
                break;
            case 9:
                mMap.addMarker(new MarkerOptions().position(dearborn).title("Dearborn Observatory"));
                break;
            case 10:
                mMap.addMarker(new MarkerOptions().position(norris).title("Norris University Center"));
                break;
        }
    }
}

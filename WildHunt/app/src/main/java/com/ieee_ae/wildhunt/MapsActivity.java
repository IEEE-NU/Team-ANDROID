package com.ieee_ae.wildhunt;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.atomic.AtomicReference;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;

public class MapsActivity extends FragmentActivity implements MeteorCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Meteor mMeteor; // Server connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mMeteor = new Meteor("ws://wildhunt.meteor.com/websocket");
        mMeteor.setCallback(this);
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NU, 13));
        mMap.addMarker(new MarkerOptions().position(NU).title("Northwestern University"));
        mMap.addMarker(new MarkerOptions().position(rogersHouse).title("Rogers House")
                .snippet("Chris Chen lives here!"));
    }


    /*

        METEOR SERVER API BELOW

        Usage:

            getUserID -- returns a unique server ID. Required to create or join sessions.
            createSession -- given user ID, creates a session on the server and returns a key to
                join the session for others. The server currently only supports one session.
            joinSession -- attempts to joins a room with a key and user ID. Returns 1 on success.
            leaveSession -- attempts to leave a room with a key and user ID. Returns 1 on success.
                Destroys server session if empty.

    */
    private String getUserID() {
        return (String) synchronousWrapper("getUserID", null);
    }

    private String createSession(String uid) {
        Object[] params = new Object[1];
        params[0] = uid;
        return (String) synchronousWrapper("createSession", params);
    }

    private int joinSession(String key, String uid) {
        Object[] params = new Object[2];
        params[0] = key;
        params[1] = uid;
        return (int) synchronousWrapper("joinSession", params);
    }

    private int leaveSession(String key, String uid) {
        Object[] params = new Object[2];
        params[0] = key;
        params[1] = uid;
        return (int) synchronousWrapper("leaveSession", params);
    }

    private Object synchronousWrapper(String method, Object[] params) {
        final AtomicReference<Object> notifier = null;
        ResultListener callback = new ResultListener() {
            @Override
            public void onSuccess(String result) {
                synchronized (notifier) {
                    notifier.set(result);
                    notifier.notify();
                }
            }

            @Override
            public void onError(String s, String s2, String s3) {
                // handle
            }
        };
        mMeteor.call(method, params, callback) ;
        synchronized (notifier) {
            while (notifier.get() == null)
                try {
                    notifier.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return notifier.get();
    }

    // METEOR SERVER STUBS BELOW
    // YOU CAN IGNORE ALL OF THIS.
    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect(int i, String s) {

    }

    @Override
    public void onDataAdded(String s, String s2, String s3) {

    }

    @Override
    public void onDataChanged(String s, String s2, String s3, String s4) {

    }

    @Override
    public void onDataRemoved(String s, String s2) {

    }

    @Override
    public void onException(Exception e) {

    }
}

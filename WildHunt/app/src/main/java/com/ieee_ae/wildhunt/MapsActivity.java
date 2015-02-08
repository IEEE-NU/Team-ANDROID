package com.ieee_ae.wildhunt;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

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

    // Stopwatch Stuff
    private TextView textTimer;
    private Button startButton;
    private Button pauseButton;
    private long startTime = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        // Stopwatch stuff
        textTimer = (TextView) findViewById(R.id.stopwatch);

        startButton = (Button) findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                myHandler.postDelayed(updateTimerMethod, 0);

            }
        });
        pauseButton = (Button) findViewById(R.id.btnPause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeSwap += timeInMillies;
                myHandler.removeCallbacks(updateTimerMethod);

            }
        });

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

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            textTimer.setText("" + minutes + ":"
            + String.format("%02d", seconds) + ":"
            + String.format("%03d", milliseconds));
            myHandler.postDelayed(this, 0);
        }

    };


    /*

        METEOR SERVER API BELOW

        Usage:

            getUserID -- returns a unique server ID. Required to create or join sessions.
            createSession -- given user ID, creates a session on the server and returns a key to
                join the session for others.
            joinSession -- attempts to joins a room with a key and user ID. Returns 1 on success.
            leaveSession -- attempts to leave a room with a key and user ID. Returns 1 on success.

        For now, if you don't feel like dealing with creating / joining sessions, there is one session
        always active on the server with the key "balloon". (e.g. do joinSession("balloon", uid))

            getSessionMembers -- returns a list of all user IDs in a session given the key.
            addSessionData -- stores data in session (determined via key) that is retrievable through a
                second dataKey.
            getSessionData -- retrieves previously stored data. If that's not found, returns 1.

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

    private String[] getSessionMembers(String key) {
        Object[] params = new Object[1];
        params[0] = key;
        return (String[]) synchronousWrapper("getSessionMembers", params);
    }

    private int addSessionData(String key, String dataKey, Object data) {
        Object[] params = new Object[3];
        params[0] = key;
        params[1] = dataKey;
        params[2] = data;
        return (int) synchronousWrapper("addSessionData", params);
    }

    private Object getSessionData(String key, String dataKey) {
        Object[] params = new Object[2];
        params[0] = key;
        params[1] = dataKey;
        return synchronousWrapper("getSessionData", params);
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

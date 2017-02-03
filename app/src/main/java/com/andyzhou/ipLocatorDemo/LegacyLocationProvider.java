package com.andyzhou.ipLocatorDemo;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;

public class LegacyLocationProvider {

    private LocationManager locationManager;

    private LocationListener listener;


    private static LegacyLocationProvider instance;

    public static LegacyLocationProvider instanceOf(Context context) {
        if (instance == null) {
            instance = new LegacyLocationProvider(context);
        }
        return instance;
    }

    public LegacyLocationProvider(Context context) {

        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startGetLocation(LocationListener listener) throws SecurityException {
        String bestProvider = locationManager.getBestProvider(new Criteria(), true);
        this.listener = listener;
        locationManager.requestLocationUpdates(bestProvider, 0, 0, listener);
    }

    public void stopGetLocation() throws SecurityException {
        locationManager.removeUpdates(listener);
    }
}

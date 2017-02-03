package com.andyzhou.ipLocatorDemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

import iplocator.IPLocationListener;
import iplocator.IPLocationProvider;

public class MainActivity extends AppCompatActivity {
    private static final int ACCESS_LOCATION_CODE = 506;
    TextView tv_location;
    LegacyLocationProvider gpsProvider;
    IPLocationProvider ipProvider;
    Location gpsLocation;
    Address ipLocation;
    GpsListener mGPSListener = new GpsListener();
    IPLocationListener mIPListener = new myIPListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_location.setMovementMethod(new ScrollingMovementMethod());
        gpsProvider = LegacyLocationProvider.instanceOf(this);
        ipProvider = new IPLocationProvider(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gps_location:
                loadGPSLocation();
                return true;
            case R.id.ip_location:
                IPLocationProvider provider = new IPLocationProvider(this);
                provider.getLocationByIp(mIPListener);
                return true;
            case R.id.clear_screen:
                tv_location.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadGPSLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_CODE);
        } else {
            gpsProvider.startGetLocation(mGPSListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gpsProvider.startGetLocation(mGPSListener);
        }
    }

    private Location addressToLocation(Address address) {
        Location newLocation = new Location("");
        newLocation.setLongitude(address.getLongitude());
        newLocation.setLatitude(address.getLatitude());
        return newLocation;
    }

    private class GpsListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            gpsProvider.stopGetLocation();
            String notice = String.format(Locale.US, "GPS: %.4f, %.4f\n", location.getLatitude(), location.getLongitude());
            String textContent = tv_location.getText().toString();
            textContent += notice;
            tv_location.setText(textContent);
            gpsLocation = location;
            if (ipLocation != null) {
                float distance = gpsLocation.distanceTo(addressToLocation(ipLocation));
                notice = String.format(Locale.US, "%.0fm to IP location\n", distance);
                textContent = tv_location.getText().toString();
                textContent += notice;
                tv_location.setText(textContent);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    private class myIPListener implements IPLocationListener {
        @Override
        public void onIPLocation(Address address) {
            String notice = String.format(Locale.US, "IP: %.4f, %.4f, %s, %s\n",
                    address.getLatitude(),
                    address.getLongitude(),
                    address.getLocality(),
                    address.getAdminArea());
            String textContent = tv_location.getText().toString();
            textContent += notice;
            tv_location.setText(textContent);
            ipLocation = address;
            if (gpsLocation != null) {
                float distance = gpsLocation.distanceTo(addressToLocation(ipLocation));
                notice = String.format(Locale.US, "%.0fm to GPS location\n", distance);
                textContent = tv_location.getText().toString();
                textContent += notice;
                tv_location.setText(textContent);
            }
        }
    }
}

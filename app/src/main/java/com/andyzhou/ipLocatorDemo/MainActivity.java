package com.andyzhou.ipLocatorDemo;

import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

import iplocator.IPLocationListener;
import iplocator.IPLocationProvider;

public class MainActivity extends AppCompatActivity {

    TextView tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_location = (TextView) findViewById(R.id.tv_location);
        final IPLocationProvider provider = new IPLocationProvider(this);
        final IPLocationListener listener = new IPLocationListener() {
            @Override
            public void onIPLocation(Address address) {
                String notice = String.format(Locale.US,
                        "%.4f,%.4f,%s,%s",
                        address.getLatitude(),
                        address.getLongitude(),
                        address.getLocality(),
                        address.getAdminArea());
                tv_location.setText(notice);
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider.getLocationByIp(listener);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

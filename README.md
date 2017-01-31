# IPLocator
This library is used for get user's location by IpInfo.io's IP Geolocation API, this is especially usefule to provide an alternative way to get user's location when user denied to grant location permission to the app, or avoid to request location permission when only city level location informaiton required.

## Usage:
This library provides IPLocationProvider and IPLocationListener for asynchronous call and get android.location.Address for its result:
```java
IPLocationProvider provider = new IPLocationProvider(this);
IPLocationListener listener = new IPLocationListener() {
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
provider.getLocationByIp(listener);
```
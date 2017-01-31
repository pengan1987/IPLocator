package iplocator;

import android.content.Context;
import android.location.Address;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

//Pengan(andy) zhou 2017-01-30
public class IPLocationProvider {
    private final static String url = "http://ipinfo.io/json";
    private RequestQueue queue;

    public IPLocationProvider(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void getLocationByIp(final IPLocationListener listener) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String city = "";
                        String region = "";
                        Locale country = Locale.getDefault();
                        String locString = "";
                        String postal = "";
                        Double lat = null;
                        Double lon = null;
                        try {
                            city = response.getString("city");
                            region = response.getString("region");
                            String countryCode = response.getString("country");
                            country = new Locale("", countryCode);
                            locString = response.getString("loc");
                            postal = response.getString("postal");
                            String[] locArray = locString.split(",");
                            lat = Double.parseDouble(locArray[0]);
                            lon = Double.parseDouble(locArray[1]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Address address = new Address(Locale.US);
                        if (lat != null)
                            address.setLatitude(lat);
                        if (lon != null)
                            address.setLongitude(lon);
                        address.setPostalCode(postal);
                        address.setAdminArea(region);
                        address.setCountryCode(country.getCountry());
                        address.setCountryName(country.getDisplayCountry());
                        address.setLocality(city);
                        if (listener != null)
                            listener.onIPLocation(address);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null)
                            listener.onIPLocation(new Address(Locale.US));
                    }
                });
        queue.add(jsObjRequest);
    }
}

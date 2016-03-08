package gusrsilva.represent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class ChooseLocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , ActivityCompat.OnRequestPermissionsResultCallback{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private GoogleApiClient mApiClient;
    private List<Node> nodes = new ArrayList<>();
    private String zip = "00000";
    private String TAG = "Represent!";
    private static int PERMISSION_ACCESS_COURSE_LOCATION = 0, PERMISSION_INTERNET = 1;
    private String REVERSE_GEO_REQUEST = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY";
    private final String COUNTY = "administrative_area_level_2", STATE = "administrative_area_level_1", CITY = "locality", ZIP = "postal_code";
    public static Place currentPlace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        // Set up the login form.

        final EditText zipCode = (EditText)findViewById(R.id.zipCode);
        zipCode.clearFocus();
        Button bUseCurrentLocation = (Button) findViewById(R.id.currentLocationButton);
        Button bContinue = (Button) findViewById(R.id.continueButton);
        bContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                zip = zipCode.getText().toString();
                String url = buildUrlFromZip(zip);
                createLocationFromUrl(url);
                continueToMainActivity(zip);
            }
        });

        bUseCurrentLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dummy zip until I see how the API works
                //zip = "94704";
                Location mLastLocation = null;
                if ( ContextCompat.checkSelfPermission( getApplicationContext()
                        , android.Manifest.permission.ACCESS_COARSE_LOCATION )
                        != PackageManager.PERMISSION_GRANTED ) {
                    requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_ACCESS_COURSE_LOCATION);
                }
                else if (mApiClient.isConnected())
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mApiClient);
                if(mLastLocation != null)
                {

                    String url = buildUrlFromLatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    createLocationFromUrl(url);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Wearable.NodeApi.getConnectedNodes(mApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        //Log.d("T", "found nodes");
                        //when we find a connected node, we populate the list declared above
                        //finally, we can send a message
                        sendMessage("/zip_code", zip);
                        Log.d(TAG, "Sent zipCode from ChooseLocationActivty. Zip= " + zip);
                    }
                });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "ChooseLocationActivity: Connection Failed! Result: " + connectionResult.toString());

    }

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mApiClient, node.getId(), path, text.getBytes());
        }
    }

    private void continueToMainActivity(String zip)
    {
        mApiClient.disconnect();mApiClient.connect();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.ZIP_CODE, zip);
        startActivity(intent);
    }

    private void requestPermission(String permission, int requestCode)
    {
        ActivityCompat.requestPermissions(this, new String[] {permission}, requestCode);
    }

    private String getZipFromLatLng(double lat, double lng)
    {
        List<Address> lst = new ArrayList<>();
        android.location.Geocoder geo = new Geocoder(getApplicationContext());
        try {
            lst = geo.getFromLocation(lat, lng, 1);
            Address addr = lst.get(0);
            String city = addr.getLocality();
            String state = addr.getAdminArea();
            Log.d(TAG, "Location: " + city+ ", " + state);
            Log.d(TAG, "County: " + addr.getSubAdminArea());
            if(city != null && state != null)
                MainActivity.location = city + ", " + state;
            return addr.getPostalCode();
        }
        catch (IOException e) {
            Log.d(TAG, "Error getLocation(): " + e.toString());
            return null;
        }
    }

    private String buildUrlFromLatLng(double lat, double lng)
    {

        //"https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY";
        String key = getResources().getString(R.string.KEY_GOOGLE_GEO);
        return
                String.format(Locale.ENGLISH,"https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s"
                        , lat
                        , lng
                        ,key);
    }

    private String buildUrlFromZip(String zip)
    {

        //"https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY";
        String key = getResources().getString(R.string.KEY_GOOGLE_GEO);
        return
                String.format(Locale.ENGLISH,"https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s"
                        , zip
                        ,key);
    }

    private void createLocationFromUrl(String url)
    {
        Log.d(TAG, "Calling attemptRequest()");

        if ( ContextCompat.checkSelfPermission( getApplicationContext()
                , Manifest.permission.INTERNET )
                != PackageManager.PERMISSION_GRANTED )
            requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try
                        {

                            JSONArray arr = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                            currentPlace = new Place();
                            for(int i = 0; i < arr.length(); i++) {
                                JSONObject currObj = arr.getJSONObject(i);
                                JSONArray types = currObj.getJSONArray("types");
                                String type = types.getString(0);
                                switch(type)
                                {
                                    case STATE:
                                        Log.d(TAG, "State: " + currObj.getString("short_name"));
                                        currentPlace.setState(currObj.getString("short_name"));
                                        break;
                                    case CITY:
                                        Log.d(TAG, "City: " + currObj.getString("short_name"));
                                        currentPlace.setCity(currObj.getString("long_name"));
                                        break;
                                    case ZIP:
                                        Log.d(TAG, "Zip: " + currObj.getString("short_name"));
                                        currentPlace.setZip(currObj.getString("short_name"));
                                        break;
                                    case COUNTY:
                                        Log.d(TAG, "County: " + currObj.getString("short_name"));
                                        currentPlace.setCounty(currObj.getString("short_name"));
                                        break;
                                }
                            }

                        }
                        catch (JSONException e)
                        {
                            Log.d(TAG, "Failed: " + e.toString());
                            Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
    }
}


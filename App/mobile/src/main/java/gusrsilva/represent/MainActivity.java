package gusrsilva.represent;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private String TAG = "Represent!";
    public static String REP_NUM = "rep_num", ZIP_CODE = "zip_code";
    public static ArrayList<Rep> repList = new ArrayList<>();
    private ProgressDialog dialog;
    private ListAdapter adt;
    private RecyclerView recyclerView;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "XI1YkrqjI0iKPWtHpxqvjoxY2";
    private static final String TWITTER_SECRET = "JzCQURwTp98Zip9rN5hNIpM68HGzNMG1DFOLa0qztlHtoO0oLe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        // set an exit transition
        //getWindow().setExitTransition(new Explode()); // TODO: Enable

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading Representatives. Please wait...", true);

        Place currentPlace = ChooseLocationActivity.currentPlace;
        if(currentPlace == null) {
            Toast.makeText(getApplicationContext(), "Error retrieving location", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        else if(currentPlace.getPrettyLocation() == null)
            Toast.makeText(getApplicationContext(), "Current Zip: " + currentPlace.getZip(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Current Location: " + currentPlace.getPrettyLocation(), Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView)findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String url = buildUrlFromZip(currentPlace.getZip());
        createRepListFromUrl(url);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

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

    public void moreInfoPressed(View view)
    {
        int pos = Integer.parseInt(view.getTag().toString());
        Log.d(TAG, "Launching moreInfo with pos: " + pos);
        Intent intent = new Intent(getApplicationContext(), ViewRepresentative.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(REP_NUM, pos);
        startActivity(intent);
    }

    private String buildUrlFromZip(String zip)
    {
        String key = getResources().getString(R.string.KEY_SUNLIGHT);
        String url = String.format(Locale.ENGLISH
                ,"http://congress.api.sunlightfoundation.com/legislators/locate?zip=%s&apikey=%s"
                , zip
                , key);
        return url;
    }

    private void createRepListFromUrl(String url)
    {
        Log.d(TAG, "Calling createRepListFromUrl()");

        if ( ContextCompat.checkSelfPermission( getApplicationContext()
                , Manifest.permission.INTERNET )
                != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, 0);
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
                            Log.d(TAG, "Count: " + response.getString("count"));
                            JSONArray results = response.getJSONArray("results");
                            repList = new ArrayList<>();
                            for(int i=0; i<results.length(); i++)
                            {
                                Rep currRep = new Rep();
                                JSONObject curr = results.getJSONObject(i);
                                currRep.setName(curr.getString("first_name") + " " + curr.getString("last_name"));
                                currRep.setBioId(curr.getString("bioguide_id"));
                                currRep.setImageUri(String.format(Locale.ENGLISH
                                        ,"https://theunitedstates.io/images/congress/original/%s.jpg"
                                        , curr.get("bioguide_id")));
                                currRep.setRepType(curr.getString("chamber"));
                                currRep.setEmail(curr.getString("oc_email"));
                                currRep.setParty(curr.getString("party"));
                                if(curr.getString("party").equalsIgnoreCase("R"))
                                    currRep.setColor(ContextCompat.getColor(getApplicationContext(), R.color.rep_red));
                                else
                                    currRep.setColor(ContextCompat.getColor(getApplicationContext(), R.color.dem_blue));
                                currRep.setWebsite(curr.getString("website"));
                                currRep.setTermEnd(curr.getString("term_end"));
                                currRep.setTermStart(curr.getString("term_start"));
                                currRep.setTwitterId(curr.getString("twitter_id"));
                                //Log.d(TAG, currRep.toString());
                                repList.add(currRep);
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.d(TAG, "Failed: " + e.toString());
                            Toast.makeText(getApplicationContext(), "Error retrieving representatives.", Toast.LENGTH_SHORT).show();
                        }


                        updateRecycler();
                        if(dialog != null)
                            dialog.dismiss();

                        getBills();
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
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
            }
        });
    }

    private String buildBillsUrlFromBioguide(String bioguide)
    {
        String key = getResources().getString(R.string.KEY_SUNLIGHT);
        return String.format(Locale.ENGLISH
                , "http://congress.api.sunlightfoundation.com/bills?sponsor_id=%s&apikey=%s"
                , bioguide
                , key);
    }

    private void addRepBillsFromUrl(String url, final int index)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest GET = new JsonObjectRequest(Request.Method.GET
                , url
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Bill> billList = new ArrayList<>();
                    int THRESHOLD_NUM = 10;
                    int count = Integer.parseInt(response.getString("count"));
                    JSONArray arr = response.getJSONArray("results");
                    for(int i = 0; i < arr.length() && i < THRESHOLD_NUM; i++)
                    {
                        Bill bill = new Bill();
                        JSONObject currObj = arr.getJSONObject(i);
                        String introDate = currObj.getString("introduced_on");
                        String title = currObj.getString("short_title");
                        if (title == null)
                            title = currObj.getString("official_title");

                        if(title.equalsIgnoreCase("null"))
                            THRESHOLD_NUM++;
                        else
                        {
                            if(index == 0)
                                Log.d(TAG, "Title: " + title);
                            bill.setName(title);
                            bill.setIntroDate(introDate);
                            billList.add(bill);
                        }
                    }

                    repList.get(index).setBills(billList);

                }
                catch (JSONException e)
                {
                    Log.d(TAG, "JSONException when retreiving bills: " + e.getMessage());
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error retreiving bills: " + error.getMessage());
            }
        });
        queue.add(GET);
    }

    private void getBills()
    {
        for(int i = 0; i < repList.size(); i++)
        {
            if(repList.get(i).getBioId() != null)
            {
                String url = buildBillsUrlFromBioguide(repList.get(i).getBioId());
                addRepBillsFromUrl(url, i);
            }
        }
    }

    private void generateDummyReps()
    {
        // Generate Dummy Representatives
        Rep rep1 = new Rep();
        rep1.setRepType("Senator");
        rep1.setName("Barbara Boxer");
        rep1.setParty("Democrat");
        rep1.setEmail("Sen.Boxer@opencongress.org");
        rep1.setWebsite("www.boxer.senate.gov");
        rep1.setImageResource(R.drawable.rep1);
        rep1.setWideImageResource(R.drawable.rep1_wide);
        rep1.setColor(ContextCompat.getColor(getApplicationContext(), R.color.dem_blue));

        Rep rep2 = new Rep();
        rep2.setRepType("Senator");
        rep2.setName("Dianne Feinstein");
        rep2.setParty("Democrat");
        rep2.setEmail("Sen.Feinstein@opencongress.org");
        rep2.setWebsite("www.feinstein.senate.gov");
        rep2.setImageResource(R.drawable.rep2);
        rep2.setWideImageResource(R.drawable.rep2_wide);
        rep2.setColor(ContextCompat.getColor(getApplicationContext(), R.color.dem_blue));

        Rep rep3 = new Rep();
        rep3.setRepType("Representative");
        rep3.setName("Paul Cook");
        rep3.setParty("Republican");
        rep3.setEmail("Rep.Cook@opencongress.org");
        rep3.setWebsite("www.Cook.representative.gov");
        rep3.setImageResource(R.drawable.rep3);
        rep3.setWideImageResource(R.drawable.rep3_wide);
        rep3.setColor(ContextCompat.getColor(getApplicationContext(), R.color.rep_red));

        repList.add(rep1);repList.add(rep2);repList.add(rep3);repList.add(rep1);
    }

    private void updateRecycler()
    {
        adt = new ListAdapter(repList, getApplicationContext());
        recyclerView.setAdapter(adt);
    }

}

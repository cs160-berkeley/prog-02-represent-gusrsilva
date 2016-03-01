package gusrsilva.represent;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private String TAG = "Represent!";
    public static String REP_NUM = "rep_num";
    public static ArrayList<Rep> repList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set an exit transition
        //getWindow().setExitTransition(new Explode()); // TODO: Enable

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);


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

        repList = new ArrayList<>();
        repList.add(rep1); repList.add(rep2); repList.add(rep3);

        ListAdapter adt = new ListAdapter(repList, getApplicationContext());
        recyclerView.setAdapter(adt);

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
        intent.putExtra(REP_NUM, pos);
        startActivity(intent);
    }

}

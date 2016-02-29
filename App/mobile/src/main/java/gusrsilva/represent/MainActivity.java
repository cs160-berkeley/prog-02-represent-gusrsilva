package gusrsilva.represent;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    public static Rep currentRep = null;
    private ArrayList<Rep> repList = new ArrayList<>();

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
        Rep boxer = new Rep();
        boxer.setRepType("Senator");
        boxer.setName("Barbara Boxer");
        boxer.setParty("Democrat");
        boxer.setEmail("Sen.Boxer@opencongress.org");
        boxer.setWebsite("www.boxer.senate.gov");
        boxer.setImageUri("drawable://" + R.drawable.rep1);

        repList = new ArrayList<>();
        repList.add(boxer); repList.add(boxer); repList.add(boxer);

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
        currentRep = repList.get(pos);
        Intent intent = new Intent(getApplicationContext(), ViewRepresentative.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create((View)fab, "fab"));
        startActivity(intent, options.toBundle());
    }

}

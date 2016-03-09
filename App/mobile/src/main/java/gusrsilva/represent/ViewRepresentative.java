package gusrsilva.represent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ViewRepresentative extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_representative);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This would bring up email, website, etc...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int pos = getIntent().getIntExtra(MainActivity.REP_NUM, -1);

        if(pos == -1 || MainActivity.repList == null || pos >= MainActivity.repList.size())
        {
            Toast.makeText(getApplication(), "Error retreiving representatives", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Rep rep = MainActivity.repList.get(pos);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), R.color.dem_blue));
        collapsingToolbarLayout.setBackgroundResource(R.drawable.capitol);


        TextView partyType = (TextView)findViewById(R.id.party_type);
        TextView name = (TextView)findViewById(R.id.name);
        partyType.setText(rep.getParty() + " " + rep.getRepType());
        name.setText(rep.getName());

        // Set up bills list
        RecyclerView billRecycler = (RecyclerView)findViewById(R.id.billsRecycler);
        billRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        BillAdapter billAdt = new BillAdapter(rep.getBills(), getApplicationContext(), rep.getColor());
        billRecycler.setAdapter(billAdt);
        billRecycler.setNestedScrollingEnabled(false);



    }
}

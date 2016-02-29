package gusrsilva.represent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GridViewPager pager = (GridViewPager)findViewById(R.id.pager);
        //PagerAdapter adapter = new PagerAdapter(getApplicationContext(), getFragmentManager());

        ArrayList<Rep> repList = new ArrayList<>();
        Rep rep1 = new Rep();
        rep1.setRepType("Senator");
        rep1.setName("Barbara Boxer");
        rep1.setParty("Democrat");
        rep1.setEmail("Sen.Boxer@opencongress.org");
        rep1.setWebsite("www.boxer.senate.gov");
        rep1.setImageResource(R.drawable.rep1);
        rep1.setColor(ContextCompat.getColor(getApplicationContext(), R.color.dem_blue));

        Rep rep2 = new Rep();
        rep2.setRepType("Senator");
        rep2.setName("Dianne Feinstein");
        rep2.setParty("Democrat");
        rep2.setImageResource(R.drawable.rep2);
        rep2.setColor(ContextCompat.getColor(getApplicationContext(), R.color.dem_blue));

        Rep rep3 = new Rep();
        rep3.setRepType("Representative");
        rep3.setName("Paul Cook");
        rep3.setParty("Republican");
        rep3.setImageResource(R.drawable.rep3);
        rep3.setColor(ContextCompat.getColor(getApplicationContext(), R.color.rep_red));

        repList.add(rep1);repList.add(rep2);repList.add(rep3);repList.add(rep1);
        CardAdapter adapter = new CardAdapter(repList, getApplicationContext());
        pager.setAdapter(adapter);
    }
}

package gusrsilva.represent;

import android.app.Activity;
import android.os.Bundle;
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
        Rep boxer = new Rep();
        boxer.setRepType("Senator");
        boxer.setName("Barbara Boxer");
        boxer.setParty("Democrat");
        boxer.setEmail("Sen.Boxer@opencongress.org");
        boxer.setWebsite("www.boxer.senate.gov");
        boxer.setImageResource(R.drawable.rep1);

        repList.add(boxer);repList.add(boxer);repList.add(boxer);repList.add(boxer);
        CardAdapter adapter = new CardAdapter(repList, getApplicationContext());
        pager.setAdapter(adapter);
    }
}

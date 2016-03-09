package gusrsilva.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * Created by GusSilva on 2/29/16.
 */
public class CardAdapter extends GridPagerAdapter {

    ArrayList<Rep> repList;
    String[] dummyCities = {"Los Angeles,CA", "Upland, CA", "Quoahog, RI", "Boston, MA", "Berkeley, CA"};
    Context mContext;
    public PieChart mChart;
    private String oldZip = "99999", city = dummyCities[0];
    private int dummyY1 = 29, dummyY2 = 71;
    private String TAG = "Represent!";

    public CardAdapter(ArrayList<Rep> reps, Context context)
    {
        repList = reps;
        mContext = context;
    }

    @Override
    public int getRowCount() {
        //TODO: Dynamically calculate
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return repList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, final int col) {
        View view;
        Rep rep = repList.get(col);
        if(!rep.getName().equalsIgnoreCase("dummy"))
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.rep_card, null);

            TextView type, name, party;
            BoxInsetLayout holder = (BoxInsetLayout) view.findViewById(R.id.holder);

            type = (TextView) view.findViewById(R.id.type);
            name = (TextView) view.findViewById(R.id.name);
            party = (TextView) view.findViewById(R.id.party);

            type.setText(rep.getRepType());
            name.setText(rep.getName());
            party.setText(rep.getParty());
            party.setTextColor(rep.getColor());
            holder.setBackgroundResource(rep.getImageResource());
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.cardClicked(col);
                }
            });
        }
        else
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.pi_screen, null);
            mChart = (PieChart)view.findViewById(R.id.chart);
            mChart.setUsePercentValues(true);
            mChart.setDescription("");
            mChart.getLegend().setEnabled(false);
            //mChart.setExtraOffsets(5, 10, 5, 5);
            mChart.setDrawHoleEnabled(false);
            setData(2, 100);
            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            ((TextView)view.findViewById(R.id.location)).setText(city);
            get2012Info();

        }
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, int i1, Object o) {
        viewGroup.removeView((View) o);

    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

    public void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        //TODO: Dynamically calculate results from API


        Log.d(TAG, " setting PieChart Data. oldZip: " + oldZip + "   currZip: " + MainActivity.zipCode);
        if (!oldZip.equalsIgnoreCase(MainActivity.zipCode))
        {
            Log.d(TAG, "Setting new location");
            oldZip = MainActivity.zipCode;
            Random rand = new Random(System.currentTimeMillis());
            String oldCity = dummyCities[rand.nextInt(5)];
            dummyY1 = rand.nextInt(30) + 30;
            dummyY2 = 100 - dummyY1;

            while(!city.equalsIgnoreCase(oldCity))
            {
                city = dummyCities[rand.nextInt(5)];
                dummyY1 = rand.nextInt(30) + 30;
                dummyY2 = 100 - dummyY1;
            }

            Log.d(TAG, "New values: " + dummyY1 + ", " + dummyY2);

        }
        else
        {
            Log.d(TAG, "Zip is the same!");
        }
        yVals1.add(new Entry((float) dummyY1, 0));
        yVals1.add(new Entry((float) dummyY2, 1));

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Romney"); xVals.add("Obama");

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(0f);
        //dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(mContext, R.color.rep_red));
        colors.add(ContextCompat.getColor(mContext, R.color.dem_blue));


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    public void get2012Info()
    {
        String url = "https://raw.githubusercontent.com/cs160-sp16/voting-data/master/election-county-2012.json";
        RequestQueue queue = Volley.newRequestQueue(mContext);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url
                ,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "JSON response: " + response.toString().substring(0,100));
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error with 2012 info");
            }
        });
        queue.add(getRequest);
    }
}


package gusrsilva.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by GusSilva on 2/29/16.
 */
public class CardAdapter extends GridPagerAdapter {

    ArrayList<Rep> repList;
    Context mContext;
    PieChart mChart;

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

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        //TODO: Dynamically calculate results from API
        yVals1.add(new Entry((float) 29, 0));
        yVals1.add(new Entry((float) 71, 1));

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
}


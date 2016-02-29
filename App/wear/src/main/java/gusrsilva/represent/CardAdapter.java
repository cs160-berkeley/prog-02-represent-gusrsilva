package gusrsilva.represent;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by GusSilva on 2/29/16.
 */
public class CardAdapter extends GridPagerAdapter {

    ArrayList<Rep> repList;
    Context mContext;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.rep_card, null);

        Rep rep = repList.get(col);
        TextView type, name, party;
        BoxInsetLayout holder = (BoxInsetLayout)view.findViewById(R.id.holder);

        type = (TextView)view.findViewById(R.id.type);
        name = (TextView)view.findViewById(R.id.name);
        party = (TextView)view.findViewById(R.id.party);

        type.setText(rep.getRepType());
        name.setText(rep.getName());
        party.setText(rep.getParty());
        party.setTextColor(rep.getColor());
        holder.setBackgroundResource(rep.getImageResource());
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClicked(col);
            }
        });



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

    private void cardClicked(int col)
    {
        Toast.makeText(mContext, "Clicked: " + repList.get(col).getName(), Toast.LENGTH_SHORT).show();
    }
}


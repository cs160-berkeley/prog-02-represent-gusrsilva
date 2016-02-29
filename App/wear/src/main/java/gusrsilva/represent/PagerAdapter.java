package gusrsilva.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GusSilva on 2/28/16.
 */
public class PagerAdapter extends FragmentGridPagerAdapter{

    private final Context mContext;
    private List mRows;

    ArrayList<Rep> repList = new ArrayList<>();

    public PagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;

        Rep boxer = new Rep();
        boxer.setRepType("Senator");
        boxer.setName("Barbara Boxer");
        boxer.setParty("Democrat");
        boxer.setEmail("Sen.Boxer@opencongress.org");
        boxer.setWebsite("www.boxer.senate.gov");
        boxer.setImageResource(R.drawable.rep1);

        repList.add(boxer);repList.add(boxer);repList.add(boxer);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Rep rep = repList.get(col);
        String title = rep.getName();
        String text = rep.getParty();
        CardFragment fragment = CardFragment.create(title, text);

        // Advanced settings (card gravity, card expansion/scrolling)
        /*
        fragment.setCardGravity(page.cardGravity);
        fragment.setExpansionEnabled(page.expansionEnabled);
        fragment.setExpansionDirection(page.expansionDirection);
        fragment.setExpansionFactor(page.expansionFactor);
        */
        return fragment;
    }

    @Override
    public int getRowCount() {
        //TODO: Dynamically calculate
        return 3;
    }

    @Override
    public int getColumnCount(int i) {
        //TODO: Dynamically calculate
        return 3;
    }

    @Override
    public Drawable getBackgroundForRow(int row) {
        //TODO: Dynamically find image and generate Drawable
        return ContextCompat.getDrawable(mContext, R.drawable.rep1);
    }
}

package gusrsilva.represent;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by GusSilva on 2/24/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<Rep> repList = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ListAdapter(ArrayList<Rep> reps, Context c)
    {
        //Constructor
        context = c;
        repList = reps;
        imageLoader = ImageLoader.getInstance(); // Get singleton instance

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.rep3) // resource or drawable
                .showImageOnFail(R.drawable.rep3) // TODO: Add a stock imaage
                .resetViewBeforeLoading(true)  // default
                .cacheInMemory(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rep_card, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rep currentRep = repList.get(position);

        if(holder != null)
        {
            holder.repType.setText(currentRep.getRepType());
            holder.name.setText(currentRep.getName());
            holder.party.setText(currentRep.getParty());
            holder.email.setText(currentRep.getEmail());
            holder.website.setText(currentRep.getWebsite());
            holder.moreInfo.setTag(position);

            imageLoader.displayImage("drawable://" + currentRep.getImageResource(), holder.image, options);

            holder.email.setTextColor(currentRep.getColor());
            holder.website.setTextColor(currentRep.getColor());
            holder.moreInfo.setTextColor(currentRep.getColor());
        }

    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView repType, name, party, email, website;
        public ImageView image;
        public Button moreInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            //get title and artist views
            repType = (TextView) itemView.findViewById(R.id.rep_type);
            name = (TextView) itemView.findViewById(R.id.name);
            party = (TextView) itemView.findViewById(R.id.party);
            email = (TextView) itemView.findViewById(R.id.email);
            website = (TextView) itemView.findViewById(R.id.website);
            image = (ImageView) itemView.findViewById(R.id.rep_image);
            moreInfo = (Button) itemView.findViewById(R.id.more_info);
        }

    }
}

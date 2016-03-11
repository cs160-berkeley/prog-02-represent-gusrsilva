package gusrsilva.represent.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;
import java.util.Locale;

import gusrsilva.represent.R;
import gusrsilva.represent.Objects.Rep;

/**
 * Created by GusSilva on 2/24/16.
 */
public class RepsAdapter extends RecyclerView.Adapter<RepsAdapter.ViewHolder> {

    private ArrayList<Rep> repList = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public RepsAdapter(ArrayList<Rep> reps, Context c)
    {
        //Constructor
        context = c;
        repList = reps;
        imageLoader = ImageLoader.getInstance(); // Get singleton instance

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.def)
                .showImageOnFail(R.drawable.def)
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Rep currentRep = repList.get(position);

        if(holder != null)
        {
            holder.repType.setText(currentRep.getRepType());
            holder.name.setText(currentRep.getName());
            holder.party.setText(currentRep.getParty());
            holder.email.setText(currentRep.getEmail());
            holder.website.setText(currentRep.getWebsite());
            holder.moreInfo.setTag(position);

            // TODO: Base this Tweet ID on some data from elsewhere in your app
            long tweetId = 631879971628183552L;
            TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    TweetView tweetView = new TweetView(context, result.data);
                    holder.tweetHolder.addView(tweetView);
                }
                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Load Tweet failure", exception);
                }
            });


            imageLoader.displayImage(currentRep.getImageUri(), holder.image, options);

            holder.email.setTextColor(currentRep.getColor());
            holder.website.setTextColor(currentRep.getColor());
            holder.moreInfo.setTextColor(currentRep.getColor());

            holder.website.setLinkTextColor(currentRep.getColor());
            holder.email.setLinkTextColor(currentRep.getColor());
            holder.website.setLinksClickable(true);
            holder.email.setLinksClickable(true);

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
        public FrameLayout tweetHolder;

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
            tweetHolder = (FrameLayout)itemView.findViewById(R.id.tweet_holder);
        }

    }
}

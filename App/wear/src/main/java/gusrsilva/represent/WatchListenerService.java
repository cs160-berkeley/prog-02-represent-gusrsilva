package gusrsilva.represent;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by GusSilva on 2/29/16.
 */
public class WatchListenerService extends WearableListenerService{

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Toast.makeText(getApplicationContext(), "Message Received!", Toast.LENGTH_SHORT).show();

        if(messageEvent.getPath().equalsIgnoreCase("/Barbara"))
        {
            Log.d("REP", "Success!");
        }
        else
        {
            Log.d("REP", "Failure!");
        }
    }
}

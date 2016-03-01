package gusrsilva.represent;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by GusSilva on 2/29/16.
 */
public class WatchListenerService extends WearableListenerService{
    private String TAG = "Represent!";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //Toast.makeText(getApplicationContext(), "Message Received!", Toast.LENGTH_SHORT).show();

        if(messageEvent.getPath().equalsIgnoreCase("/show_reps"))
        {
            Log.d(TAG, "Launching MainActivity");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Log.d(TAG, "Failure!");
        }
    }
}

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
        String zip = new String(messageEvent.getData());
        if(messageEvent.getPath().equalsIgnoreCase("/ZIP_CODE"))
        {
            //Log.d(TAG, "Launching MainActivity");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ZIP_CODE", zip);
            startActivity(intent);
        }
        else
        {
            Log.d(TAG, "Failure!");
        }
    }
}

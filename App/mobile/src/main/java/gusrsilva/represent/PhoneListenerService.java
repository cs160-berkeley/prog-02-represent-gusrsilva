package gusrsilva.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by GusSilva on 2/29/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String REP_NUM = "/rep_number";
    private String TAG = "Represent!";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "in PhoneListenerService, got: " + messageEvent.getPath());
        //Toast.makeText(getApplicationContext(), "MessageReceived!", Toast.LENGTH_SHORT).show();
        if( messageEvent.getPath().equalsIgnoreCase(REP_NUM) ) {

            Log.d(TAG, "Message matched, starting activity!");

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
            int repNum = Integer.parseInt(value);
            Intent intent = new Intent(getApplicationContext(), ViewRepresentative.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MainActivity.REP_NUM, repNum);
            startActivity(intent);


            //Toast toast = Toast.makeText(context, value, duration);
            //toast.show();

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else {
            Log.d(TAG, " message did not match!");
            super.onMessageReceived( messageEvent );
        }

    }
}

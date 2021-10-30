package pwc.appdev.speedalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mServiceIntent = new Intent(context, Services.class);
        mServiceIntent.setAction(Services.ACTION_START_FOREGROUND_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.startForegroundService(mServiceIntent);
        }

        else {

            context.startService(mServiceIntent);
        }
    }
}

package pwc.appdev.speedalert;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.intentfilter.androidpermissions.PermissionManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import static java.util.Collections.singleton;

public class Services extends Service {

    private static final String TAG = Services.class.getSimpleName();
    private static final int LOCATION_INTERVAL = 500;
    private static final float LOCATION_DISTANCE = 0;
    private LocationManager mLocationManager = null;
    Location locations;
    Location mCurrentLocation, lStart, lEnd;
    static double distance = 0;
    double speed, oldspeed = 0.00;
    private FirebaseAuth auth;
    private String email = "";
    String oras = "";
    Double lat1, lat2, lng1, lng2;
    FirebaseDatabase fd;
    DatabaseReference dr;

    public Services() {
    }

    public Services(Context applicationContext) {

        super();
    }

    @Override
    public void onCreate() {

        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(singleton(android.Manifest.permission.ACCESS_FINE_LOCATION), new PermissionManager.PermissionRequestListener(){
            @Override
            public void onPermissionGranted() {
                Log.e(TAG, "onCreate");
                initializeLocationManager();
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[0]);
                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "Failed to request Location Update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "GPS Provider does not exist " + ex.getMessage());
                }
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            locations = location;
            mLastLocation.set(location);
            speed = location.getSpeed() * 18 / 5;
            System.out.println("Current Speed is (Unedited): "+location.getSpeed());
            System.out.println("Current Bearing is (Unedited): "+location.getBearing());
            System.out.println("Current Speed is: "+speed);
            lat1 = location.getLatitude();
            lng1 = location.getLongitude();
            System.out.println("Current Latitude is: "+lat1);
            System.out.println("Current Longitude is: "+lng1);
            compare();
            setVal(location);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER)
    };


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service started");
        showNotification(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        if (users != null) {

            email = users.getEmail();

        }
        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference();

        new Handler(Looper.getMainLooper()).postDelayed(() -> setVal(locations), 500);

        return START_STICKY;
    }

    private void setValues (String speed, String time, String distance, String average) {

        Initial i = new Initial();
        i.setSpeed(speed);
        i.setTime(time);
        i.setDistance(distance);
        i.setAverage(average);
        i.setLocation(""+lat1+" ,"+lng1+"");

        String[] parts = email.split("@");
        dr.child("Users").child(parts[0]).child("Vehicle Data").setValue(i);

    }


    public void compare(){

        MediaPlayer mp = new MediaPlayer();

        try{

            mp.start();

        }

        catch(Exception e){

            Toast.makeText(getApplicationContext(), "Can't Calculate Locations", Toast.LENGTH_LONG).show();
        }

    }

    private void setVal(Location location){

        Log.i(TAG, "Last location: " + location);
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else
            lEnd = mCurrentLocation;

        if(oldspeed < speed)
            oldspeed = speed;
        else if(oldspeed > speed)
            oldspeed = oldspeed;
        updateUI();

    }

    private void updateUI() {

        try{

            if (MapFragment.p == 0) {

                MapFragment.lat = lat1;
                MapFragment.lng = lng1;
                distance = distance + (lStart.distanceTo(lEnd) / 1000.00);
                long endtime = System.currentTimeMillis();
                long diff = endtime - login.startTime;
                diff = TimeUnit.MILLISECONDS.toMinutes(diff);
                oras = Long.toString(diff);
                MapFragment.time.setText(oras);
                if (speed > 0.0)
                    MapFragment.speed.setText(new DecimalFormat("#.##").format(speed));
                else
                    MapFragment.speed.setText("0.00");

                MapFragment.distance.setText(new DecimalFormat("#.###").format(distance));
                MapFragment.average.setText(new DecimalFormat("#.###").format(oldspeed));
                lStart = lEnd;
                String newspeed = (new DecimalFormat("#.##").format(speed));
                String newdistance = (new DecimalFormat("#.###").format(distance));
                String newaverage = (new DecimalFormat("#.###").format(oldspeed));
                setValues(newspeed, oras, newdistance, newaverage);

            }

        }

        catch(Exception e){

            Log.e(TAG, "onUpdateUI: " + e);
        }

    }

    public void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent intent1  = new Intent(this, main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("SpeedAlert")
                .setContentText("Your Location and Vehicle Speed is being monitored.")
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        startForeground(notificationId, notification);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        stopForeground(false);
        stopSelf();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("RestartSensor");
        broadcastIntent.setClass(this, Receiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();

    }
}

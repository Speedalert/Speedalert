package pwc.appdev.speedalert;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.Date;

public class login extends AppCompatActivity {

    Button register, login, reset;
    TextInputEditText eusername, epass;
    TextInputLayout lusername, lpass;
    private FirebaseAuth auth;
    boolean isConnected = true;
    private boolean monitoringConnectivity = false;
    FirebaseDatabase firebaseDatabase, fd, fd1, fd2, fd3;
    DatabaseReference databaseReference, dr, dr1, dr2, dr3;
    private ProgressBar bar;
    private ConstraintLayout layout;
    private static String passwords = " ";
    private static String emails = " ";
    static long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(!CheckGpsStatus()){

            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        Services services = new Services(this);
        Intent mServiceIntent = new Intent(login.this, Services.class);
        mServiceIntent.setAction(Services.ACTION_STOP_FOREGROUND_SERVICE);

        if (isMyServiceRunning(services.getClass())) {

            startForegroundService(mServiceIntent);
        }

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){

            startActivity(new Intent(login.this, main.class));
            finish();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        layout = findViewById(R.id.loginconstraint);

        register = findViewById(R.id.registernewuser);
        login = findViewById(R.id.loginbutton);
        reset = findViewById(R.id.forgotpassword);
        bar = findViewById(R.id.progressBarlogin);
        eusername = findViewById(R.id.editusername);
        epass = findViewById(R.id.editpassword);
        lusername = findViewById(R.id.username);
        lpass = findViewById(R.id.password);
        FirebaseApp.initializeApp(login.this);
        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference();

        register.setOnClickListener(v -> startActivity(new Intent(login.this, Signup.class)));

        reset.setOnClickListener(v -> startActivity(new Intent(login.this, reset.class)));

        login.setOnClickListener(v -> {

            try {

                final String fuser = eusername.getText().toString().trim();
                final String fpass = epass.getText().toString().trim();

                if(TextUtils.isEmpty(fuser) && TextUtils.isEmpty(fpass)){

                    lusername.setError("A username is required");
                    lpass.setError("A password is required");
                }

                else if(TextUtils.isEmpty(fuser)) {

                    lusername.setError("A username is required");
                }

                else if(TextUtils.isEmpty(fpass)) {

                    lpass.setError("A password is required");
                }

                else if(!isConnected){

                    Snackbar sn = Snackbar.make(layout, "Authentication Failed, check your internet connection.", Snackbar.LENGTH_SHORT);
                    sn.show();
                }

                else {

                    getEmail(fuser);
                }

            }

            catch(Exception e){

                Log.e("On Login Button Clicked: ", e.getMessage(), e);

            }

        });


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }

        return false;
    }

    public boolean CheckGpsStatus() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return GpsStatus;
    }

    private void getEmail(String user){

        try{

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            databaseReference.child("Emails").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try{

                        if(snapshot != null){

                            emails = snapshot.getValue(String.class);
                            passwords = epass.getText().toString().trim();
                            if(emails != null){

                                bar.setVisibility(View.VISIBLE);
                                auth.signInWithEmailAndPassword(emails, passwords)
                                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {

                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if(task.isSuccessful()){

                                                    setInitialValues();
                                                    startTime = System.currentTimeMillis();
                                                    setStartTime(startTime);
                                                    bar.setVisibility(View.GONE);
                                                    Intent intent = new Intent(login.this, main.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                else {

                                                    bar.setVisibility(View.GONE);
                                                    Snackbar sn3 = Snackbar.make(layout, "Authentication Failed, check your credentials.", Snackbar.LENGTH_SHORT);
                                                    sn3.show();
                                                    eusername.setText(null);
                                                    epass.setText(null);


                                                }

                                            }
                                        });
                            }

                            else {

                                Snackbar sn2 = Snackbar.make(layout, "No email address found for this username, please register first.", Snackbar.LENGTH_SHORT);
                                sn2.show();
                            }
                        }

                        else {

                            Snackbar sn1 = Snackbar.make(layout, "Account not registered, please register first.", Snackbar.LENGTH_SHORT);
                            sn1.show();
                            Intent intent = new Intent(login.this, Signup.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    catch(Exception e){

                        Log.e("On Data Change (getEmail): ", e.getMessage(), e);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });
        }

        catch(Exception e){

            Log.e("On Email Retrieval: ", e.getMessage(), e);
        }
    }

    private void setStartTime(Long a){

        try{

            fd3 = FirebaseDatabase.getInstance();
            dr3 = fd3.getReference();
            String[] parts = emails.split("@");
            dr3.child("Users").child(parts[0]).child("Start Time").setValue(a);
        }

        catch(Exception e){

            System.out.println("Login (setStartTime): "+e);
        }
    }

    private void setInitialValues() {

        try{

            Initial i = new Initial();
            i.setSpeed("0.000");
            i.setTime("0.0");
            i.setDistance("0.000");
            i.setAverage("0.000");
            i.setLocation("0.0");

            String[] parts = emails.split("@");
            dr.child("Users").child(parts[0]).child("Vehicle Data").setValue(i);
            addLoginTime();
        }

        catch(Exception e){

            System.out.println("Login (setInitialValues): "+e);
        }
    }

    private void addLoginTime() {

        try{

            fd1 = FirebaseDatabase.getInstance();
            dr1 = fd1.getReference();
            Date currentTime = Calendar.getInstance().getTime();
            String[] parts = emails.split("@");
            dr1.child("Users").child(parts[0]).child("Last Login Date & Time").setValue(currentTime.toString());
            setStatus();

        }

        catch(Exception e){

            System.out.println("Login (addLoginTime): "+e);
        }
    }

    private void setStatus() {

        try{

            fd2 = FirebaseDatabase.getInstance();
            dr2 = fd2.getReference();
            String[] parts = emails.split("@");
            dr2.child("Users").child(parts[0]).child("User Status").setValue("Active");
        }

        catch(Exception e){

            System.out.println("Login (setStatus): "+e);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        checkConnectivity();

    }

    @Override
    protected void onPause() {

        if (monitoringConnectivity) {

            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;

        }

        super.onPause();

    }

    private final ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;

            Snackbar snonavail = Snackbar.make(layout, "Connection Established!", Snackbar.LENGTH_SHORT);
            snonavail.show();

        }
        @Override
        public void onLost(Network network) {
            isConnected = false;

            Snackbar snonlost = Snackbar.make(layout, "Connection Lost!", Snackbar.LENGTH_INDEFINITE);
            snonlost.show();
        }
    };

    private void checkConnectivity() {

        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {

            Snackbar sncc = Snackbar.make(layout, "No Connection!", Snackbar.LENGTH_INDEFINITE);
            sncc.show();
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;
        }
        else {

            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;

        }

    }
}




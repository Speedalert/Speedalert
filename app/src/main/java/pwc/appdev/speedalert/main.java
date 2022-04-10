package pwc.appdev.speedalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

public class main extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener,
                    ProfileFragment.OnFragmentInteractionListener,
                        MapFragment.OnFragmentInteractionListener {

    private FirebaseAuth auth;
    private String useremail = " ";
    public String name = " ";
    boolean isConnected = true;
    private boolean monitoringConnectivity = false;
    private CircleImageView imageViewNav;
    private TextView fname,femail;
    private DrawerLayout drawer, mRoot;
    private String[] parts = new String[2];
    private BroadcastReceiver mReceiver = null;
    FirebaseDatabase fd, fd1, fd2, fd3, fd4;
    DatabaseReference dr, dr1, dr2, dr3, dr4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Services services = new Services(this);
        Intent mServiceIntent = new Intent(main.this, Services.class);
        mServiceIntent.setAction(Services.ACTION_START_FOREGROUND_SERVICE);

        if (!isMyServiceRunning(services.getClass())) {

            startForegroundService(mServiceIntent);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().hide();
        }
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("MESSAGE");
                Snackbar msg = Snackbar.make(mRoot, message, Snackbar.LENGTH_LONG);
                msg.show();
            }
        };
        auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        if (users != null) {

            useremail = users.getEmail();
            String[] part = useremail.split("@");
            parts[0] = part[0];
            getName();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        fname = (TextView)header.findViewById(R.id.navusername);
        femail = (TextView)header.findViewById(R.id.navuseremail);
        imageViewNav = (CircleImageView)header.findViewById(R.id.imageViewNav);

        MapFragment p = new MapFragment();
        FragmentManager f = getSupportFragmentManager();
        f.beginTransaction().replace(R.id.mainLayout, p).addToBackStack(null).commit();

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

    public void signOut() {

        try{

            fd = FirebaseDatabase.getInstance();
            dr = fd.getReference();
            SimpleDateFormat simpleTimeStampFormat;
            simpleTimeStampFormat = new SimpleDateFormat(getString(R.string.timestamp), Locale.US);
            String timestamp = simpleTimeStampFormat.format(new Date());
            String[] parts = useremail.split("@");
            dr.child("Users").child(parts[0]).child("LastLogoutDateTime").setValue(timestamp);
            auth.signOut();
            setStatusInActiveLogout();
            setRemarks();
            finish();
            Intent intent = new Intent(main.this, login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        catch(Exception e){

            Log.e("Main (onSignOut): ", e.getMessage(), e);

        }
    }

    private void setStatusActive() {

        try{

            fd1 = FirebaseDatabase.getInstance();
            dr1 = fd1.getReference();
            String[] parts = useremail.split("@");
            dr1.child("Users").child(parts[0]).child("UserStatus").setValue("User is now active.");
        }

        catch(Exception e){

            System.out.println("Main (setStatusActive): "+e);
        }
    }

    private void setStatusInActive() {

        try{

            fd2 = FirebaseDatabase.getInstance();
            dr2 = fd2.getReference();
            String[] parts = useremail.split("@");
            dr2.child("Users").child(parts[0]).child("UserStatus").setValue("User is inactive.");
        }

        catch(Exception e){

            System.out.println("Main (setStatusInactive): "+e);
        }
    }

    private void setStatusInActiveLogout() {

        try{

            fd4 = FirebaseDatabase.getInstance();
            dr4 = fd4.getReference();
            String[] parts = useremail.split("@");
            dr4.child("Users").child(parts[0]).child("UserStatus").setValue("User is signed out.");
        }

        catch(Exception e){

            System.out.println("Main (setStatusInactiveLogout): "+e);
        }
    }

    private void setRemarks() {

        try{

            fd3 = FirebaseDatabase.getInstance();
            dr3 = fd3.getReference();
            String[] parts = useremail.split("@");
            dr3.child("Users").child(parts[0]).child("Remarks").setValue("Vehicle is not moving.");
        }

        catch(Exception e){

            System.out.println("Main (setRemarks): "+e);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.profile) {

            ProfileFragment p = new ProfileFragment();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.mainLayout, p).addToBackStack(null).commit();

        }

        else if (id == R.id.home){

            MapFragment p = new MapFragment();
            FragmentManager f = getSupportFragmentManager();
            f.beginTransaction().replace(R.id.mainLayout, p).addToBackStack(null).commit();

        }

        else if (id == R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(main.this);
            builder.setTitle("Please confirm action!");
            builder.setMessage("Are you sure you want to Logout?");
            builder.setIcon(R.drawable.speedalert);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    signOut();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                    Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();


                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onStart(){

        super.onStart();

    }

    @Override
    protected void onDestroy() {

        setStatusInActive();
        super.onDestroy();

    }

    @Override
    public void onResume() {

        setStatusActive();
        checkConnectivity();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("EVENT_SNACKBAR"));
        getName();
        super.onResume();

    }

    @Override
    public void onPause(){

        setStatusInActive();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {

            super.onBackPressed();
        }
        else {

            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void getName(){

        try{

            System.out.println("My Email Address is: "+parts[0]);
            DatabaseReference getname = FirebaseDatabase.getInstance().getReference().child("Users");
            getname.child(parts[0]).child("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null){

                        String namea = dataSnapshot.getValue(String.class);
                        try{

                            if(namea!=null){

                                name = namea;
                                getProfile();

                            }
                            else{

                                Log.d("onGetName", "No Name Retrieved.");
                            }
                        }
                        catch(Exception e){

                            Log.e("onGetName", e.getMessage(), e);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e){

            Log.e("onGetName", e.getMessage(), e);
        }
    }

    private void getProfile(){

        try{

            DatabaseReference getuser = FirebaseDatabase.getInstance().getReference().child("Users").child(parts[0]);
            getuser.child("ProfilePicture").child("imageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null){

                        String urladds = dataSnapshot.getValue(String.class);
                        try{

                            if(urladds!=null){

                                Glide.with(main.this).load(urladds).into(imageViewNav);
                                fname.setText(name);
                                femail.setText(useremail);
                            }
                            else{

                                Log.d("getProfile", "No Profile was retrieved");
                            }
                        }
                        catch(Exception e){

                            Log.e("onGetProfile", e.getMessage(), e);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch(Exception e){

            Log.e("onGetProfile", e.getMessage(), e);
        }

    }


    private final ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
            mRoot = (DrawerLayout) findViewById(R.id.drawer_layout);
            Snackbar snonavail = Snackbar.make(mRoot, "Connection Established!", Snackbar.LENGTH_SHORT);
            snonavail.show();

        }
        @Override
        public void onLost(Network network) {
            isConnected = false;
            mRoot = (DrawerLayout) findViewById(R.id.drawer_layout);
            Snackbar snonlost = Snackbar.make(mRoot, "Connection Lost!", Snackbar.LENGTH_INDEFINITE);
            snonlost.show();
        }
    };

    private void checkConnectivity() {

        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            mRoot = (DrawerLayout) findViewById(R.id.drawer_layout);
            Snackbar sncc = Snackbar.make(mRoot, "No Connection!", Snackbar.LENGTH_INDEFINITE);
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
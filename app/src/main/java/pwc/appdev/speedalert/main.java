package pwc.appdev.speedalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class main extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener,
                    ProfileFragment.OnFragmentInteractionListener,
                        MapFragment.OnFragmentInteractionListener {

    private FirebaseAuth auth;
    private String useremail = " ";
    private String name = " ";
    private CircleImageView imageViewNav;
    private TextView fname,femail;
    private DrawerLayout drawer;
    private String[] parts = new String[2];
    FirebaseDatabase fd;
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Services services = new Services(this);
        Intent mServiceIntent = new Intent(main.this, Services.class);
        mServiceIntent.setAction(Services.ACTION_START_FOREGROUND_SERVICE);

        if (!isMyServiceRunning(services.getClass())) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                startForegroundService(mServiceIntent);
            }

            else {

                startService(mServiceIntent);
            }
        }
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().hide();
        }

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
            Date currentTime = Calendar.getInstance().getTime();
            String[] parts = useremail.split("@");
            dr.child("Users").child(parts[0]).child("Last Logout Date & Time").setValue(currentTime.toString());
            auth.signOut();
            Intent intent = new Intent(main.this, login.class);
            startActivity(intent);
        }

        catch(Exception e){

            Log.e("onSignOut", e.getMessage(), e);

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

//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("RestartSensor");
//        broadcastIntent.setClass(this, Receiver.class);
//        this.sendBroadcast(broadcastIntent);
        super.onDestroy();

    }

    @Override
    public void onResume() {

        super.onResume();

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
}
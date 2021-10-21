package pwc.appdev.speedalert;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private GoogleMap mMap;
    static int p = 0;
    public static TextView speed, distance, time, average;
    private Circle mCircle, mCircle1, mCircle2;
    static double lat, lng;
    private static final String mTitle = "Current Location";
    Marker marker1;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);
        speed = view.findViewById(R.id.speed);
        distance = view.findViewById(R.id.distance);
        time = view.findViewById(R.id.time);
        average = view.findViewById(R.id.avg_speed);
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync((OnMapReadyCallback) this);

        final Handler handler = new Handler();
        final int delay = 2000;
        handler.postDelayed(new Runnable(){
            public void run(){

                setCamera(lat, lng, mTitle);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onMapReady (GoogleMap googleMap){

        try{

            mMap = googleMap;
            LatLng loc = new LatLng(7.088769, 125.620268);
            drawMarkerWithCircle(loc);
            drawMarkerWithCircle1(loc);
            drawMarkerWithCircle2(loc);

        }

        catch(Exception e){

            Log.e("onMapReady", e.getMessage(), e);
        }
    }

    private void drawMarkerWithCircle (LatLng position){

        try {

            double radiusInMeters = 3500;
            int strokeColor = 0x33FFFFFF;
            int shadeColor = 0x33FFFFFF;

            CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
            mCircle = mMap.addCircle(circleOptions);
        }
        catch (Exception e) {

            Log.e("onDrawCircle", e.getMessage(), e);
        }

    }

    private void drawMarkerWithCircle1 (LatLng position){

        try {

            double radiusInMeters = 10000;
            int strokeColor = 0x33FFFFFF;
            int shadeColor = 0x33FFFFFF;

            CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
            mCircle1 = mMap.addCircle(circleOptions);
        }
        catch (Exception e) {

            Log.e("onDrawCircle", e.getMessage(), e);
        }

    }

    private void drawMarkerWithCircle2 (LatLng position){

        try {

            double radiusInMeters = 17000;
            int strokeColor = 0x33FFFFFF;
            int shadeColor = 0x33FFFFFF;

            CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
            mCircle2 = mMap.addCircle(circleOptions);
        }
        catch (Exception e) {

            Log.e("onDrawCircle", e.getMessage(), e);
        }

    }

    public void setCamera (Double lat, Double lng, String title){

        try {

            LatLng loc = new LatLng(lat, lng);
            System.out.println("Set Camera Current Location: "+lat+" ,"+lng+"");
            System.out.println("Location Value: "+loc);
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));
            marker1 = mMap.addMarker(new MarkerOptions().position(loc).title(title));
        }
        catch (Exception e) {

            Log.e("onMoveCamera", e.getMessage(), e);
        }
    }

    public void setCamera1 (Double lat, Double lng){

        try {

            LatLng loc = new LatLng(lat, lng);
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));
        }
        catch (Exception e) {

            Log.e("onMoveCameraStart", e.getMessage(), e);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}

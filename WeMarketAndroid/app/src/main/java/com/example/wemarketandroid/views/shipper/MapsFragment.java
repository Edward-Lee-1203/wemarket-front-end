package com.example.wemarketandroid.views.shipper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentShipperMapsBinding;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.viewmodels.shipper.DeliverySharedViewModel;
import com.example.wemarketandroid.viewmodels.shipper.MapsViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private MainActivity mContainingActivity;
    private FragmentShipperMapsBinding mViewBinding;
    private MapsViewModel mViewModel;
    private DeliverySharedViewModel mSharedViewModel;
    private FusedLocationProviderClient locationProviderClient;
    private GoogleMap googleMap;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float STREET_ZOOM_LEVEL = 14;
    private int callbackCount;
    private int locationUpdateCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentShipperMapsBinding.inflate(inflater, container, false);
        View rootView = mViewBinding.getRoot();
        mContainingActivity = (MainActivity) getActivity();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(mContainingActivity.getApplication()).create(MapsViewModel.class);
        mSharedViewModel = new ViewModelProvider(mContainingActivity).get(DeliverySharedViewModel.class);
        // setup maps
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_shipper_maps);
        mapFragment.getMapAsync(this);
        // create list of markers from delivery order
        Delivery delivery = mSharedViewModel.getmDeliveryMutableLiveData().getValue();
        initializeDeliveryMarkers(delivery);
        // setup location api
        locationProviderClient = LocationServices.getFusedLocationProviderClient(mContainingActivity);
        // initialize location update callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // debugging: log callback counts
                callbackCount++;
                Log.d("MapsFragment","Callback count = "+callbackCount);
                if(locationResult==null) return;    // skips if no result
                // debugging: log update counts
                locationUpdateCount++;
                Log.d("MapsFragment","Location update count = "+locationUpdateCount);
                Marker shipperLocation = mViewModel.getShipperMarkerLiveData().getValue();
                for(Location location : locationResult.getLocations()){
                    // shows updated location on map
                    shipperLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                mViewModel.getShipperMarkerLiveData().postValue(shipperLocation);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shipperLocation.getPosition(),STREET_ZOOM_LEVEL));
            }
        };
        // bind button
        mViewBinding.buttonShipperMapsConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: call API to confirm order
                mContainingActivity.getNavController().navigate(R.id.destination_shipper_orders);
            }
        });

        return rootView;
    }

    private void initializeDeliveryMarkers(Delivery delivery){
        ArrayList<Marker> markers = new ArrayList<>();
        for(OrderDetail orderDetail : delivery.getOrder().getOrderDetailList()){
            Market market = orderDetail.getFood().getMarket();
            // TODO: create marker
        }
        // TODO: create delivery location marker
        mViewModel.getOrderMarkersLiveData().postValue(markers);
    }

    private void initializeShipperMarker(LatLng shipperLocation){
        Marker marker = googleMap.addMarker(new MarkerOptions().position(shipperLocation).title("You").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_current_location)));
        mViewModel.getShipperMarkerLiveData().postValue(marker);
    }

    // shows data on map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        displayLatestLocation();

    }

    private void displayLatestLocation(){
        // checks if any location permission is granted
        if(ActivityCompat.checkSelfPermission(mContainingActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContainingActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        // gets and shows current location on map
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location==null){
                    Toast.makeText(mContainingActivity,"Last location is null",Toast.LENGTH_SHORT).show();
                } else{
                    LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                    initializeShipperMarker(pos);

//                    mViewModel.getShipperMarkerLiveData().postValue(googleMap.addMarker(new MarkerOptions().position(pos).title("You")));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,STREET_ZOOM_LEVEL));
                    // TODO: render the marketList of markers
                    registerLocationUpdate();
                }
            }
        });
    }

    /**
     * Registers location update request and the process location update callback
     */
    private void registerLocationUpdate(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(mContainingActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(mContainingActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. register location update callback
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

            }
        });
        task.addOnFailureListener(mContainingActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContainingActivity,"Failed to register location request",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestLocationPermission(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     * Actively asks permission then show locations on map
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode != LOCATION_PERMISSION_REQUEST_CODE) return;
        // try displaying locations on map again
        displayLatestLocation();
    }

    @Override
    public void onResume() {
        super.onResume();

        mContainingActivity.getmBottomNavBar().setVisibility(View.VISIBLE);
        mContainingActivity.getmToolbar().setVisibility(View.VISIBLE);
        Menu menu = mContainingActivity.getmToolbar().getMenu();
        for(int i = 0; i<menu.size();++i) {
            menu.getItem(i).setVisible(false);
        }
    }

    // Template code
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mViewBinding = null;
    }
}
package com.example.wemarketandroid.viewmodels.shipper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Market;
import com.example.wemarketandroid.models.OrderDetail;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MapsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Marker>> orderMarkersLiveData;
    private MutableLiveData<Marker> shipperMarkerLiveData;

    public MapsViewModel(){
        orderMarkersLiveData = new MutableLiveData<>();
        shipperMarkerLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Marker>> getOrderMarkersLiveData() {
        return orderMarkersLiveData;
    }

    public MutableLiveData<Marker> getShipperMarkerLiveData() {
        return shipperMarkerLiveData;
    }
}

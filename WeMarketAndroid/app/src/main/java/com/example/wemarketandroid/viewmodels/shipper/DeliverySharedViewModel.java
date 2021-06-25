package com.example.wemarketandroid.viewmodels.shipper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.DeliveryStatus;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.UserStatus;
import com.example.wemarketandroid.repository.Repo;

import java.util.List;

public class DeliverySharedViewModel extends ViewModel {
    private Repo mRepo;
    private LiveData<List<Delivery>> mDeliveryListLiveData;
    private MutableLiveData<Boolean> isReceived;
    private LiveData<Shipper> mShipperLiveData;
    private MutableLiveData<Delivery> mCurrentDeliveryMutableLiveData;

    public DeliverySharedViewModel(){
        mRepo = Repo.getInstance();
        mShipperLiveData = mRepo.getmShipper();
        mDeliveryListLiveData = mRepo.getDeliveryByShipper(mShipperLiveData.getValue());
        mCurrentDeliveryMutableLiveData = new MutableLiveData<>();
    }

    public Repo getmRepo() {
        return mRepo;
    }

    public MutableLiveData<Boolean> getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(MutableLiveData<Boolean> isReceived) {
        this.isReceived = isReceived;
    }

    public LiveData<List<Delivery>> getShipperDeliveryListLiveData() {
        return mDeliveryListLiveData;
    }

    public LiveData<Shipper> getmShipperLiveData() {
        return mShipperLiveData;
    }
    public boolean isShipperActive(){
        Shipper shipper = mShipperLiveData.getValue();
        return shipper.getShipperStatus()!=null && shipper.getShipperStatus().equals(UserStatus.ACTIVE.toString());
    }
    public void saveShipper(Shipper shipper, Activity activity){mRepo.saveShipper(shipper,activity);}

    public MutableLiveData<Delivery> getmCurrentDeliveryMutableLiveData() {
        return mCurrentDeliveryMutableLiveData;
    }
    public void completeDelivery(Context context){
        Delivery currentDelivery = mCurrentDeliveryMutableLiveData.getValue();
        currentDelivery.setConfirm(1);
        currentDelivery.setDelivery(DeliveryStatus.DELIVERED.toString());
        mRepo.saveDelivery(currentDelivery, context);
    }
}

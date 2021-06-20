package com.example.wemarketandroid.viewmodels.shipper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.repository.Repo;

public class DeliverySharedViewModel extends ViewModel {
    private Repo mRepo;
    private MutableLiveData<Delivery> mDeliveryMutableLiveData;
    private MutableLiveData<Boolean> isReceived;    // TODO: shows if shipper accepts or declines the delivery

    public DeliverySharedViewModel(){
        mRepo = Repo.getInstance();
        mDeliveryMutableLiveData = mRepo.getmShipperDelivery();
        if(mDeliveryMutableLiveData.getValue()==null)
            mDeliveryMutableLiveData.postValue(mDeliveryMutableLiveData.getValue());    // notifies observers
    }

    public Repo getmRepo() {
        return mRepo;
    }

    public MutableLiveData<Delivery> getmDeliveryMutableLiveData() {
        return mDeliveryMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(MutableLiveData<Boolean> isReceived) {
        this.isReceived = isReceived;
    }
}

package com.example.wemarketandroid.viewmodels.shipper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.repository.Repo;

public class HomeViewModel extends ViewModel {
    private Repo mRepo;
    private LiveData<Shipper> shipperLiveData;

    public HomeViewModel(){

        mRepo = Repo.getInstance();
        shipperLiveData = mRepo.getmShipper();
    }

    public Repo getmRepo() {
        return mRepo;
    }
    public LiveData<Shipper> getShipperLiveData(){return shipperLiveData;}
}

package com.example.wemarketandroid.viewmodels.shipper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.repository.Repo;

public class HomeViewModel extends ViewModel {
    private Repo mRepo;

    public HomeViewModel(){
        mRepo = Repo.getInstance();
    }

    public Repo getmRepo() {
        return mRepo;
    }
}

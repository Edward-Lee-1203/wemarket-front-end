package com.example.wemarketandroid.viewmodels.buyer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.Repo;

public class UserSharedViewModel extends ViewModel {

    // TODO: consider deleting this viewmodel
    private Repo mRepo;
    private LiveData<User> mUser;

    public UserSharedViewModel(){
        mRepo = Repo.getInstance();
        mUser = mRepo.getUser();
    }

    public LiveData<User> getUser(){ return mUser; }
}

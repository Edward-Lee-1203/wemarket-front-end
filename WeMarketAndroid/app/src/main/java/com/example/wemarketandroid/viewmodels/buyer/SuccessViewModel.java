package com.example.wemarketandroid.viewmodels.buyer;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wemarketandroid.models.CartItem;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.models.Order;
import com.example.wemarketandroid.models.OrderDetail;
import com.example.wemarketandroid.repository.Repo;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessViewModel extends AndroidViewModel {

    private Repo mRepo;
    private Context mContext;
    private MutableLiveData<Delivery> mDeliveryLiveData;

    public SuccessViewModel(@NonNull Application application) {
        super(application);
        mRepo = Repo.getInstance();
        mContext = application.getApplicationContext();
        mDeliveryLiveData = new MutableLiveData<>();
    }

}

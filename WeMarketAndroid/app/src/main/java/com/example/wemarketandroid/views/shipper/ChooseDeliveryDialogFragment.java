package com.example.wemarketandroid.views.shipper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wemarketandroid.R;
import com.example.wemarketandroid.models.Delivery;
import com.example.wemarketandroid.viewmodels.shipper.DeliverySharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChooseDeliveryDialogFragment extends DialogFragment {

    private DeliverySharedViewModel mSharedViewModel;
    private List<Delivery> deliveries;

    public interface ChooseDeliveryListener{
        void onChooseDelivery(Delivery delivery);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(DeliverySharedViewModel.class);
        deliveries = mSharedViewModel.getShipperDeliveryListLiveData().getValue();
        String[] deliveryIds = new String[deliveries.size()];
        for(int i = 0; i<deliveries.size(); ++i)
            deliveryIds[i] = "Delivery #"+deliveries.get(i).getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.shipper_choose_delivery_dialog_title)
                .setItems(deliveryIds, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSharedViewModel.getmCurrentDeliveryMutableLiveData().postValue(deliveries.get(i));
                        ChooseDeliveryListener listener = (ChooseDeliveryListener)getTargetFragment();
                        listener.onChooseDelivery(deliveries.get(i));
                        dismiss();  // closes dialog
                    }
                });
        return builder.create();
    }
}

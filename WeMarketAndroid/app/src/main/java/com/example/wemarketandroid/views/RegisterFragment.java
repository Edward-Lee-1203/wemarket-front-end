package com.example.wemarketandroid.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.wemarketandroid.AuthActivity;
import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentLoginBinding;
import com.example.wemarketandroid.databinding.FragmentRegisterBinding;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.Repo;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding mViewBinding;
    private AuthActivity mAuthActivity;
    private Repo mRepo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentRegisterBinding.inflate(inflater,container,false);
        View root = mViewBinding.getRoot();
        mRepo = Repo.getInstance();
        mAuthActivity = (AuthActivity)requireActivity();
        // binding
        mViewBinding.buttonRegisterSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mViewBinding.editTextRegisterUsername.getText().toString();
                String password = mViewBinding.editTextRegisterPassword.getText().toString();
                String phoneNumber= mViewBinding.editTextRegisterPhoneNumber.getText().toString();
                String email = mViewBinding.editTextRegisterEmail.getText().toString();
                boolean isBuyerRegister = mViewBinding.radioButtonRegisterBuyer.isChecked();
                ArrayList<String> roles = new ArrayList<>();
                if(isBuyerRegister){
                    roles.add("ROLE_USER");
                    User user = new User(null, username, password, phoneNumber, phoneNumber,null,null,0,null,roles);
                    mRepo.saveUser(user,getActivity());
                    mRepo.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if(user!=null){
                                Toast.makeText(getContext(),"User registered",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    roles.add("ROLE_SHIPPER");
                    Shipper shipper = new Shipper(null,phoneNumber, phoneNumber, username, null, null, password, null,roles);
                    mRepo.saveShipper(shipper,getActivity());
                    mRepo.getmShipper().observe(getViewLifecycleOwner(), new Observer<Shipper>() {
                        @Override
                        public void onChanged(Shipper shipper) {
                            if(shipper!=null){
                                Toast.makeText(getContext(),"Shipper registered",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return root;
    }
}
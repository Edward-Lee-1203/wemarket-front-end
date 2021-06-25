package com.example.wemarketandroid.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wemarketandroid.AuthActivity;
import com.example.wemarketandroid.R;
import com.example.wemarketandroid.databinding.FragmentLoginBinding;
import com.example.wemarketandroid.models.Shipper;
import com.example.wemarketandroid.models.User;
import com.example.wemarketandroid.repository.Repo;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding mViewBinding;
    private AuthActivity mAuthActivity;
    private Repo mRepo;
    private Handler mainLooperHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = FragmentLoginBinding.inflate(inflater,container,false);
        View root = mViewBinding.getRoot();
        mRepo = Repo.getInstance();
        mAuthActivity = (AuthActivity)requireActivity();
        mainLooperHandler = new Handler(Looper.getMainLooper());
        mViewBinding.buttonLoginSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mViewBinding.editTextLoginUsername.getText().toString().trim();
                String password = mViewBinding.editTextLoginPassword.getText().toString().trim();
                boolean isBuyer = mViewBinding.radioButtonBuyer.isChecked();
                if(isBuyer){
                    mRepo.loginBuyer(username,password,mainLooperHandler,mAuthActivity).observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if(user!=null){
                                Intent intent = new Intent(getContext(), com.example.wemarketandroid.views.buyer.MainActivity.class);
                                startActivity(intent);
                            } else{
                                // TODO: implements TextInputLayout and display error message there
                                Toast.makeText(getContext(),"Login failed. Please retry!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{

                    mRepo.loginShipper(username,password,mAuthActivity).observe(getViewLifecycleOwner(), new Observer<Shipper>() {
                        @Override
                        public void onChanged(Shipper user) {
                            if(user!=null){
                                Intent intent = new Intent(getContext(), com.example.wemarketandroid.views.shipper.MainActivity.class);
                                startActivity(intent);
                            } else{
                                // TODO: implements TextInputLayout and display error message there
                                Toast.makeText(getContext(),"Login failed. Please retry!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        // binds reigster button
        mViewBinding.buttonLoginCreateAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthActivity.getNavController().navigate(R.id.destination_register);
            }
        });

        return root;
    }


}
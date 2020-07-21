package com.example.scheduling_activity.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduling_activity.LoginActivity;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.SessionManager;
import com.example.scheduling_activity.ui.manager.ManagerViewModel;
import com.google.firebase.auth.FirebaseAuth;

class LogoutFragment extends Fragment {

    private LogoutViewModel managerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        managerViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        managerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth.getInstance().signOut();
        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.redirectOnLogout(LoginActivity.class);
    }
}

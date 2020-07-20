package com.example.scheduling_activity.ui.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.detail.DetailKegiatan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManagerFragment extends Fragment {

    private ManagerViewModel managerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        managerViewModel =
                ViewModelProviders.of(this).get(ManagerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_manager, container, false);

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


        FloatingActionButton fab = view.findViewById(R.id.assign);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CreateAssignActivity.class);
                startActivity(i);
            }
        });

    }
}

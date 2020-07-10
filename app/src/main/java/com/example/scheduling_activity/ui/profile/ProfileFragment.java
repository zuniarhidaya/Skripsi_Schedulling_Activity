package com.example.scheduling_activity.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.database.AppExecutors;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private ImageView profil;
    private TextView nama;
    private TextView jabatan;
    private TextView pegawai;
    private Button ubah;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance){
        super.onViewCreated(view, savedInstance);
        profil = (ImageView) view.findViewById(R.id.fotoProfile);
        nama = (TextView) view.findViewById(R.id.namaProfil);
        jabatan = (TextView) view.findViewById(R.id.jabatanProfil);
        pegawai = (TextView) view.findViewById(R.id.pegawaiProfil);
        ubah = (Button) view.findViewById(R.id.ubahProfil);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}



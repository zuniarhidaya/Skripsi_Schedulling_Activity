package com.example.scheduling_activity.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduling_activity.R;
import com.example.scheduling_activity.SessionManager;
import com.example.scheduling_activity.ui.database.AppExecutors;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private ImageView profil;
    private TextView nama;
    private TextView jabatan;
    private TextView pegawai;
    private Button ubah;

    SessionManager session;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        profil = (ImageView) view.findViewById(R.id.fotoProfile);
        nama = (TextView) view.findViewById(R.id.namaProfil);
        jabatan = (TextView) view.findViewById(R.id.jabatanProfil);
        pegawai = (TextView) view.findViewById(R.id.pegawaiProfil);
        //ubah = (Button) view.findViewById(R.id.ubahProfil);

        session = new SessionManager(getContext());
        setData();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
//        ubah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });
    }

    private void setData() {
        nama.setText(session.getName());
        jabatan.setText(session.getJabatan());
        pegawai.setText(session.getNip());
    }


    private void showDialog() {
        LayoutInflater factory = LayoutInflater.from(getContext());
        View dialogView =
                factory.inflate(R.layout.dialog_edit, null);
        AlertDialog dialogOne = new AlertDialog.Builder(getContext()).create();

        EditText edtNama = dialogView.findViewById(R.id.edtNama);
        EditText edtJabatan = dialogView.findViewById(R.id.edtJabatan);
        EditText edtNIK = dialogView.findViewById(R.id.edtNIK);
        Button btnEdit = dialogView.findViewById(R.id.btn_edit);

        edtNama.setText(session.getName());
        edtJabatan.setText(session.getJabatan());
        edtNIK.setText(session.getNip());

        btnEdit.setOnClickListener(v -> {
            String nama = edtNama.getText().toString();
            String jabatan = edtJabatan.getText().toString();
            String nik = edtNIK.getText().toString();

            session.setJabatan(jabatan);
            session.setName(nama);
            session.setNip(nik);

            dialogOne.dismiss();
            setData();

        });

        dialogOne.setCancelable(true);
        dialogOne.setView(dialogView);
        dialogOne.show();


    }
}



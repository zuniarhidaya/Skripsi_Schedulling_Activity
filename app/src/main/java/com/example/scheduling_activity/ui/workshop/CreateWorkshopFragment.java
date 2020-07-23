package com.example.scheduling_activity.ui.workshop;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduling_activity.Bobot;
import com.example.scheduling_activity.MainActivity;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaModel;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.manager.CreatePengajuanActivity;
import com.example.scheduling_activity.ui.manager.ManagerViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class CreateWorkshopFragment extends Fragment {

    private static final String TAG = "CreateWorkshopActivity";

    private CreateWorkshopViewModel viewModel;
    private EditText editNama;
    private EditText editCalendar;
    private EditText editMulai;
    private EditText editSelesai;
    private Spinner spinnerAgenda;
    private Spinner spinnerJarak;
    private Spinner spinnerStatus;
    private Spinner spinnerAbsensi;
    private Button button1;

    private String jarak;
    private String agenda;
    private String jabatan;
    private String status;
    private String absensi;
    private String tanggal;
    private String waktu;
    private String waktuAkhir;

    private CheckBox checkBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(CreateWorkshopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workshop, container, false);

        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editNama = (EditText) view.findViewById(R.id.editNama);
        editCalendar = (EditText) view.findViewById(R.id.editCalendar);
        editMulai = (EditText) view.findViewById(R.id.editMulai);
        editSelesai = (EditText) view.findViewById(R.id.editAkhir);
        spinnerJarak = (Spinner) view.findViewById(R.id.spinnerJarak);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        spinnerAbsensi = (Spinner) view.findViewById(R.id.spinnerAbsensi);
        spinnerAgenda = (Spinner) view.findViewById(R.id.spinnerAgenda);
        button1 = (Button) view.findViewById(R.id.btnSimpan);
        checkBox = (CheckBox) view.findViewById(R.id.cb_set_reminder);

        status();
        jarak();
        absensi();
        agenda();

        editCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        editMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeStart();
            }
        });


        editSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeEnd();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editNama.getText().toString().trim().isEmpty() &&
                        !editCalendar.getText().toString().trim().isEmpty() &&
                        jarak != null &&
                        status != null &&
                        absensi != null) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                            AgendaTable agen = new AgendaTable();
                            List<AgendaTable> list = db.agendaDao().getAgendaList();

                            agen.setName(editNama.getText().toString());
                            agen.setTanggal(editCalendar.getText().toString());
                            agen.setJarak(jarak);
                            agen.setStatus(status);
                            agen.setMeeting(agenda);
                            agen.setJabatan("Manajer");
                            agen.setAbsensi(absensi);
                            agen.setKaryawan(false);

                            if (checkBox.isChecked()) {
                                agen.setReminder(true);
                            } else {
                                agen.setTime(0L);
                                agen.setReminder(false);
                            }

                            db.agendaDao().insertAgenda(agen);


                            AgendaModel agendaModel = new AgendaModel();
                            agendaModel.setJabatan("Manajer");
                            agendaModel.setName(editNama.getText().toString());
                            agendaModel.setTanggal(editCalendar.getText().toString());
                            agendaModel.setJarak(jarak);
                            agendaModel.setMeeting(agenda);
                            agendaModel.setStatus(status);
                            agendaModel.setAbsensi(absensi);
                            agendaModel.setKaryawan(false);
                            addDataAgendaToFirestore(agendaModel);

                            getActivity().runOnUiThread(() -> afterInsert());


                            for (int i = 0; i < list.size(); i++) {
                                Log.e("Data", list.get(i).getName() + ", " + list.get(i).getTanggal() + ", " + list.get(i).getMeeting() + ", " + list.get(i).getJabatan() + ", " + list.get(i).getJarak() + ", " + list.get(i).getStatus() + ", " + list.get(i).getAbsensi());
                            }
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void afterInsert(){
        editNama.setText("");
        editCalendar.setText("");
        jarak = null;
        agenda = null;
        status = null;
        absensi = null;

    }

    private void addDataAgendaToFirestore(AgendaModel agendaTable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d(TAG, "onSuccess: " + agendaTable.getTanggal());
        db.collection("agenda")
                .document(agendaTable.getId())
                .set(agendaTable)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: berhasil tambah agenda");
                        Toast.makeText(getContext(), "Berhasil membuat Workshop", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }



    private void agenda() {
        ArrayAdapter<String> dataAdapterAgenda = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Bobot.workshop);
        dataAdapterAgenda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgenda.setAdapter(dataAdapterAgenda);
        spinnerAgenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agenda = Bobot.workshop[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void status() {
        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Bobot.status);
        dataAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(dataAdapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = Bobot.status[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void jarak() {
        ArrayAdapter<String> dataAdapterJarak = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Bobot.jarak);
        dataAdapterJarak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJarak.setAdapter(dataAdapterJarak);
        spinnerJarak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jarak = Bobot.jarak[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void absensi() {
        ArrayAdapter<String> dataAdapterAbsensi = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Bobot.absensi);
        dataAdapterAbsensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAbsensi.setAdapter(dataAdapterAbsensi);
        spinnerAbsensi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                absensi = Bobot.absensi[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showCalendar() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                tanggal = mYear + "-" + (mMonth + 1) + "-" + mDayOfMonth;
                String tanggal2 = mDayOfMonth + "-" + (mMonth + 1) + "-" + mYear;
                editCalendar.setText(tanggal2);
            }
        };

        DatePickerDialog dp = new DatePickerDialog(getContext(), listener, year, month, day);
        dp.show();
        dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dp.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(Color.BLUE);
        dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
    }

    private void showTimeStart() {
        Calendar calendar = Calendar.getInstance();
        final int jam = calendar.get(Calendar.HOUR_OF_DAY);
        final int menit = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            waktu = hourOfDay + ":" + minute;
            editMulai.setText(waktu);
        }, jam, menit, true);

        timePickerDialog.show();
    }

    private void showTimeEnd() {
        Calendar calendar = Calendar.getInstance();
        final int jam = calendar.get(Calendar.HOUR_OF_DAY);
        final int menit = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            waktuAkhir = hourOfDay + ":" + minute;
            editSelesai.setText(waktuAkhir);
        }, jam, menit, true);

        timePickerDialog.show();
    }


}

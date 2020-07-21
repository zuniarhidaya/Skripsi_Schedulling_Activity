package com.example.scheduling_activity.ui.manager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduling_activity.Bobot;
import com.example.scheduling_activity.MainActivity;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.detail.DetailKegiatan;

import java.util.Calendar;
import java.util.List;

public class CreateAssignActivity extends AppCompatActivity {


    private EditText editNama;
    private EditText editCalendar;
    private EditText editMulai;
    private EditText editSelesai;
    private Spinner spinnerAgenda;
    private Spinner spinnerJabatan;
    private Spinner spinnerJarak;
    private Spinner spinnerStatus;
    private Spinner spinnerAbsensi;
    private Button button1;

    private String agenda;
    private String jarak;
    private String jabatan;
    private String status;
    private String absensi;
    private String tanggal;
    private String waktu;
    private String waktuAkhir;

    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assign);

        editNama = (EditText) findViewById(R.id.editNama);
        editCalendar = (EditText) findViewById(R.id.editCalendar);
        editMulai = (EditText) findViewById(R.id.editMulai);
        editSelesai = (EditText) findViewById(R.id.editAkhir);
        spinnerAgenda = (Spinner) findViewById(R.id.spinnerAgenda);
        spinnerJabatan = (Spinner) findViewById(R.id.spinnerJabatan);
        spinnerJarak = (Spinner) findViewById(R.id.spinnerJarak);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spinnerAbsensi = (Spinner) findViewById(R.id.spinnerAbsensi);
        button1 = (Button) findViewById(R.id.btnSimpan);

        agenda();
        jabatan();
        status();
        jarak();
        absensi();

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
                        agenda != null &&
                        jarak != null &&
                        jabatan != null &&
                        status != null &&
                        absensi != null) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
                            AgendaTable agen = new AgendaTable();
                            List<AgendaTable> list = db.agendaDao().getAgendaList();

                            agen.setJabatan(jabatan);
                            agen.setName(editNama.getText().toString());
                            agen.setTanggal(editCalendar.getText().toString());
                            agen.setJarak(jarak);
                            agen.setMeeting(agenda);
                            agen.setStatus(status);
                            agen.setAbsensi(absensi);
                            agen.setKaryawan(true);

                            if (checkBox.isChecked()) {
                                agen.setReminder(true);
                            } else {
                                agen.setTime(0L);
                                agen.setReminder(false);
                            }

                            db.agendaDao().insertAgenda(agen);

                            for (int i = 0; i < list.size(); i++) {
                                Log.e("Data", list.get(i).getName() + ", " + list.get(i).getTanggal() + ", " + list.get(i).getMeeting() + ", " + list.get(i).getJabatan() + ", " + list.get(i).getJarak() + ", " + list.get(i).getStatus() + ", " + list.get(i).getAbsensi());
                            }
                        }
                    });


                    Intent intent;
                    intent = new Intent(CreateAssignActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void agenda() {
        ArrayAdapter<String> dataAdapterAgenda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Bobot.meeting);
        dataAdapterAgenda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgenda.setAdapter(dataAdapterAgenda);
        spinnerAgenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agenda = Bobot.meeting[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void jabatan() {
        ArrayAdapter<String> dataAdapterJabatan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Bobot.jabatan);
        dataAdapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJabatan.setAdapter(dataAdapterJabatan);
        spinnerJabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jabatan = Bobot.jabatan[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void status() {
        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Bobot.status);
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
        ArrayAdapter<String> dataAdapterJarak = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Bobot.jarak);
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
        ArrayAdapter<String> dataAdapterAbsensi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Bobot.absensi);
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

        DatePickerDialog dp = new DatePickerDialog(this, listener, year, month, day);
        dp.show();
        dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dp.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(Color.BLUE);
        dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
    }

    private void showTimeStart() {
        Calendar calendar = Calendar.getInstance();
        final int jam = calendar.get(Calendar.HOUR_OF_DAY);
        final int menit = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            waktu = hourOfDay + ":" + minute;
            editMulai.setText(waktu);
        }, jam, menit, true);

        timePickerDialog.show();
    }

    private void showTimeEnd() {
        Calendar calendar = Calendar.getInstance();
        final int jam = calendar.get(Calendar.HOUR_OF_DAY);
        final int menit = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            waktuAkhir = hourOfDay + ":" + minute;
            editSelesai.setText(waktuAkhir);
        }, jam, menit, true);

        timePickerDialog.show();
    }

}
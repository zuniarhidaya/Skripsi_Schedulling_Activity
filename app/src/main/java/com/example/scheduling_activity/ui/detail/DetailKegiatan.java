package com.example.scheduling_activity.ui.detail;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduling_activity.MainActivity;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.alarm.service.AlarmHelper;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailKegiatan extends AppCompatActivity {

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

    private CheckBox checkBox;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_kegiatan);

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
        checkBox = (CheckBox) findViewById(R.id.cb_set_reminder);

        Agenda();
        Jabatan();
        Status();
        Jarak();
        Absensi();

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

                            db.agendaDao().insertAgenda(agen);

                            for (int i = 0; i < list.size(); i++) {
                                Log.e("Data", list.get(i).getName() + ", " + list.get(i).getTanggal() + ", " + list.get(i).getMeeting() + ", " + list.get(i).getJabatan() + ", " + list.get(i).getJarak() + ", " + list.get(i).getStatus() + ", " + list.get(i).getAbsensi());
                            }
                        }
                    });


                    if (checkBox.isChecked()) {

                        String[] date = tanggal.split("-");
                        String[] time = waktu.split(":");

                        int year = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int day = Integer.parseInt(date[2]);

                        int hour = Integer.parseInt(time[0]);
                        int minute = Integer.parseInt(time[1]);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                        String dateFormatted = format.format(calendar.getTimeInMillis());

                        try {
                            Date milDate = format.parse(dateFormatted);
                            assert milDate != null;
                            Long timeInMillis = milDate.getTime();
                            AlarmHelper.setAlarm(DetailKegiatan.this, timeInMillis, agenda);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent intent;
                    intent = new Intent(DetailKegiatan.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void Agenda() {
        final List<String> categoriesAgenda = new ArrayList<>();
        categoriesAgenda.add("Rapat Divisi");
        categoriesAgenda.add("Rapat Direksi");
        categoriesAgenda.add("Rapat Pemegang Saham");
        ArrayAdapter<String> dataAdapterAgenda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesAgenda);
        dataAdapterAgenda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgenda.setAdapter(dataAdapterAgenda);
        spinnerAgenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                agenda = categoriesAgenda.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Jabatan() {
        final List<String> categoriesJabatan = new ArrayList<>();
        categoriesJabatan.add("Direktur");
        categoriesJabatan.add("Manajer");
        categoriesJabatan.add("Supervisor/PIC");
        ArrayAdapter<String> dataAdapterJabatan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesJabatan);
        dataAdapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJabatan.setAdapter(dataAdapterJabatan);
        spinnerJabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jabatan = categoriesJabatan.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Status() {
        final List<String> categoriesStatus = new ArrayList<>();
        categoriesStatus.add("Online");
        categoriesStatus.add("Offline");
        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesStatus);
        dataAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(dataAdapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = categoriesStatus.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Jarak() {
        final List<String> categoriesJarak = new ArrayList<>();
        categoriesJarak.add("0-5 km");
        categoriesJarak.add("6-10 km");
        categoriesJarak.add("11-20 km");
        categoriesJarak.add(">20 km");
        ArrayAdapter<String> dataAdapterJarak = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesJarak);
        dataAdapterJarak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJarak.setAdapter(dataAdapterJarak);
        spinnerJarak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jarak = categoriesJarak.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Absensi() {
        final List<String> categoriesAbsensi = new ArrayList<>();
        categoriesAbsensi.add("Tetap Hadir");
        categoriesAbsensi.add("Izin setengah hari");
        categoriesAbsensi.add("Cuti");
        categoriesAbsensi.add("Tanpa Keterangan");
        ArrayAdapter<String> dataAdapterAbsensi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesAbsensi);
        dataAdapterAbsensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAbsensi.setAdapter(dataAdapterAbsensi);
        spinnerAbsensi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                absensi = categoriesAbsensi.get(position);
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
        }, jam, menit, false);

        timePickerDialog.show();
    }

    private void showTimeEnd() {
        Calendar calendar = Calendar.getInstance();
        final int jam = calendar.get(Calendar.HOUR_OF_DAY);
        final int menit = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            waktu = hourOfDay + ":" + minute;
            editSelesai.setText(waktu);
        }, jam, menit, false);

        timePickerDialog.show();
    }

}

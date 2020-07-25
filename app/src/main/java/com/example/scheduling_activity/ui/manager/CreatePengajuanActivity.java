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
import com.example.scheduling_activity.ui.alarm.service.AlarmHelper;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePengajuanActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pengajuan);

        editNama = (EditText) findViewById(R.id.editNama);
        editCalendar = (EditText) findViewById(R.id.editCalendar);
        editMulai = (EditText) findViewById(R.id.editMulai);
        editSelesai = (EditText) findViewById(R.id.editAkhir);
        spinnerJarak = (Spinner) findViewById(R.id.spinnerJarak);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spinnerAbsensi = (Spinner) findViewById(R.id.spinnerAbsensi);
        spinnerAgenda = (Spinner) findViewById(R.id.spinnerAgenda);
        button1 = (Button) findViewById(R.id.btnSimpan);
        //checkBox = (CheckBox) findViewById(R.id.cb_set_reminder);

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
                        editMulai != null &&
                        editSelesai != null &&
                        jarak != null &&
                        status != null &&
                        absensi != null) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
                            AgendaTable agen = new AgendaTable();
                            List<AgendaTable> list = db.agendaDao().getAgendaList();

                            agen.setName(editNama.getText().toString());
                            agen.setTanggal(editCalendar.getText().toString());
                            agen.setJarak(jarak);
                            agen.setAwal(waktu);
                            agen.setAkhir(waktuAkhir);
                            agen.setStatus(status);
                            agen.setMeeting(agenda);
                            agen.setJabatan("Manajer");
                            agen.setAbsensi(absensi);
                            agen.setKaryawan(false);

//                            if (checkBox.isChecked()) {
//                                setEvent();
//                                agen.setReminder(true);
//                            } else {
//                                agen.setTime(0L);
//                                agen.setReminder(false);
//                            }

                            db.agendaDao().insertAgenda(agen);

                            for (int i = 0; i < list.size(); i++) {
                                Log.e("Data", list.get(i).getName() + ", " + list.get(i).getTanggal() + ", " + list.get(i).getMeeting() + ", " + list.get(i).getJabatan() + ", " + list.get(i).getJarak() + ", " + list.get(i).getStatus() + ", " + list.get(i).getAbsensi());
                            }
                        }
                    });


                    Intent intent;
                    intent = new Intent(CreatePengajuanActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatePengajuanActivity.this, "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Long setReminder(String timeInFormatted) {

        String[] date = tanggal.split("-");
        String[] time = timeInFormatted.split(":");

        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
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
            AlarmHelper.setAlarm(CreatePengajuanActivity.this, timeInMillis, agenda);
            return timeInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private void setEvent() {
        // get calendar
/*
        Long startTime = setReminder(waktu);
        Long endTime = setReminder(waktuAkhir);

        ContentResolver cr = getContentResolver();

// event insert
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, agenda);
        values.put(CalendarContract.Events.DESCRIPTION, agenda);
        values.put(CalendarContract.Events.CALENDAR_ID, System.currentTimeMillis() / 10);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        values.put(CalendarContract.Events.EVENT_LOCATION, jabatan);
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Uri event = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// reminder insert
        ContentValues reminder = new ContentValues();
        assert event != null;
        reminder.put("event_id", Long.parseLong(Objects.requireNonNull(event.getLastPathSegment())));
        reminder.put("method", 1);
        reminder.put("minutes", 10);
        cr.insert(CalendarContract.Reminders.CONTENT_URI, reminder);*/

        Long startTime = setReminder(waktu);
        Long endTime = setReminder(waktuAkhir);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", startTime);
        intent.putExtra("allDay", false);
        //intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", endTime);
        intent.putExtra("title", editNama.getText().toString());
        startActivity(intent);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void agenda() {
        ArrayAdapter<String> dataAdapterAgenda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Bobot.workshop);
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

    private void afterInsert(){
        editNama.setText("");
        editCalendar.setText("");
        editMulai = null;
        editSelesai = null;
        jarak = null;
        agenda = null;
        status = null;
        absensi = null;
    }
}
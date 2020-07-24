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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduling_activity.Bobot;
import com.example.scheduling_activity.MainActivity;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.alarm.service.AlarmHelper;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaModel;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.register.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateAssignActivity extends AppCompatActivity {
    private static final String TAG = "CreateAssignActivity";

    private EditText editNama;
    private EditText editCalendar;
    private EditText editMulai;
    private EditText editSelesai;
    private Spinner spinnerAgenda;
    private Spinner spinnerJabatan;
    private Spinner spinnerJarak;
    private Spinner spinnerStatus;
    private Spinner spinnerAbsensi;

    private Spinner spinnerKaryawan;
    private Button button1;

    private String agenda;
    private String jarak;
    private String jabatan;
    private String status;
    private String absensi;
    private String tanggal;
    private String waktu;
    private String waktuAkhir;
    private String karyawan;
    private String idKaryawan;

    private List<UserModel> userModelList = new ArrayList<>();
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

        spinnerKaryawan = findViewById(R.id.spinnerNamaKaryawan);

        button1 = (Button) findViewById(R.id.btnSimpan);
        checkBox = (CheckBox) findViewById(R.id.cb_set_reminder);

        agenda();
        jabatan();
        status();
        jarak();
        absensi();

        getDataKaryawan();

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
                        editMulai != null &&
                        editSelesai != null &&
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
                            agen.setAwal(waktu);
                            agen.setAkhir(waktuAkhir);
                            agen.setMeeting(agenda);
                            agen.setStatus(status);
                            agen.setAbsensi(absensi);
                            agen.setKaryawan(true);

                            if (checkBox.isChecked()) {
                                setEvent();
                                agen.setReminder(true);
                            } else {
                                agen.setTime(0L);
                                agen.setReminder(false);
                            }

                            db.agendaDao().insertAgenda(agen);

                            AgendaModel agendaModel = new AgendaModel();
                            agendaModel.setJabatan(jabatan);
                            agendaModel.setName(editNama.getText().toString());
                            agendaModel.setTanggal(editCalendar.getText().toString());
                            agendaModel.setAwal(waktu);
                            agendaModel.setAkhir(waktuAkhir);
                            agendaModel.setJarak(jarak);
                            agendaModel.setMeeting(agenda);
                            agendaModel.setStatus(status);
                            agendaModel.setAbsensi(absensi);
                            agendaModel.setKaryawan(true);

                            addDataAgendaToFirestore(agendaModel);
                            addDataAgendaKaryawanToFirestore(agendaModel);


                            for (int i = 0; i < list.size(); i++) {
                                Log.e("Data", list.get(i).getName() + ", " + list.get(i).getTanggal() + ", " + list.get(i).getMeeting() + ", " + list.get(i).getJabatan() + ", " + list.get(i).getJarak() + ", " + list.get(i).getStatus() + ", " + list.get(i).getAbsensi());
                            }
                        }
                    });

                    Intent intent;
                    intent = new Intent(CreateAssignActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(CreateAssignActivity.this, "Agenda Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAssignActivity.this, "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addDataAgendaToFirestore(AgendaModel agendaTable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d(TAG, "onSuccess: " + agendaTable.getTanggal());
        db.collection("agenda")
                .document(agendaTable.getId())
                .set(agendaTable)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: berhasil tambah agenda");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void addDataAgendaKaryawanToFirestore(AgendaModel agendaTable) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d(TAG, "onSuccess: " + agendaTable.getTanggal());
        db.collection(idKaryawan)
                .document(agendaTable.getId())
                .set(agendaTable)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: berhasil tambah agenda karyawan");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document karyawan", e);
                    }
                });
    }

    private void getDataKaryawan() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get("jabatan").toString().equals("Karyawan")) {
                                UserModel user = new UserModel();

                                user.setUserID(document.getData().get("userID").toString());
                                user.setEmail(document.getData().get("email").toString());
                                user.setPassword(document.getData().get("password").toString());
                                user.setJabatan(document.getData().get("jabatan").toString());
                                user.setNip(document.getData().get("nip").toString());
                                user.setNama(document.getData().get("nama").toString());

                                userModelList.add(user);
                                Log.d(TAG, "getDataKaryawan: " + userModelList);
                            }
                        }

                                setSpinnerKaryawan(userModelList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
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
            AlarmHelper.setAlarm(CreateAssignActivity.this, timeInMillis, agenda);
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

    private void setSpinnerKaryawan(List<UserModel> userList) {
        List<String> namaKaryawanList = new ArrayList<>();
        for (UserModel user : userList){
            namaKaryawanList.add(user.getNama());
        }
        ArrayAdapter<String> dataAdapterKaryawan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaKaryawanList);
        dataAdapterKaryawan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKaryawan.setAdapter(dataAdapterKaryawan);
        spinnerKaryawan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                karyawan = userList.get(position).getNama();
                idKaryawan = userList.get(position).getUserID();
                Log.d(TAG, "onItemSelected: " + idKaryawan);
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
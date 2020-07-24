package com.example.scheduling_activity.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduling_activity.HasilKonversi;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.SessionManager;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME";
    private HomeViewModel homeViewModel;

    private String tanggal;
    private TextView hari;
    private TextView bulan;
    private TextView tanggalAgenda;
    private RecyclerView recyclerView;


    private List<CriteriaTable> label = new ArrayList<>();
    private List<AgendaTable> agendas = new ArrayList<>();
    private List<HasilKonversi> hasils = new ArrayList<>();
    private RecyclerView rv;

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CalendarView calendarView = ((CalendarView) view.findViewById(R.id.calendar));
        //bulan = (TextView) view.findViewById(R.id.bulan);
        recyclerView = view.findViewById(R.id.agendaToday);
        tanggalAgenda = (TextView) view.findViewById(R.id.tanggal);

//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(), DetailKegiatan.class);
//                startActivity(i);
//            }
//        });


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dateNow = day + "-" + month + "-" + year;

        Log.e("Date", dateNow);

        //Pilih Tanggal filter tanggal

        //Dafault hari ini (pertama kali buka)

        SessionManager sessionManager = new SessionManager(getContext());

        String jabatan = sessionManager.getJabatan();
        if (jabatan.equals("Karyawan")) {
            getDataAgendaKaryawan(dateNow);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int years, int months, int dayOfMonths) {
                    agendas.clear();
                    int monthPlusOne = months + 1;
                    String pickedDate = dayOfMonths + "-" + monthPlusOne + "-" + years;
                    getDataAgendaKaryawan(pickedDate);
                    tanggalAgenda.setText(pickedDate);
                }
            });
        } else {
            getDataAgenda(dateNow);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int years, int months, int dayOfMonths) {
                    agendas.clear();
                    int monthPlusOne = months + 1;
                    String pickedDate = dayOfMonths + "-" + monthPlusOne + "-" + years;
                    getDataAgenda(pickedDate);
                    tanggalAgenda.setText(pickedDate);
                }
            });
        }


    }

    private void getDataAgenda(String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("agenda")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (Boolean.parseBoolean(document.getData().get("karyawan").toString())) {
                                if (document.getData().get("tanggal").toString().equals(date)) {
                                    AgendaTable agenda = new AgendaTable();
                                    agenda.setName(document.getData().get("name").toString());
                                    agenda.setAbsensi(document.getData().get("absensi").toString());
                                    agenda.setMeeting(document.getData().get("meeting").toString());
                                    agenda.setJabatan(document.getData().get("jabatan").toString());
                                    agenda.setJarak(document.getData().get("jarak").toString());
                                    agenda.setStatus(document.getData().get("status").toString());
                                    agenda.setTanggal(document.getData().get("tanggal").toString());
                                    agenda.setAwal(document.getData().get("awal").toString());
                                    agenda.setAkhir(document.getData().get("akhir").toString());
                                    agenda.setTime(Long.valueOf(document.getData().get("time").toString()));
                                    agenda.setKaryawan(Boolean.parseBoolean(document.getData().get("karyawan").toString()));
                                    agenda.setReminder(Boolean.parseBoolean(document.getData().get("reminder").toString()));
                                    agendas.add(agenda);
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                        }
                        HomeAdapter homeAdapter = new HomeAdapter(getContext(), agendas);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(homeAdapter);

                        //getActivity().runOnUiThread(PengajuanFragment.this::testMobile);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void getDataAgendaKaryawan(String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (Boolean.parseBoolean(document.getData().get("karyawan").toString())) {
                                if (document.getData().get("tanggal").toString().equals(date)) {
                                    AgendaTable agenda = new AgendaTable();
                                    agenda.setName(document.getData().get("name").toString());
                                    agenda.setMeeting(document.getData().get("meeting").toString());
                                    agenda.setJabatan(document.getData().get("jabatan").toString());
                                    agenda.setAbsensi(document.getData().get("absensi").toString());
                                    agenda.setJarak(document.getData().get("jarak").toString());
                                    agenda.setStatus(document.getData().get("status").toString());
                                    agenda.setTanggal(document.getData().get("tanggal").toString());
                                    agenda.setAwal(document.getData().get("awal").toString());
                                    agenda.setAkhir(document.getData().get("akhir").toString());
                                    agenda.setTime(Long.valueOf(document.getData().get("time").toString()));
                                    agenda.setKaryawan(Boolean.parseBoolean(document.getData().get("karyawan").toString()));
                                    agenda.setReminder(Boolean.parseBoolean(document.getData().get("reminder").toString()));
                                    agendas.add(agenda);
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                        }
                        HomeAdapter homeAdapter = new HomeAdapter(getContext(), agendas);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(homeAdapter);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

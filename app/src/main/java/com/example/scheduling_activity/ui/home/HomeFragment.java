package com.example.scheduling_activity.ui.home;

import android.content.Intent;
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
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;
import com.example.scheduling_activity.ui.detail.DetailKegiatan;
import com.example.scheduling_activity.ui.manager.PengajuanFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        if(jabatan.equals("Karyawan")){
            getDataAgenda();


        }else{
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int years, int months, int dayOfMonths) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            agendas.clear();
                            int monthPlusOne = months + 1;
                            String pickedDate = dayOfMonths + "-" + monthPlusOne + "-" + years;

                            DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                            List<CriteriaTable> list = db.criteriaDao().getCriteriaList();
                            List<AgendaTable> agenda = db.agendaDao().filterDate(pickedDate);

                            label.addAll(list);
                            agendas.addAll(agenda);

                            getActivity().runOnUiThread(() -> {
                                HomeAdapter homeAdapter = new HomeAdapter(getContext(), agenda);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(homeAdapter);

                                tanggalAgenda.setText(pickedDate);
                            });
                        }
                    });
                }
            });
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                    List<CriteriaTable> list = db.criteriaDao().getCriteriaList();
                    List<AgendaTable> agenda = db.agendaDao().filterDate(dateNow);

                    label.addAll(list);
                    agendas.addAll(agenda);

                    HomeAdapter homeAdapter = new HomeAdapter(getContext(), agenda);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(homeAdapter);


                /*for (int i = 0; i < list.size(); i++) {
                    Log.e("Data Label", list.get(i).getName() + ", " + list.get(i).getValue() + ", " + list.get(i).getNilai());
                    String tanggalAgenda = agenda.get(i).getTanggal();
                    String[] dates = tanggalAgenda.split("-");
                    int year = Integer.parseInt(dates[0]);
                    int month = Integer.parseInt(dates[1]);
                    int day = Integer.parseInt(dates[2]);



                    *//*calendarView.markDate(
                            new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN))
                    );*//*
                }*/
                }
            });
        }


    }

    private void getDataAgenda() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("agenda")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (Boolean.parseBoolean(document.getData().get("karyawan").toString())) {
                                AgendaTable agenda = new AgendaTable();
                                agenda.setName(document.getData().get("name").toString());
                                agenda.setAbsensi(document.getData().get("absensi").toString());
                                agenda.setMeeting(document.getData().get("meeting").toString());
                                agenda.setJabatan(document.getData().get("jabatan").toString());
                                agenda.setJarak(document.getData().get("jarak").toString());
                                agenda.setStatus(document.getData().get("status").toString());
                                agenda.setTanggal(document.getData().get("tanggal").toString());
//                                agenda.setHari(document.getData().get("hari").toString());
                                agenda.setAwal(Integer.parseInt(document.getData().get("awal").toString()));
                                agenda.setAkhir(Integer.parseInt(document.getData().get("akhir").toString()));
                                agenda.setTime(Long.valueOf(document.getData().get("time").toString()));
                                agenda.setKaryawan(Boolean.parseBoolean(document.getData().get("karyawan").toString()));
                                agenda.setReminder(Boolean.parseBoolean(document.getData().get("reminder").toString()));
                                agendas.add(agenda);
                                Log.d(TAG, document.getId() + " => " + document.getData());
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

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

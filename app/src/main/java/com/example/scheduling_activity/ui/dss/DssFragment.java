package com.example.scheduling_activity.ui.dss;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduling_activity.Bobot;
import com.example.scheduling_activity.HasilKonversi;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.topsis.Alternative;
import com.example.scheduling_activity.topsis.Criteria;
import com.example.scheduling_activity.topsis.Topsis;
import com.example.scheduling_activity.topsis.TopsisIncompleteAlternativeDataException;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DssFragment extends Fragment {

    private DssViewModel dssViewModel;
    private EditText masukTanggal;
    private String tanggal;
    private TextView ans1;
    private TextView ans2;
    private TextView ans3;
    private TextView ans4;
    private RecyclerView recyclerView;
    private List<CriteriaTable> label = new ArrayList<>();
    private List<AgendaTable> agendas = new ArrayList<>();
    private List<HasilKonversi> hasils = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dssViewModel =
                ViewModelProviders.of(this).get(DssViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dss, container, false);

        dssViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ans1 = (TextView) view.findViewById(R.id.ans_1);
        ans2 = (TextView) view.findViewById(R.id.ans_2);
        //ans3 = (TextView) view.findViewById(R.id.ans_3);
        //ans4 = (TextView) view.findViewById(R.id.ans_4);
        recyclerView = view.findViewById(R.id.rv_hasil);
        masukTanggal = (EditText) view.findViewById(R.id.masukTanggal);
        masukTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });


    }

    public void testMobile() {

        Log.e("AGENDA SIZE", "" + agendas.size());

        for (int i = 0; i < agendas.size(); i++) {

            Log.e("TRACK", "1");

            HasilKonversi hasil = new HasilKonversi();
            AgendaTable agenda = agendas.get(i);

            for (int k = 0; k < Bobot.meeting.length; k++) {
                if (Bobot.meeting[k].equals(agenda.getMeeting())) {
                    hasil.setMeeting(Bobot.meetingCriteria[k]);
                }
            }

            for (int k = 0; k < Bobot.jabatan.length; k++) {
                if (Bobot.jabatan[k].equals(agenda.getJabatan())) {
                    hasil.setJabatan(Bobot.jabatanCriteria[k]);
                }
            }

            for (int k = 0; k < Bobot.status.length; k++) {
                if (Bobot.status[k].equals(agenda.getStatus())) {
                    hasil.setStatus(Bobot.statusCriteria[k]);
                }
            }

            for (int k = 0; k < Bobot.jarak.length; k++) {
                if (Bobot.jarak[k].equals(agenda.getJarak())) {
                    hasil.setJarak(Bobot.jarakCriteria[k]);
                }
            }

            for (int k = 0; k < Bobot.absensi.length; k++) {
                if (Bobot.absensi[k].equals(agenda.getAbsensi())) {
                    hasil.setAbsensi(Bobot.absensiCriteria[k]);
                }
            }

            hasil.setTanggal(agenda.getTanggal());
            hasil.setName(agenda.getName());
            hasils.add(hasil);
        }
        Log.e("HASIL", hasils.size()+"");
        for (int i = 0; i < hasils.size(); i++) {
            Log.e("Data Hasil", hasils.get(i).getName() + ", " +
                    hasils.get(i).getTanggal() + ", " +
                    hasils.get(i).getMeeting() + ", " +
                    hasils.get(i).getJabatan() + ", " +
                    hasils.get(i).getJarak() + ", " +
                    hasils.get(i).getStatus() + "," +
                    hasils.get(i).getAbsensi());
        }

        Log.e("JUMLAH", "" + hasils.size());
        if (hasils.size() > 2) {
            Log.e("TRACK", "3");
            Criteria criteriaJarak = new Criteria("Jarak", 3, true);
            Criteria criteriaMeeting = new Criteria("Meeting", 4);
            Criteria criteriaJabatan = new Criteria("Jabatan", 5);
            Criteria criteriaStatus = new Criteria("Status", 3);
            Criteria criteriaAbsensi = new Criteria("Absensi", 4, true);

            Topsis topsis = new Topsis();

            for (int i = 0; i < hasils.size(); i++) {
                Log.e("TRACK", i+1+"");

                Alternative agenda = new Alternative(hasils.get(i).getName());
                agenda.addCriteriaValue(criteriaJarak, hasils.get(i).getJarak());
                agenda.addCriteriaValue(criteriaMeeting, hasils.get(i).getMeeting());
                agenda.addCriteriaValue(criteriaJabatan, hasils.get(i).getJabatan());
                agenda.addCriteriaValue(criteriaStatus, hasils.get(i).getStatus());
                agenda.addCriteriaValue(criteriaAbsensi, hasils.get(i).getAbsensi());

                topsis.addAlternative(agenda);
            }

            try {
                Log.e("FINAL HASIL", "5");
                Alternative result = topsis.calculateOptimalSolution();
                ans1.setText(result.getName());
                ans2.setText("" + result.getCalculatedPerformanceScore());

                printDetailedResults(topsis);

                //assertEquals("Mobile 3", result.getName());

            } catch (TopsisIncompleteAlternativeDataException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void printDetailedResults(Topsis topsis) {


        ArrayList<Result> results = new ArrayList<>();

        for (Alternative alternative : topsis.getAlternatives()) {

            Result result = new Result();
            result.setName(alternative.getName());
            result.setScore(alternative.getCalculatedPerformanceScore()+"");
            results.add(result);

            /*ans3.setText("" + alternative.getName());
            ans4.setText("" + alternative.getCalculatedPerformanceScore());*/
        }

        DssAdapter dssAdapter = new DssAdapter(getContext(), results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dssAdapter);

    }

    public void onBackPressed() {

    }

    private void showCalendar() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                tanggal = mDayOfMonth + "-" + (mMonth + 1) + "-" + mYear;
                String tanggal2 = mDayOfMonth + "-" + (mMonth + 1) + "-" + mYear;

                masukTanggal.setText(tanggal2);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                        List<CriteriaTable> list = db.criteriaDao().getCriteriaList();
                        List<AgendaTable> agenda = db.agendaDao().filterDate(tanggal);
                        
                        label.addAll(list);
                        agendas.addAll(agenda);

                        for (int i = 0; i < agenda.size(); i++) {
                            Log.e("Data Agenda", agenda.get(i).getName() + ", " + agenda.get(i).getAbsensi() + ", " + agenda.get(i).getMeeting());
                        }

                        getActivity().runOnUiThread(DssFragment.this::testMobile);


                        onBackPressed();
                    }
                });


            }
        };

        DatePickerDialog dp = new DatePickerDialog(getContext(), listener, year, month, day);
        dp.show();
        dp.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dp.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(Color.BLUE);
        dp.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
    }
}

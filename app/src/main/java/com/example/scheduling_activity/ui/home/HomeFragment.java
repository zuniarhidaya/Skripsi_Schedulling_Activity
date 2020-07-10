package com.example.scheduling_activity.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduling_activity.HasilKonversi;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private String tanggal;
    private TextView hari;
    private TextView bulan;
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

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CalendarView calendarView = ((CalendarView) view.findViewById(R.id.calendar));
        bulan = (TextView) view.findViewById(R.id.bulan);

       /* Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);*/

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String nameMonth = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("in", "ID"));
        //bulan.setText(nameMonth);

//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
//                List<CriteriaTable> list = db.criteriaDao().getCriteriaList();
//                List<AgendaTable> agenda = db.agendaDao().getAgendaList();
//
//                label.addAll(list);
//                agendas.addAll(agenda);
//
//                for (int i = 0; i < list.size(); i++) {
//                    Log.e("Data Label", list.get(i).getName() + ", " + list.get(i).getValue() + ", " + list.get(i).getNilai());
//                    String tanggalAgenda = agenda.get(i).getTanggal();
//                    String[] dates = tanggalAgenda.split("-");
//                    int year = Integer.parseInt(dates[0]);
//                    int month = Integer.parseInt(dates[1]);
//                    int day = Integer.parseInt(dates[2]);
//
//                    calendarView.markDate(
//                            new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.GREEN))
//                    );
//                }
//            }
//        });
    }

    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

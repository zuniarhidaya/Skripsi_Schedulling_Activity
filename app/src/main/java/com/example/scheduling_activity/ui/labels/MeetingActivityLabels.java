package com.example.scheduling_activity.ui.labels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduling_activity.Bobot;
import com.example.scheduling_activity.MainActivity;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.SessionManager;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;

import java.util.ArrayList;
import java.util.List;

public class MeetingActivityLabels extends AppCompatActivity {

    private RelativeLayout relativeOne;
    private RelativeLayout relativeTwo;
    private RelativeLayout relativeThree;
    private TextView numberOne;
    private TextView numberTwo;
    private TextView numberThree;
    private TextView rapatDireksi;
    private TextView rapatDivisi;
    private TextView rapatSaham;
    private int counter = 0;
    private Button button1;
    private Button button2;
    private boolean arr[]  = new boolean[]{false, false, false};
    private List<String> criteria = new ArrayList<>();

    SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labels_meeting);

        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.isLabelling()) {
            Intent i = new Intent(MeetingActivityLabels.this, MainActivity.class);
            startActivity(i);
        }

        relativeOne = (RelativeLayout) findViewById(R.id.relativeMeeting_1);
        relativeTwo = (RelativeLayout) findViewById(R.id.relativeMeeting_2);
        relativeThree = (RelativeLayout) findViewById(R.id.relativeMeeting_3);
        numberOne = (TextView) findViewById((R.id.numberMeeting_1));
        numberTwo = (TextView) findViewById(R.id.numberMeeting_2);
        numberThree = (TextView) findViewById(R.id.numberMeeting_3);
        rapatDireksi = (TextView) findViewById(R.id.rapatDireksi);
        rapatDivisi = (TextView) findViewById(R.id.rapatDivisi);
        rapatSaham = (TextView) findViewById(R.id.rapatSaham);
        button1 = (Button) findViewById(R.id.btnResetMeeting_1);
        button2 = (Button) findViewById(R.id.btnNextMeeting_2);
        init();
    }

    private void init(){

        relativeOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!arr[0]){

                    criteria.add(rapatDireksi.getText().toString());
                    counter++;
                    numberOne.setText(String.valueOf(counter));
                    arr[0] = true;
                }
            }
        });

        relativeTwo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!arr[1]){

                    criteria.add(rapatDivisi.getText().toString());
                    counter++;
                    numberTwo.setText(String.valueOf(counter));
                    arr[1] = true;
                }
            }
        });

        relativeThree.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!arr[2]){

                    criteria.add(rapatSaham.getText().toString());
                    counter++;
                    numberThree.setText(String.valueOf(counter));
                    arr[2] = true;
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                counter = 0;
                numberOne.setText("");
                numberTwo.setText("");
                numberThree.setText("");
                arr[2] = false;
                arr[1] = false;
                arr[0] = false;
                criteria.clear();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arr[0] && arr[1] && arr[2]) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
                            for (int i = 0; i < criteria.size(); i++) {
                                String nameLabel = criteria.get(i);
                                int nilai = Bobot.meetingCriteria[i];

                                CriteriaTable crit = new CriteriaTable();
                                crit.setName("Meeting");
                                crit.setValue(nameLabel);
                                crit.setNilai(nilai);

                                db.criteriaDao().insertCriteria(crit);
                            }
                        }
                    });
                    Intent intent;
                    intent = new Intent(MeetingActivityLabels.this, JabatanActivityLabels.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Harap Lengkapi Data Anda!",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

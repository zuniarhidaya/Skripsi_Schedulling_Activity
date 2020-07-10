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

public class StatusActivityLabels extends AppCompatActivity {

    private RelativeLayout relativeOne;
    private RelativeLayout relativeTwo;
    private TextView statusOnline;
    private TextView statusOffline;
    private TextView numberOne;
    private TextView numberTwo;
    private int counter = 0;
    private Button button1;
    private Button button2;
    private boolean arr[] = new boolean[]{false, false, false};
    private List<String> criteria = new ArrayList<>();

    SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labels_status);


        sessionManager = new SessionManager(getApplicationContext());


        relativeOne = (RelativeLayout) findViewById(R.id.relativeStatus_1);
        relativeTwo = (RelativeLayout) findViewById(R.id.relativeStatus_2);
        statusOnline = (TextView) findViewById(R.id.statusOnline);
        statusOffline = (TextView) findViewById(R.id.statusOffline);
        numberOne = (TextView) findViewById((R.id.numberStatus_1));
        numberTwo = (TextView) findViewById(R.id.numberStatus_2);
        button1 = (Button) findViewById(R.id.btnResetStatus_1);
        button2 = (Button) findViewById(R.id.btnDoneStatus_2);
        init();
    }

    private void init() {

        relativeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!arr[0]) {

                    criteria.add(statusOnline.getText().toString());
                    counter++;
                    numberOne.setText(String.valueOf(counter));
                    arr[0] = true;
                }
            }
        });

        relativeTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!arr[1]) {

                    criteria.add(statusOffline.getText().toString());
                    counter++;
                    numberTwo.setText(String.valueOf(counter));
                    arr[1] = true;
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                counter = 0;
                numberOne.setText("");
                numberTwo.setText("");
                arr[1] = false;
                arr[0] = false;
                criteria.clear();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr[0] && arr[1]) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
                            for (int i = 0; i < criteria.size(); i++) {
                                String nameLabel = criteria.get(i);
                                int nilai = Bobot.statusCriteria[i];

                                CriteriaTable crit = new CriteriaTable();
                                crit.setName("Status");
                                crit.setValue(nameLabel);
                                crit.setNilai(nilai);

                                db.criteriaDao().insertCriteria(crit);
                            }
                        }
                    });
                    Intent intent;
                    sessionManager.setLabelling();
                    intent = new Intent(StatusActivityLabels.this, MainActivity.class);
                    SessionManager sessionManager = new SessionManager(StatusActivityLabels.this);
                    sessionManager.setDoneCriteria(true);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}

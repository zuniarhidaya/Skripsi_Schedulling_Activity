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
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.database.AppExecutors;
import com.example.scheduling_activity.ui.database.DatabaseHelper;
import com.example.scheduling_activity.ui.database.criteria.CriteriaTable;

import java.util.ArrayList;
import java.util.List;

public class JabatanActivityLabels extends AppCompatActivity {

    private RelativeLayout relativeOne;
    private RelativeLayout relativeTwo;
    private RelativeLayout relativeThree;
    private TextView jabatanDirektur;
    private TextView jabatanManajer;
    private TextView jabatanSupervisor;
    private TextView numberOne;
    private TextView numberTwo;
    private TextView numberThree;
    private int counter = 0;
    private Button button1;
    private Button button2;
    private boolean arr[]  = new boolean[]{false, false, false};
    private List<String> criteria = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labels_jabatan);

        relativeOne = (RelativeLayout) findViewById(R.id.relativeJabatan_1);
        relativeTwo = (RelativeLayout) findViewById(R.id.relativeJabatan_2);
        relativeThree = (RelativeLayout) findViewById(R.id.relativeJabatan_3);
        jabatanDirektur = (TextView) findViewById(R.id.jabatanDirektur);
        jabatanManajer = (TextView) findViewById(R.id.jabatanManajer);
        jabatanSupervisor = (TextView) findViewById(R.id.jabatanSupervisor);
        numberOne = (TextView) findViewById((R.id.numberJabatan_1));
        numberTwo = (TextView) findViewById(R.id.numberJabatan_2);
        numberThree = (TextView) findViewById(R.id.numberJabatan_3);
        button1 = (Button) findViewById(R.id.btnResetJabatan_1);
        button2 = (Button) findViewById(R.id.btnNextJabatan_2);
        init();
    }

    private void init(){

        relativeOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!arr[0]){

                    criteria.add(jabatanDirektur.getText().toString());
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

                    criteria.add(jabatanManajer.getText().toString());
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

                    criteria.add(jabatanSupervisor.getText().toString());
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
                                int nilai = Bobot.jabatanCriteria[i];

                                CriteriaTable crit = new CriteriaTable();
                                crit.setName("Jabatan");
                                crit.setValue(nameLabel);
                                crit.setNilai(nilai);

                                db.criteriaDao().insertCriteria(crit);
                            }
                        }
                    });
                    Intent intent;
                    intent = new Intent(JabatanActivityLabels.this, StatusActivityLabels.class);
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

    }
}

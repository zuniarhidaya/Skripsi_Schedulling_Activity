package com.example.scheduling_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduling_activity.ui.labels.MeetingActivityLabels;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private Button button1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


        final SessionManager session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(RegisterActivity.this, MeetingActivityLabels.class);
            startActivity(intent);
        }

        editText1 = (EditText) findViewById(R.id.editNama);
        editText2 = (EditText) findViewById(R.id.editEmail);
        editText3 = (EditText) findViewById(R.id.editJabatan);
        editText4 = (EditText) findViewById(R.id.editNotlpn);
        editText6 = (EditText) findViewById(R.id.editId);
        button1 = (Button) findViewById(R.id.btnSimpan);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editText1.getText().toString();
                String email = editText2.getText().toString();
                String jabatan = editText3.getText().toString();
                String telpon = editText4.getText().toString();
                String nip = editText6.getText().toString();

                if (!nama.isEmpty() && !email.isEmpty() && !jabatan.isEmpty() && !telpon.isEmpty() && !nip.isEmpty()) {

                    session.createLoginSession(nama, email, jabatan, telpon, nip);
                    Intent intent = new Intent(RegisterActivity.this, MeetingActivityLabels.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Harap Lengkapi Data Anda!", Toast.LENGTH_SHORT).show();
                    OnBackPressed();
                }
            }
        });

    }

    public void OnBackPressed() {

    }

}

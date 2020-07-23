package com.example.scheduling_activity.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduling_activity.Bobot;
import com.example.scheduling_activity.R;
import com.example.scheduling_activity.SplashScreenActivity;
import com.example.scheduling_activity.ui.login.LoginApiActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterApiActivity extends AppCompatActivity {

    private static final String TAG = "RegisterApiActivity";
    private EditText editText1;
    private EditText editText3;
    private EditText editText6;
    private Spinner spinnerJabatan;
    private EditText edtNip;
    private EditText edtEmail;
    private Button button1;

    private String jabatan;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_api);

        editText1 = (EditText) findViewById(R.id.editNama);
        editText3 = (EditText) findViewById(R.id.editJabatan);
        editText6 = (EditText) findViewById(R.id.editId);
        spinnerJabatan = (Spinner) findViewById(R.id.spinnerRegis);
        button1 = (Button) findViewById(R.id.btnSimpan);
        edtNip = findViewById(R.id.edt_nip);
        edtEmail = findViewById(R.id.edt_email);

        mAuth = FirebaseAuth.getInstance();

        setJabatan();

        button1.setOnClickListener(v -> register(edtEmail.getText().toString(),
                editText6.getText().toString(),
                jabatan,
                edtNip.getText().toString(),
                editText1.getText().toString()));

    }

    private void register(String email, String password, String jabatan, String nip, String nama) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserModel userModel =
                                    new UserModel(
                                            user.getUid(),
                                            email,
                                            password,
                                            jabatan, nip, nama);
                            addDataToFirestore(userModel);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                        }

                    }
                });
    }

    private void addDataToFirestore(UserModel user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(user.getUserID())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Intent i = new Intent(RegisterApiActivity.this, LoginApiActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void setJabatan() {
        String[] jabatans = new String[] {"Manajer", "Karyawan"};
        ArrayAdapter<String> dataAdapterJabatan = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jabatans);
        dataAdapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJabatan.setAdapter(dataAdapterJabatan);
        spinnerJabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jabatan = jabatans[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
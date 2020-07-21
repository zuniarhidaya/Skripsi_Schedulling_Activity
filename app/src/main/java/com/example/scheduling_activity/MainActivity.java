package com.example.scheduling_activity;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        SessionManager sessionManager = new SessionManager(this);

        String jabatan = sessionManager.getJabatan();

        if (jabatan.equals("Karyawan")) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_profile,
                    R.id.nav_home, R.id.nav_dss, R.id.nav_workshop, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_profile,
                    R.id.nav_home, R.id.nav_dss, R.id.nav_manager, R.id.nav_pengajuan, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
        }


        final SessionManager session = new SessionManager(getApplicationContext());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        onBackPressed();

        View headerView = navigationView.getHeaderView(0);

        TextView txtName = headerView.findViewById(R.id.txt_name_nav);
        TextView txtJabatan = headerView.findViewById(R.id.txt_jabatan_nav);

        txtName.setText(session.getName());
        txtJabatan.setText(session.getJabatan());

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_CALENDAR,
                        Manifest.permission.READ_CALENDAR
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

    }
}
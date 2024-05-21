package com.diasjoao.metrosultejo.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.fragments.MapFragment;
import com.diasjoao.metrosultejo.fragments.RouteFragment;
import com.diasjoao.metrosultejo.fragments.TimetableFragment;
import com.google.android.material.navigation.NavigationBarView;

public class TimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = switch (item.getItemId()) {
                case R.id.nav_weekday_fragment -> new TimetableFragment();
                case R.id.nav_saturday_fragment -> new TimetableFragment();
                case R.id.nav_sunday_fragment -> new TimetableFragment();
                default -> null;
            };

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();
            }

            return true;
        });
    }
}
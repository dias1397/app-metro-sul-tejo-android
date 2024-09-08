package com.diasjoao.metrosultejo.ui.schedule;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.navigation.NavigationBarView;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int dayId = 1;

            if (item.getItemId() == R.id.nav_weekday_fragment) {
                selectedFragment = new ScheduleFragment();
            }

            if (item.getItemId() == R.id.nav_saturday_fragment) {
                selectedFragment = new ScheduleFragment();
                dayId = 2;
            }

            if (item.getItemId() == R.id.nav_sunday_fragment) {
                selectedFragment = new ScheduleFragment();
                dayId = 3;
            }

            if (selectedFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("dayId", dayId);

                selectedFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        navigationBarView.setSelectedItemId(R.id.nav_weekday_fragment);
    }
}
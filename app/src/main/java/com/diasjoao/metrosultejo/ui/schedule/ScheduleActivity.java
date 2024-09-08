package com.diasjoao.metrosultejo.ui.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Arrays;

public class ScheduleActivity extends AppCompatActivity {

    private int dayId, lineId = 1;

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

        Spinner lineSpinner = findViewById(R.id.spinner_line);

        String line1 = getResources().getString(R.string.line_1_name);
        String line2 = getResources().getString(R.string.line_2_name);
        String line3 = getResources().getString(R.string.line_3_name);

        ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(line1, line2, line3));
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineSpinner.setAdapter(lineAdapter);

        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lineId = position + 1;

                updateFragment(dayId, lineId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_weekday_fragment) {
                dayId = 1;
            }

            if (item.getItemId() == R.id.nav_saturday_fragment) {
                dayId = 2;
            }

            if (item.getItemId() == R.id.nav_sunday_fragment) {
                dayId = 3;
            }

            updateFragment(dayId, lineId);

            return true;
        });

        navigationBarView.setSelectedItemId(R.id.nav_weekday_fragment);
    }

    private void updateFragment(int dayId, int lineId) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("dayId", dayId);
        bundle.putInt("lineId", lineId);

        scheduleFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, scheduleFragment)
                .commit();
    }
}
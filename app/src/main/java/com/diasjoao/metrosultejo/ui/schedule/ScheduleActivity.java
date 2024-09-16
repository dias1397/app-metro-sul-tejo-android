package com.diasjoao.metrosultejo.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScheduleActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private Handler handler;

    private int dayId, lineId, seasonId = 1;

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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        Intent intent = getIntent();
        seasonId = intent.getIntExtra("seasonId", 1);

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        Spinner lineSpinner = findViewById(R.id.spinner_line);
        SwitchMaterial seasonSwitch = findViewById(R.id.switch_season);
        seasonSwitch.setChecked(seasonId == 2);

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        String line11 = getResources().getString(R.string.line11_name);
        String line12 = getResources().getString(R.string.line12_name);
        String line21 = getResources().getString(R.string.line21_name);
        String line22 = getResources().getString(R.string.line22_name);
        String line31 = getResources().getString(R.string.line31_name);
        String line32 = getResources().getString(R.string.line32_name);

        ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(line11, line12, line21, line22, line31, line32));
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineSpinner.setAdapter(lineAdapter);

        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);

                executorService.execute(() -> {
                    lineId = position + 1;
                    updateFragment(seasonId, dayId, lineId);

                    handler.post(() -> progressBar.setVisibility(View.GONE));
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        seasonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                progressBar.setVisibility(View.VISIBLE);

                executorService.execute(() -> {
                    seasonId = isChecked ? 2 : 1;
                    updateFragment(seasonId, dayId, lineId);

                    handler.post(() -> progressBar.setVisibility(View.GONE));
                });
            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            progressBar.setVisibility(View.VISIBLE);

            executorService.execute(() -> {
                if (item.getItemId() == R.id.nav_weekday_fragment) {
                    dayId = 1;
                }

                if (item.getItemId() == R.id.nav_saturday_fragment) {
                    dayId = 2;
                }

                if (item.getItemId() == R.id.nav_sunday_fragment) {
                    dayId = 3;
                }

                updateFragment(seasonId, dayId, lineId);

                handler.post(() -> progressBar.setVisibility(View.GONE));
            });

            return true;
        });

        navigationBarView.setSelectedItemId(R.id.nav_weekday_fragment);
    }

    private void updateFragment(int seasonId, int dayId, int lineId) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("seasonId", seasonId);
        bundle.putInt("dayId", dayId);
        bundle.putInt("lineId", lineId);

        scheduleFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, scheduleFragment)
                .commit();
    }
}
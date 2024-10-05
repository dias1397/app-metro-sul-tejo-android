package com.diasjoao.metrosultejo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.fragment.ScheduleFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Arrays;
import java.util.concurrent.Executors;

public class ScheduleActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private ProgressBar progressBar;
    private Spinner lineSpinner;
    private SwitchMaterial seasonSwitchMaterial;
    private NavigationBarView navigationBarView;
    private AdView adBannerView;

    private int dayId, lineId, seasonId = 1;
    private ArrayAdapter<String> lineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initVars();
        initViews();

        setupUI();
        setupListeners();
        setupAds();
    }

    private void initVars() {
        seasonId = getIntent().getIntExtra("seasonId", 1);

        lineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList(
                        getResources().getString(R.string.line_11_name), getResources().getString(R.string.line_12_name),
                        getResources().getString(R.string.line_21_name), getResources().getString(R.string.line_22_name),
                        getResources().getString(R.string.line_31_name), getResources().getString(R.string.line_32_name)
                )
        );
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);

        progressBar = findViewById(R.id.progress_bar);
        lineSpinner = findViewById(R.id.spinner_line);
        seasonSwitchMaterial = findViewById(R.id.switch_season);
        navigationBarView = findViewById(R.id.bottom_navigation);

        adBannerView = findViewById(R.id.adView);
    }

    private void setupUI() {
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        setSupportActionBar(materialToolbar);
        materialToolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        lineSpinner.setAdapter(lineAdapter);
        seasonSwitchMaterial.setChecked(seasonId == 2);
    }

    private void setupListeners() {
        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);

                Executors.newSingleThreadExecutor().execute(() -> {
                    lineId = position + 1;
                    updateFragment(seasonId, dayId, lineId);

                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        seasonSwitchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            progressBar.setVisibility(View.VISIBLE);

            Executors.newSingleThreadExecutor().execute(() -> {
                seasonId = isChecked ? 2 : 1;
                updateFragment(seasonId, dayId, lineId);

                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            });
        });

        navigationBarView.setOnItemSelectedListener(item -> {
            progressBar.setVisibility(View.VISIBLE);

            Executors.newSingleThreadExecutor().execute(() -> {
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

                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            });

            return true;
        });
        navigationBarView.setSelectedItemId(R.id.nav_weekday_fragment);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
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
package com.diasjoao.metrosultejo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;

    private static final String PREFS_NAME = "settings";
    private static final String THEME_KEY = "theme_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup themeGroup = findViewById(R.id.radioGroupTheme);
        RadioButton lightMode = findViewById(R.id.radioLight);
        RadioButton darkMode = findViewById(R.id.radioDark);
        RadioButton systemMode = findViewById(R.id.radioSystem);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedTheme = prefs.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        if (savedTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            lightMode.setChecked(true);
        } else if (savedTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            darkMode.setChecked(true);
        } else {
            systemMode.setChecked(true);
        }

        themeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedMode;
            if (checkedId == R.id.radioLight) {
                selectedMode = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.radioDark) {
                selectedMode = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                selectedMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }

            AppCompatDelegate.setDefaultNightMode(selectedMode);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(THEME_KEY, selectedMode);
            editor.apply();
        });


        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);
        materialToolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}
package com.diasjoao.metrosultejo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SettingsActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private MaterialTextView themeTextview;
    private MaterialTextView languageTextview;

    private SharedPreferences prefs;
    private String selected_language;
    private String selected_theme;

    private static final String[] LANGUAGES = new String[]{"Português"};
    private static final String[] THEMES = new String[]{
            "Claro", "Escuro", "Sistema"
    };

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

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);
        materialToolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        languageTextview = findViewById(R.id.languageTextview);
        themeTextview = findViewById(R.id.themeTextview);

        languageTextview.setText(String.format("%s >", LANGUAGES[0]
        ));

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedTheme = prefs.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        int themeIndex = Arrays.asList(
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        ).indexOf(savedTheme);

        themeTextview.setText(String.format("%s >", THEMES[themeIndex]));
    }

    public void clickAction(View view) {
        int id = view.getId();

        if (id == R.id.languageLayout) {
            showLanguageDialog();
        }
        if (id == R.id.themeLayout) {
            showThemeDialog();
        }
    }

    private void showLanguageDialog() {
        selected_language = LANGUAGES[0];

        new AlertDialog.Builder(this)
                .setTitle("Idioma")
                .setSingleChoiceItems(LANGUAGES, 0, (dialog, i) -> selected_language = LANGUAGES[i])
                .setPositiveButton("Confirmar", null)
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showThemeDialog() {
        int[] themeModes = new int[]{
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        };

        int savedTheme = prefs.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        int selectedItem = IntStream.range(0, themeModes.length)
                .filter(i -> themeModes[i] == savedTheme)
                .findFirst()
                .orElse(2);

        selected_theme = THEMES[selectedItem];

        new AlertDialog.Builder(this)
                .setTitle("Tema")
                .setSingleChoiceItems(THEMES, selectedItem, (dialog, i) -> selected_theme = THEMES[i])
                .setPositiveButton("Confirmar", (dialog, i) -> {
                    int selectedMode = themeModes[Arrays.asList(THEMES).indexOf(selected_theme)];
                    AppCompatDelegate.setDefaultNightMode(selectedMode);

                    prefs.edit().putInt(THEME_KEY, selectedMode).apply();
                    recreate();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
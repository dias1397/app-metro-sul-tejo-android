package com.diasjoao.metrosultejo.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SettingsActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private MaterialTextView themeTextview;
    private MaterialTextView languageTextview;

    private LinearLayout rateLayout;
    private LinearLayout shareLayout;
    private LinearLayout emailLayout;

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

        languageTextview.setText(LANGUAGES[0]);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedTheme = prefs.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        int themeIndex = Arrays.asList(
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        ).indexOf(savedTheme);

        themeTextview.setText(THEMES[themeIndex]);

        rateLayout = findViewById(R.id.rateLayout);
        rateLayout.setOnClickListener(view -> openPlayStoreForRating());

        shareLayout = findViewById(R.id.shareLayout);
        shareLayout.setOnClickListener(view -> shareApp());

        emailLayout = findViewById(R.id.emailLayout);
        emailLayout.setOnClickListener(view -> sendEmail());
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

    private void openPlayStoreForRating() {
        String packageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    private void shareApp() {
        String packageName = getPackageName();
        String shareMessage = "Descubra esta aplicação incrível! 🚀\n\n" +
                "Descarregue agora na Play Store:\n" +
                "https://play.google.com/store/apps/details?id=" + packageName;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        startActivity(Intent.createChooser(shareIntent, "Partilhar aplicação via"));
    }

    private void sendEmail() {
        String recipient = "mts.appsuporte@gmail.com";
        String subject = "Feedback sobre a aplicação";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recipient));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email via"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "Nenhuma aplicação de email encontrada", Toast.LENGTH_SHORT).show();
        }
    }
}
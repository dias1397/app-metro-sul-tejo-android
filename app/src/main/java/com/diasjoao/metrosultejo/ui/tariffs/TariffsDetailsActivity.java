package com.diasjoao.metrosultejo.ui.tariffs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TariffsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tariffs_details);

        MobileAds.initialize(this, initializationStatus -> {});

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int tarifId = intent.getIntExtra("TARIFF_ID", 0);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        TextView title = findViewById(R.id.title);
        TextView price = findViewById(R.id.price);

        TextView description = findViewById(R.id.descriptionText);
        TextView definitionTitle = findViewById(R.id.definitionTitle);
        TextView definitionText = findViewById(R.id.definitionText);
        TextView validadeTitle = findViewById(R.id.validadeTitle);
        TextView validadeText = findViewById(R.id.validadeText);
        TextView aquisicaoTitle = findViewById(R.id.aquisicaoTitle);
        TextView aquisicaoText = findViewById(R.id.aquisicaoText);
        TextView suporteTitle = findViewById(R.id.suporteTitle);
        TextView suporteText = findViewById(R.id.suporteText);

        String json = loadJSONFromAsset(this, "tariffs.json");
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray tarifarios = jsonObject.getJSONArray("tarifarios");
                JSONObject tarifario = tarifarios.getJSONObject(tarifId);

                title.setText(tarifario.getString("title"));
                price.setText("Preço: " + tarifario.getString("price"));

                if (tarifario.has("descrição")) {
                    description.setText(tarifario.getString("descrição"));
                    description.setVisibility(View.VISIBLE);
                }

                if (tarifario.has("definição")) {
                    definitionTitle.setText("Definição");
                    definitionTitle.setVisibility(View.VISIBLE);
                    definitionText.setText(tarifario.getString("definição"));
                    definitionText.setVisibility(View.VISIBLE);
                }

                if (tarifario.has("validade")) {
                    validadeTitle.setText("Validade");
                    validadeTitle.setVisibility(View.VISIBLE);
                    validadeText.setText(tarifario.getString("validade"));
                    validadeText.setVisibility(View.VISIBLE);
                }

                if (tarifario.has("aquisição")) {
                    aquisicaoTitle.setText("Aquisição");
                    aquisicaoTitle.setVisibility(View.VISIBLE);
                    aquisicaoText.setText(tarifario.getString("aquisição"));
                    aquisicaoText.setVisibility(View.VISIBLE);
                }

                if (tarifario.has("suporte")) {
                    suporteTitle.setText("Suporte");
                    suporteTitle.setVisibility(View.VISIBLE);
                    suporteText.setText(tarifario.getString("suporte"));
                    suporteText.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
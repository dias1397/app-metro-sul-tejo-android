package com.diasjoao.metrosultejo.ui.tariffs;

import android.content.Context;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class TariffsDetailsActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView titleTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private TextView definitionTitleTextView;
    private TextView definitionTextView;
    private TextView expirationTitleTextView;
    private TextView expirationTextView;
    private TextView acquisitionTitleTextView;
    private TextView acquisitionTextView;
    private TextView supportTitleTextView;
    private TextView supportTextView;
    private AdView adBannerView;

    private JSONObject tariff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariffs_details);

        initVars();
        initViews();

        setupUI();
        setupAds();
    }

    private void initVars() {
        int tariffId = getIntent().getIntExtra("TARIFF_ID", 0);

        String json = loadJSONFromAsset(this, "tariffs.json");
        if (json != null) {
            try {
                tariff = new JSONObject(json)
                        .getJSONArray("tarifarios")
                        .getJSONObject(tariffId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);

        titleTextView = findViewById(R.id.title);
        priceTextView = findViewById(R.id.price);
        descriptionTextView = findViewById(R.id.descriptionText);
        definitionTitleTextView = findViewById(R.id.definitionTitle);
        definitionTextView = findViewById(R.id.definitionText);
        expirationTitleTextView = findViewById(R.id.validadeTitle);
        expirationTextView = findViewById(R.id.validadeText);
        acquisitionTitleTextView = findViewById(R.id.aquisicaoTitle);
        acquisitionTextView = findViewById(R.id.aquisicaoText);
        supportTitleTextView = findViewById(R.id.suporteTitle);
        supportTextView = findViewById(R.id.suporteText);

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
        materialToolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        if (tariff != null) {
            try {
                titleTextView.setText(tariff.getString("title"));
                priceTextView.setText("Preço: " + tariff.getString("price"));

                setTextViewVisibility(tariff, "descrição", descriptionTextView);
                setTextViewWithTitleVisibility(tariff, "definição", "Definição", definitionTitleTextView, definitionTextView);
                setTextViewWithTitleVisibility(tariff, "validade", "Validade", expirationTitleTextView, expirationTextView);
                setTextViewWithTitleVisibility(tariff, "aquisição", "Aquisição", acquisitionTitleTextView, acquisitionTextView);
                setTextViewWithTitleVisibility(tariff, "suporte", "Suporte", supportTitleTextView, supportTextView);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void setTextViewVisibility(JSONObject tariff, String key, TextView textView) throws JSONException {
        if (tariff.has(key)) {
            textView.setText(tariff.getString(key));
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void setTextViewWithTitleVisibility(JSONObject tariff, String key, String title, TextView titleView, TextView contentView) throws JSONException {
        if (tariff.has(key)) {
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
            contentView.setText(tariff.getString(key));
            contentView.setVisibility(View.VISIBLE);
        }
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
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
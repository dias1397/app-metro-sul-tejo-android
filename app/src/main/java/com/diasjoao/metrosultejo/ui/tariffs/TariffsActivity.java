package com.diasjoao.metrosultejo.ui.tariffs;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.model.Tariff;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class TariffsActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private RecyclerView recyclerView;
    private AdView adBannerView;

    private TariffRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariffs);

        initVars();
        initViews();

        setupUI();
        setupAds();
    }

    private void initVars() {
        String[] tariffsNames = getResources().getStringArray(R.array.tariff_names);
        String[] tariffsPrices = getResources().getStringArray(R.array.tariff_prices);

        List<Tariff> tariffList = new ArrayList<>();
        for (int i = 0; i < tariffsNames.length; i++) {
            tariffList.add(new Tariff(tariffsNames[i], tariffsPrices[i]));
        }

        adapter = new TariffRecyclerViewAdapter(tariffList, this);
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        adBannerView = findViewById(R.id.adView);
    }

    private void setupUI() {
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(materialToolbar);
        materialToolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
    }
}
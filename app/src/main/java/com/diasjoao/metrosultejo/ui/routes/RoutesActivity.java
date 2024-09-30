package com.diasjoao.metrosultejo.ui.routes;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.diasjoao.metrosultejo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class RoutesActivity extends AppCompatActivity {

    private ViewPager view_pager;
    private TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routes);

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

        initToolbar();
        initComponent();

        Intent intent = getIntent();
        int lineId = intent.getIntExtra("lineId", 0);

        for (int i = 0; i < tab_layout.getTabCount(); i++) {
            Objects.requireNonNull(tab_layout.getTabAt(i)).setIcon(R.drawable.baseline_bus_24);
        }

        view_pager.setCurrentItem(lineId + 1);
        view_pager.setCurrentItem(lineId);
    }

    private void initToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));
    }


    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        RoutesPagerAdapter adapter = new RoutesPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(RoutesFragment.newInstance("LINHA 1"), "LINHA 1");
        adapter.addFragment(RoutesFragment.newInstance("LINHA 2"), "LINHA 2");
        adapter.addFragment(RoutesFragment.newInstance("LINHA 3"), "LINHA 3");
        viewPager.setAdapter(adapter);
    }
}
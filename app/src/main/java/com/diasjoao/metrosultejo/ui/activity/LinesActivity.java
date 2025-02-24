package com.diasjoao.metrosultejo.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.fragment.LineFragment;
import com.diasjoao.metrosultejo.ui.adapter.LinePageAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class LinesActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AdView adBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);

        initViews();

        setupUI();
        setupAds();
        setupViewPager();
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
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

        LinePageAdapter linePageAdapter = new LinePageAdapter(getSupportFragmentManager());
        linePageAdapter.addFragment(LineFragment.newInstance("LINHA 1"), "LINHA 1");
        linePageAdapter.addFragment(LineFragment.newInstance("LINHA 2"), "LINHA 2");
        linePageAdapter.addFragment(LineFragment.newInstance("LINHA 3"), "LINHA 3");

        viewPager.setAdapter(linePageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> { });

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
    }

    private void setupViewPager() {
        int lineId = getIntent().getIntExtra("lineId", 0);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(R.drawable.ic_bus);
        }

        viewPager.setCurrentItem(lineId);
    }
}
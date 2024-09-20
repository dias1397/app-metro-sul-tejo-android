package com.diasjoao.metrosultejo.ui.routes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.diasjoao.metrosultejo.R;
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
            Objects.requireNonNull(tab_layout.getTabAt(i)).setIcon(R.drawable.baseline_directions_bus_24);
        }

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("================================>");

                switch (position) {
                    case 0:
                        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.linha1, null));
                        tab_layout.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.linha1));
                        break;
                    case 1:
                        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.linha2, null));
                        tab_layout.getTabAt(1).getIcon().setTint(getResources().getColor(R.color.linha2));
                        break;
                    case 2:
                        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.linha3, null));
                        tab_layout.getTabAt(2).getIcon().setTint(getResources().getColor(R.color.linha3));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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
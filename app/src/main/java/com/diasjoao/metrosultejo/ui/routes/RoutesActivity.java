package com.diasjoao.metrosultejo.ui.routes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class RoutesActivity extends AppCompatActivity {

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

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        RoutesPagerAdapter adapter = new RoutesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(R.drawable.baseline_directions_bus_24);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Change tab indicator color dynamically
                switch (position) {
                    case 0:
                        tabLayout.setSelectedTabIndicatorColor(
                                getResources().getColor(R.color.linha1, null)
                        ); // Accent color for Tab 1
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicatorColor(
                                getResources().getColor(R.color.linha2, null)
                        ); // Accent color for Tab 2
                        break;
                    case 2:
                        tabLayout.setSelectedTabIndicatorColor(
                                getResources().getColor(R.color.linha3, null)
                        ); // Accent color for Tab 3
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
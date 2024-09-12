package com.diasjoao.metrosultejo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.info.InfoActivity;
import com.diasjoao.metrosultejo.ui.map.MapActivity;
import com.diasjoao.metrosultejo.ui.schedule.ScheduleActivity;
import com.diasjoao.metrosultejo.ui.search.SearchFragment;
import com.diasjoao.metrosultejo.ui.routes.RoutesActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Switch dayNightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        dayNightSwitch = findViewById(R.id.daynight);
        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        SearchFragment searchFragment = new SearchFragment();

        // Add the SearchFragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        FloatingActionButton linesButton = findViewById(R.id.lines_button);
        linesButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent);
        });

        FloatingActionButton scheduleButton = findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("seasonId", 2);
            startActivity(intent);
        });

        FloatingActionButton mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        FloatingActionButton infoButton = findViewById(R.id.info_button);
        infoButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);
        });

        MaterialCardView line2CardView = findViewById(R.id.line_card_2);
        line2CardView.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent1);
        });

        MaterialCardView line3CardView = findViewById(R.id.line_card_3);
        line3CardView.setOnClickListener(view -> {
            Intent intent2 = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent2);
        });

    }
}
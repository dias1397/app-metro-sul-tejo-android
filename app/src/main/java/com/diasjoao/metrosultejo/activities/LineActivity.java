package com.diasjoao.metrosultejo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineActivity extends AppCompatActivity {

    private int lineId;
    private List<Integer> lineColors = List.of(R.color.linha1, R.color.linha2, R.color.linha3);
    private List<Integer> lineNames  = List.of(R.string.linha1, R.string.linha2, R.string.linha3);
    private List<Integer> lineStations = List.of(R.array.linha_11, R.array.linha_21, R.array.linha_31);

    private LinearLayout stationsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        lineId = getIntent().getIntExtra("line", 0);

        setUpHomeActivityToolbar(lineId);
        setUpHomeActivityViews();

        List<String> stations = List.of(this.getResources().getStringArray(lineStations.get(lineId)));

        populateStationsView(LineActivity.this, getLayoutInflater(), stations, stationsLayout, lineColors.get(getIntent().getIntExtra("line", 0)));
    }

    private void setUpHomeActivityToolbar(int line) {
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        toolbar.setBackgroundColor(getResources().getColor(lineColors.get(line)));
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle(lineNames.get(line));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpHomeActivityViews() {
        stationsLayout = findViewById(R.id.stations_layout);
    }

    private void populateStationsView(Context context, LayoutInflater inflater, List<String> stations, ViewGroup root, Integer color) {
        root.removeAllViews();

        System.out.println(color);

        for (int i = 0; i < stations.size(); i++) {
            View stationView = inflater.inflate(R.layout.fragment_station, root, false);

            TextView stationName = stationView.findViewById(R.id.stationName);
            stationName.setText(stations.get(i));

            FrameLayout prevStationPath = stationView.findViewById(R.id.prev_station_path);
            FrameLayout nextStationPath = stationView.findViewById(R.id.next_station_path);

            if (i == 0) {
                Drawable background = getDrawable(R.drawable.station_line_top);
                background.setTint(getResources().getColor(color));

                prevStationPath.setBackground(background);
                nextStationPath.setBackgroundColor(getResources().getColor(color));
            } else if(i == stations.size() - 1) {
                Drawable background = getDrawable(R.drawable.station_line_bottom);
                background.setTint(getResources().getColor(color));

                prevStationPath.setBackgroundColor(getResources().getColor(color));
                nextStationPath.setBackground(background);

            } else {
                prevStationPath.setBackgroundColor(getResources().getColor(color));
                nextStationPath.setBackgroundColor(getResources().getColor(color));
            }

            root.addView(stationView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
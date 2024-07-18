package com.diasjoao.metrosultejo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.helpers.JsonHelper;
import com.diasjoao.metrosultejo.model.StationOld;
import com.diasjoao.metrosultejo.utils.DateUtils;
import com.diasjoao.metrosultejo.adapters.LiveRecyclerViewAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LiveActivity extends AppCompatActivity {

    /*

    Corroios -> 0, -1, 0, -1, -1, -1.
    Casa do Povo -> 1, 11, 1, 7, -1, -1


    Cacilhas -> [], Corroios -> [],


     */

    private AutoCompleteTextView lineSpinner, stationSpinner;
    private RecyclerView recyclerView;

    private LocalDateTime currentTime;
    private int seasonId, dayTypeID;

    private JSONArray schedule;
    private int lineID, stationID;

    private final List<Integer> stationsByLine = List.of(R.array.linha_11, R.array.linha_12,
            R.array.linha_21, R.array.linha_22, R.array.linha_31, R.array.linha_32);

    private List<StationOld> stationOldTimes = new ArrayList<>();
    private LiveRecyclerViewAdapter adapter = new LiveRecyclerViewAdapter(this, stationOldTimes);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live2);

        setUpLiveActivityToolbar();
        setUpLiveActivityViews();

        loadTimeSettings();
        loadSchedule();
        loadLineAndStation();

        setLineSpinnerInfo();
        setStationSpinnerInfo();

        lineSpinner.setText(this.getResources().getStringArray(R.array.linhas)[lineID], false);
    }

    private void setUpLiveActivityToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Próximo");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    private void setUpLiveActivityViews() {
        lineSpinner = findViewById(R.id.line);
        stationSpinner = findViewById(R.id.station);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTimeSettings() {
        currentTime = LocalDateTime.now().minus(3, ChronoUnit.HOURS);

        seasonId = DateUtils.getSeasonId(currentTime);
        dayTypeID = DateUtils.getDayTypeId(currentTime);
    }
    // TODO rearrange
    private void loadSchedule() {
        try {
            JSONArray file = new JSONArray(Objects.requireNonNull(JsonHelper.getJsonFromAssets(this, "schedule.json")));
            schedule = JsonHelper.getLinesSchedule(file, seasonId, dayTypeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadLineAndStation() {
        lineID = getIntent().getIntExtra("line", 0);
        stationID = getIntent().getIntExtra("station", 0);
    }

    private void setLineSpinnerInfo() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                R.layout.activity_map,
                this.getResources().getStringArray(R.array.linhas)
        );

        lineSpinner.setAdapter(adapter);
        lineSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = LiveActivity.this;
                lineID = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.linhas))).indexOf(charSequence.toString());

                try {
                    ArrayAdapter<CharSequence> stationsAdapter = new ArrayAdapter<>(context,
                            R.layout.activity_map,
                            context.getResources().getStringArray(stationsByLine.get(lineID))
                    );

                    stationID = Math.min(stationID, stationsAdapter.getCount() - 1);

                    stationSpinner.setAdapter(stationsAdapter);
                    stationSpinner.setText(context.getResources().getStringArray(stationsByLine.get(lineID))[stationID], false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
    private void setStationSpinnerInfo() {
        stationSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = LiveActivity.this;
                stationID = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(stationsByLine.get(lineID)))).indexOf(charSequence.toString());

                try {
                    JSONArray jsonStationTimes = JsonHelper.getStationTimes(schedule, lineID, stationID);
                    stationOldTimes = JsonHelper.mapStationTimes(jsonStationTimes, currentTime);
                    adapter.refreshDataSet(stationOldTimes);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
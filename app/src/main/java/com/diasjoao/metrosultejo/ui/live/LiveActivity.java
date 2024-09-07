package com.diasjoao.metrosultejo.ui.live;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.model.Station;
import com.diasjoao.metrosultejo.data.repository.ScheduleRepository;
import com.diasjoao.metrosultejo.ui.search.SearchFragment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        SearchFragment searchFragment = new SearchFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("lineId", intent.getIntExtra("lineId", 0));
        bundle.putInt("stationId", intent.getIntExtra("stationId", 0));

        searchFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ScheduleRepository scheduleRepository = new ScheduleRepository(this);

        Station station = scheduleRepository.findStationBySeasonAndDayAndLineAndName(1, 1, 1, "Corroios");

        LocalDateTime startBound = LocalDateTime.now();
        LocalDateTime endBound = LocalDateTime.now()
                .plusDays(startBound.getHour() >= 3 ? 1 : 0)
                .withHour(3)
                .withMinute(0);

        List<LocalDateTime> times = station.getConvertedTimes().stream()
                .filter(time -> time.isAfter(startBound.minusMinutes(10)) && time.isBefore(endBound))
                .collect(Collectors.toList());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LiveAdapter(this, times));
    }
}
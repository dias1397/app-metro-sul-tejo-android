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
import com.diasjoao.metrosultejo.helpers.DateHelper;
import com.diasjoao.metrosultejo.ui.search.SearchFragment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class LiveActivity extends AppCompatActivity {

    private int dayId, lineId, stationId = 1;

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

        // get initialization info
        Intent intent = getIntent();
        lineId = intent.getIntExtra("lineId", 1);
        stationId = intent.getIntExtra("stationId", 0);

        // updates search fragment
        SearchFragment searchFragment = new SearchFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("lineId", lineId - 1);
        bundle.putInt("stationId", stationId);

        searchFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ScheduleRepository scheduleRepository = new ScheduleRepository(this);

        LocalDate today = LocalDate.from(LocalDateTime.now().minusHours(3));
        if (DateHelper.isHoliday(this, today) || today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dayId = 3;
        } else if (today.getDayOfWeek() == DayOfWeek.SATURDAY) {
            dayId = 2;
        } else {
            dayId = 1;
        }

        Station station = scheduleRepository.findStationBySeasonAndDayAndLineAndName(1, dayId, lineId, stationId);

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
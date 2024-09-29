package com.diasjoao.metrosultejo.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;

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
import com.diasjoao.metrosultejo.utils.DateUtils;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LiveActivity extends AppCompatActivity {

    private int dayId, lineId, stationId = 1;
    private final Map<Integer, String> destinationByLineId = Map.of(
            1, "Cacilhas", 2, "Corroios",
            3, "Pragal", 4, "Corroios",
            5, "Universidade", 6, "Cacilhas");
    private LiveAdapter liveAdapter;
    private CountDownTimer countDownTimer;

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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        // get initialization info
        Intent intent = getIntent();
        lineId = intent.getIntExtra("lineId", 1);
        stationId = intent.getIntExtra("stationId", 0);
        String stationName = intent.getStringExtra("stationName");

        // updates search fragment
        SearchFragment searchFragment = new SearchFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("lineId", lineId - 1);
        bundle.putInt("stationId", stationId);

        searchFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        LinearLayout noTimesLayout = findViewById(R.id.no_times_layout);
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

        int seasonId = DateUtils.getSeasonId(LocalDateTime.now().minusHours(3));
        Station station = scheduleRepository.findStationBySeasonAndDayAndLineAndName(seasonId + 1, dayId, lineId, stationId);

        LocalDateTime startBound = LocalDateTime.now();
        LocalDateTime endBound = LocalDateTime.now()
                .plusDays(startBound.getHour() >= 3 ? 1 : 0)
                .withHour(3)
                .withMinute(0);

        List<LocalDateTime> times = station.getConvertedTimes().stream()
                .filter(time -> time.isAfter(startBound.minusMinutes(9)) && time.isBefore(endBound))
                .collect(Collectors.toList());

        if (times.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noTimesLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noTimesLayout.setVisibility(View.GONE);

            liveAdapter = new LiveAdapter(this, times, stationName + " → " + destinationByLineId.get(lineId));

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(liveAdapter);

            startCountdown();
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //int secondsRemaining = (int) (millisUntilFinished / 1000);

                //String timeFormatted = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60);
                //System.out.println(timeFormatted);
            }

            @Override
            public void onFinish() {
                liveAdapter.notifyDataSetChanged();
                startCountdown();
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
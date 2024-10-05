package com.diasjoao.metrosultejo.ui.activity;

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
import com.diasjoao.metrosultejo.ui.adapter.LiveAdapter;
import com.diasjoao.metrosultejo.ui.fragment.SearchFragment;
import com.diasjoao.metrosultejo.utils.DateUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.android.material.appbar.MaterialToolbar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LiveActivity extends AppCompatActivity {

    private final List<String> routes = List.of(
            "%s → Cacilhas", "%s → Corroios", "%s → Pragal",
            "%s → Corroios", "%s → Universidade", "%s → Cacilhas"
    );

    private MaterialToolbar materialToolbar;
    private SearchFragment searchFragment;
    private LinearLayout errorLayout;
    private RecyclerView recyclerView;
    private AdView adBannerView;

    private List<LocalDateTime> stationTimes;
    private LiveAdapter liveAdapter;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        initVars();
        initViews();

        setupUI();
        setupAds();
    }

    private void initVars() {
        Intent intent = getIntent();
        int lineId = intent.getIntExtra("lineId", 1);
        int stationId = intent.getIntExtra("stationId", 0);

        Bundle bundle = new Bundle();
        bundle.putInt("lineId", lineId - 1);
        bundle.putInt("stationId", stationId);

        searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);

        LocalDateTime rightNow = LocalDateTime.now();
        int seasonId = DateUtils.getSeasonId(rightNow.minusHours(3));
        int dayId = getDayId();

        ScheduleRepository scheduleRepository = new ScheduleRepository(this);
        Station station = scheduleRepository.findStationBySeasonAndDayAndLineAndName(
                seasonId + 1, dayId, lineId, stationId
        );

        LocalDateTime endBound = rightNow.plusDays(rightNow.getHour() >= 3 ? 1 : 0)
                .withHour(3).withMinute(0);

        stationTimes = station.getConvertedTimes().stream()
                .filter(time -> time.isAfter(rightNow.minusMinutes(9)) && time.isBefore(endBound))
                .collect(Collectors.toList());
        liveAdapter = new LiveAdapter(this, stationTimes,
                String.format(routes.get(lineId - 1), station.getName())
        );
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);
        errorLayout = findViewById(R.id.no_times_layout);
        recyclerView = findViewById(R.id.recyclerView);
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
        materialToolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        if (!stationTimes.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(liveAdapter);
            startCountdown();
        }

        int errorVisibility = stationTimes.isEmpty() ? View.VISIBLE : View.GONE;
        int recyclerVisibility = stationTimes.isEmpty() ? View.GONE : View.VISIBLE;

        errorLayout.setVisibility(errorVisibility);
        recyclerView.setVisibility(recyclerVisibility);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
    }

    private int getDayId() {
        LocalDate today = LocalDate.from(LocalDateTime.now().minusHours(3));
        if (DateHelper.isHoliday(this, today) || today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return 3;
        } else if (today.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return 2;
        } else {
            return 1;
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
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
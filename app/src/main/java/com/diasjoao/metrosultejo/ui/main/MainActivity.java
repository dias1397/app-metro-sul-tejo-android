package com.diasjoao.metrosultejo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.adapters.NewsAdapter;
import com.diasjoao.metrosultejo.data.model.News;
import com.diasjoao.metrosultejo.ui.tariffs.TariffsActivity;
import com.diasjoao.metrosultejo.ui.map.MapActivity;
import com.diasjoao.metrosultejo.ui.schedule.ScheduleActivity;
import com.diasjoao.metrosultejo.ui.search.SearchFragment;
import com.diasjoao.metrosultejo.ui.routes.RoutesActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private ProgressBar newsProgressBar;
    private AdView adView;
    private FloatingActionButton linesButton, scheduleButton, mapButton, tariffButton;
    private LinearLayout line1Layout, line2Layout, line3Layout;

    private final List<News> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        setupUI();
        setupAds();
        setupSearchFragment();
        setupClickListeners();

        loadNews();
    }

    private void initializeViews() {
        newsRecyclerView = findViewById(R.id.news_recycler_view);
        newsProgressBar = findViewById(R.id.news_progress_bar);
        adView = findViewById(R.id.adView);

        linesButton = findViewById(R.id.lines_button);
        scheduleButton = findViewById(R.id.schedule_button);
        mapButton = findViewById(R.id.map_button);
        tariffButton = findViewById(R.id.info_button);

        line1Layout = findViewById(R.id.line1_layout);
        line2Layout = findViewById(R.id.line2_layout);
        line3Layout = findViewById(R.id.line3_layout);
    }

    private void setupUI() {
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setAdapter(newsAdapter);

        newsRecyclerView.setVisibility(View.GONE);
        newsProgressBar.setVisibility(View.VISIBLE);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setupSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();
    }

    private void setupClickListeners() {
        linesButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RoutesActivity.class)));
        scheduleButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("seasonId", 2);
            startActivity(intent);
        });
        mapButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MapActivity.class)));
        tariffButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TariffsActivity.class)));

        line1Layout.setOnClickListener(view -> startRouteActivity(0));
        line2Layout.setOnClickListener(view -> startRouteActivity(1));
        line3Layout.setOnClickListener(view -> startRouteActivity(2));
    }

    private void startRouteActivity(int lineId) {
        Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
        intent.putExtra("lineId", lineId);
        startActivity(intent);
    }

    private void loadNews() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<News> fetchedNews = fetchNews();

                runOnUiThread(() -> {
                    if (!fetchedNews.isEmpty()) {
                        newsList.clear();
                        newsList.addAll(fetchedNews);
                        newsAdapter.notifyDataSetChanged();
                    }
                    newsRecyclerView.setVisibility(View.VISIBLE);
                    newsProgressBar.setVisibility(View.GONE);
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    newsProgressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                });
            }
        });
    }

    private List<News> fetchNews() throws IOException {
        List<News> fetchedNewsList = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.mts.pt/category/noticias/").get();

        Element main = doc.getElementById("main");
        Elements newsElements = main.select("div > section > ul").first().children();

        for (Element news : newsElements.subList(0, Math.min(newsElements.size(), 15))) {
            String title = news.select("a > h4.pages-news__title").text();
            String url = news.select("a").attr("href");
            String details = news.select("p").text();
            String date = news.select("h6.pages-news__date").text();
            String imageUrl = Objects.requireNonNull(news.selectFirst("figure.pages-news__figure"))
                    .attr("style")
                    .replaceAll(".*url\\(['\"]?(.*?)['\"]?\\).*", "$1");

            News oneNews = new News();
            oneNews.setTitle(title);
            oneNews.setUrl(url);
            oneNews.setDetails(details);
            oneNews.setDate(date);
            oneNews.setImageUrl(imageUrl);

            fetchedNewsList.add(oneNews);
        }

        return fetchedNewsList;
    }
}
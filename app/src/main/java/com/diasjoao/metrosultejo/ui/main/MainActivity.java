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
    private ProgressBar loadingProgressBar;
    private AdView adBannerView;
    private FloatingActionButton routeLinesFab, scheduleFab, mapFab, tariffFab;
    private LinearLayout line1Layout, line2Layout, line3Layout;

    private final List<News> newsItems = new ArrayList<>();
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        setupUI();
        setupAds();
        addSearchFragment();
        setupButtonListeners();

        loadNewsContent();
    }

    private void initializeViews() {
        newsRecyclerView = findViewById(R.id.news_recycler_view);
        loadingProgressBar = findViewById(R.id.news_progress_bar);
        adBannerView = findViewById(R.id.adView);

        routeLinesFab = findViewById(R.id.lines_button);
        scheduleFab = findViewById(R.id.schedule_button);
        mapFab = findViewById(R.id.map_button);
        tariffFab = findViewById(R.id.info_button);

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
        newsAdapter = new NewsAdapter(this, newsItems);
        newsRecyclerView.setAdapter(newsAdapter);

        newsRecyclerView.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
    }

    private void addSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();
    }

    private void setupButtonListeners() {
        routeLinesFab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RoutesActivity.class)));
        scheduleFab.setOnClickListener(view -> {
            Intent scheduleIntent = new Intent(MainActivity.this, ScheduleActivity.class);
            scheduleIntent.putExtra("seasonId", 2);
            startActivity(scheduleIntent);
        });
        mapFab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MapActivity.class)));
        tariffFab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TariffsActivity.class)));

        line1Layout.setOnClickListener(view -> startRouteActivity(0));
        line2Layout.setOnClickListener(view -> startRouteActivity(1));
        line3Layout.setOnClickListener(view -> startRouteActivity(2));
    }

    private void startRouteActivity(int lineId) {
        Intent routeIntent = new Intent(MainActivity.this, RoutesActivity.class);
        routeIntent.putExtra("lineId", lineId);
        startActivity(routeIntent);
    }

    private void loadNewsContent() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<News> fetchedNews = fetchNewsData();

                runOnUiThread(() -> {
                    if (!fetchedNews.isEmpty()) {
                        newsItems.clear();
                        newsItems.addAll(fetchedNews);
                        newsAdapter.notifyDataSetChanged();
                    }
                    newsRecyclerView.setVisibility(View.VISIBLE);
                    loadingProgressBar.setVisibility(View.GONE);
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                });
            }
        });
    }

    private List<News> fetchNewsData() throws IOException {
        List<News> fetchedNewsList = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.mts.pt/category/noticias/").get();

        Element mainElement = doc.getElementById("main");
        Elements newsElements = mainElement.select("div > section > ul").first().children();

        for (Element newsElement : newsElements.subList(0, Math.min(newsElements.size(), 15))) {
            String title = newsElement.select("a > h4.pages-news__title").text();
            String url = newsElement.select("a").attr("href");
            String details = newsElement.select("p").text();
            String date = newsElement.select("h6.pages-news__date").text();
            String imageUrl = Objects.requireNonNull(newsElement.selectFirst("figure.pages-news__figure"))
                    .attr("style")
                    .replaceAll(".*url\\(['\"]?(.*?)['\"]?\\).*", "$1");

            News newsItem = new News();
            newsItem.setTitle(title);
            newsItem.setUrl(url);
            newsItem.setDetails(details);
            newsItem.setDate(date);
            newsItem.setImageUrl(imageUrl);

            fetchedNewsList.add(newsItem);
        }

        return fetchedNewsList;
    }
}
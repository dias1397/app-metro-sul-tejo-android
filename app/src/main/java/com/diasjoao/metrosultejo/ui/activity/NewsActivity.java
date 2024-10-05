package com.diasjoao.metrosultejo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;

public class NewsActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private TextView newsTitleTextView;
    private PhotoView newsImageView;
    private ProgressBar newsProgressBar;
    private TextView newsDetailsTextView;
    private TextView newsDateTextView;
    private AdView adBannerView;

    private String newsUrl;
    private String newsTitle;
    private String newsImageURL;
    private String newsDetails;
    private String newsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initVars();
        initViews();

        setupUI();
        setupAds();

        fetchNewsDetail();
    }

    private void initVars() {
        Intent intent = getIntent();

        newsUrl = intent.getStringExtra("NEWS_URL");
        newsTitle = intent.getStringExtra("NEWS_TITLE");
        newsImageURL = intent.getStringExtra("NEWS_IMAGE_URL");
        newsDate = intent.getStringExtra("NEWS_DATE");
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);

        newsImageView = findViewById(R.id.newsImageView);
        newsTitleTextView = findViewById(R.id.newsTitleTextView);
        newsProgressBar = findViewById(R.id.progressBar);
        newsDetailsTextView = findViewById(R.id.newsDetailsTextView);
        newsDateTextView = findViewById(R.id.newsDateTextView);

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
        materialToolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        newsTitleTextView.setText(newsTitle);
        if (!newsImageURL.isBlank()) {
            Picasso.get().load(newsImageURL).into(newsImageView);
        }
        newsDateTextView.setText(newsDate);
        newsDetailsTextView.setText(newsDetails);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> { });

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
    }

    private void fetchNewsDetail() {
        Executors.newSingleThreadExecutor().execute(() -> {
            String errorMessage = "Unable to fetch news data";
            String newsDetails = errorMessage;

            try {
                Document doc = Jsoup.connect(newsUrl).get();
                newsDetails = Optional.ofNullable(doc.getElementById("main"))
                        .map(main -> main.select("div > section").first())
                        .map(section -> section.children().select("div").html())
                        .orElse(errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String finalNewsDetails = newsDetails;
            runOnUiThread(() -> {
                newsDetailsTextView.setText(Html.fromHtml(finalNewsDetails));

                newsProgressBar.setVisibility(View.GONE);
                newsDetailsTextView.setVisibility(View.VISIBLE);
            });
        });

    }
}
package com.diasjoao.metrosultejo.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.adapters.NewsAdapter;
import com.diasjoao.metrosultejo.data.model.News;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Executors;

public class NewsActivity extends AppCompatActivity {

    private PhotoView newsImageView;
    private TextView newsTitleTextView;
    private TextView newsDetailsTextView;
    private TextView newsDateTextView;

    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);

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

        newsImageView = findViewById(R.id.newsImageView);
        newsTitleTextView = findViewById(R.id.newsTitleTextView);
        newsDetailsTextView = findViewById(R.id.newsDetailsTextView);
        newsDateTextView = findViewById(R.id.newsDateTextView);

        Intent intent = getIntent();
        String newsTitle = intent.getStringExtra("NEWS_TITLE");
        newsUrl = intent.getStringExtra("NEWS_URL");
        String newsDetails = intent.getStringExtra("NEWS_DETAILS");
        String newsImageURL = intent.getStringExtra("NEWS_IMAGE_URL");
        String newsDate = intent.getStringExtra("NEWS_DATE");

        newsTitleTextView.setText(newsTitle);
        newsDetailsTextView.setText(newsDetails);
        newsDateTextView.setText(newsDate);
        if (newsImageURL != null && !newsImageURL.isEmpty()) {
            Picasso.get().load(newsImageURL).into(newsImageView);
        }

        Executors.newSingleThreadExecutor().execute(this::getNewsDetail);
    }

    private void getNewsDetail() {
        try {
            Document doc = Jsoup.connect(newsUrl).get();

            Element main = doc.getElementById("main");

            Elements newsElements = main.select("div > section").first().children();

            String details = newsElements.select("div").html();

            new Handler(Looper.getMainLooper()).post(() -> {
                newsDetailsTextView.setText(Html.fromHtml(details));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
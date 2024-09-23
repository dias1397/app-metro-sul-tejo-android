package com.diasjoao.metrosultejo.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

public class NewsActivity extends AppCompatActivity {

    private ImageView newsImageView;
    private TextView newsTitleTextView;
    private TextView newsDetailsTextView;
    private TextView newsDateTextView;

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
        String newsDetails = intent.getStringExtra("NEWS_DETAILS");
        String newsImageURL = intent.getStringExtra("NEWS_IMAGE_URL");
        String newsDate = intent.getStringExtra("NEWS_DATE");

        newsTitleTextView.setText(newsTitle);
        newsDetailsTextView.setText(newsDetails);
        newsDateTextView.setText(newsDate);
        if (newsImageURL != null && !newsImageURL.isEmpty()) {
            Picasso.get().load(newsImageURL).into(newsImageView);
        }
    }
}
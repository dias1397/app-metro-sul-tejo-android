package com.diasjoao.metrosultejo.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.card.MaterialCardView;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialCardView searchCardView = findViewById(R.id.search_card);
        searchCardView.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, RealTimingActivity.class);
            startActivity(intent);
        });

        MaterialCardView line1CardView = findViewById(R.id.line_card_1);
        line1CardView.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, RoutesActivity.class);
            startActivity(intent);
        });

        MaterialCardView line2CardView = findViewById(R.id.line_card_2);
        line2CardView.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, RoutesActivity.class);
            startActivity(intent);
        });

        MaterialCardView line3CardView = findViewById(R.id.line_card_3);
        line3CardView.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, RoutesActivity.class);
            startActivity(intent);
        });

    }
}
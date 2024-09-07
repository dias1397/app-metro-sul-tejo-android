package com.diasjoao.metrosultejo.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.search.SearchFragment;
import com.diasjoao.metrosultejo.ui.routes.RoutesActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SearchFragment searchFragment = new SearchFragment();

        // Add the SearchFragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        MaterialCardView line2CardView = findViewById(R.id.line_card_2);
        line2CardView.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent1);
        });

        MaterialCardView line3CardView = findViewById(R.id.line_card_3);
        line3CardView.setOnClickListener(view -> {
            Intent intent2 = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent2);
        });

    }
}
package com.diasjoao.metrosultejo.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.activities.LiveActivityOld;
import com.diasjoao.metrosultejo.ui.activities.MapActivity;
import com.diasjoao.metrosultejo.ui.activities.ScheduleActivity;
import com.google.android.material.appbar.MaterialToolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpHomeActivityToolbar();
        setUpHomeActivityButtons();
        setUpLineStateButtons();
    }

    private void setUpHomeActivityToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpHomeActivityButtons() {
        CardView nextCardView = findViewById(R.id.next_cardview);
        nextCardView.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), LiveActivity.class);
            intent.putExtra("line", 0);
            intent.putExtra("station", 0);
            startActivity(intent);
        });

        CardView scheduleCardView = findViewById(R.id.schedule_cardview);
        scheduleCardView.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Horários", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), ScheduleActivity.class);
            intent.putExtra("line", 0);
            startActivity(intent);
        });

        CardView mapCardView = findViewById(R.id.map_cardview);
        mapCardView.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Mapa", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), MapActivity.class);
            startActivity(intent);
        });

        CardView pricesCardView = findViewById(R.id.prices_cardview);
        pricesCardView.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Tarifas", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), LiveActivityOld.class);
            intent.putExtra("line", 0);
            intent.putExtra("station", 0);
            startActivity(intent);
        });
    }

    private void setUpLineStateButtons() {
        CardView line1CardView = findViewById(R.id.line1_cardview);
        line1CardView.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Linha 1", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), LineActivity.class);
            intent.putExtra("line", 0);
            startActivity(intent);
        });

        CardView line2CardView = findViewById(R.id.line2_cardview);
        line2CardView.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Linha 2", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), LineActivity.class);
            intent.putExtra("line", 1);
            startActivity(intent);
        });

        CardView line3CardView = findViewById(R.id.line3_cardview);
        line3CardView.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Linha 3", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), LineActivity.class);
            intent.putExtra("line", 2);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miRefresh:
                Toast.makeText(HomeActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.miInfo:
                Toast.makeText(HomeActivity.this, "Info", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
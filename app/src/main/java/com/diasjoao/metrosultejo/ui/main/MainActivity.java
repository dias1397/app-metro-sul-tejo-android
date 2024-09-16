package com.diasjoao.metrosultejo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.tariffs.TariffsActivity;
import com.diasjoao.metrosultejo.ui.map.MapActivity;
import com.diasjoao.metrosultejo.ui.schedule.ScheduleActivity;
import com.diasjoao.metrosultejo.ui.search.SearchFragment;
import com.diasjoao.metrosultejo.ui.routes.RoutesActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Switch dayNightSwitch;

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

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        dayNightSwitch = findViewById(R.id.daynight);
        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        SearchFragment searchFragment = new SearchFragment();

        // Add the SearchFragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit();

        FloatingActionButton linesButton = findViewById(R.id.lines_button);
        linesButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent);
        });

        FloatingActionButton scheduleButton = findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("seasonId", 2);
            startActivity(intent);
        });

        FloatingActionButton mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        FloatingActionButton infoButton = findViewById(R.id.info_button);
        infoButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TariffsActivity.class);
            startActivity(intent);
        });

        LinearLayout line1_layout = findViewById(R.id.line1_layout);
        line1_layout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
            intent.putExtra("lineId", 0);
            startActivity(intent);
        });

        LinearLayout line2_layout = findViewById(R.id.line2_layout);
        line2_layout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
            intent.putExtra("lineId", 1);
            startActivity(intent);
        });

        LinearLayout line3_layout = findViewById(R.id.line3_layout);
        line3_layout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
            intent.putExtra("lineId", 2);
            startActivity(intent);
        });

        MaterialCardView line3CardView = findViewById(R.id.line_card_3);
        line3CardView.setOnClickListener(view -> {
            Intent intent2 = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent2);
        });

        //Executors.newSingleThreadExecutor().execute(this::parse);
    }

    private void parse() {
        try {
            // Connect to the website and parse the HTML
            Document doc = Jsoup.connect("https://www.mts.pt/tarifarios/").get();

            // Select specific elements (for example, tarifarios table)
            Elements tarifarios = doc.getElementById("main").child(0).child(0).children(); // Adjust selector to target the correct element

            // Create a JSON object to store parsed data
            JsonObject jsonTarifarios = new JsonObject();

            JsonArray lista = new JsonArray();

            // Iterate through the selected elements and extract data
            for (Element tarifario : tarifarios) {
                if (tarifario.child(0).child(0).childrenSize() == 0) {
                    continue;
                }
                Element child1 = tarifario.child(0)
                        .child(0)
                        .child(1)
                        .getElementsByTag("tbody")
                        .get(0)
                        .child(0);

                String title = child1.child(0).text(); // Adjust selector
                String price = child1.child(1).text(); // Adjust selector


                // Add data to JSON object
                JsonObject tarifarioDetails = new JsonObject();
                tarifarioDetails.addProperty("title", title);
                tarifarioDetails.addProperty("price", price);

                Element child2 = tarifario.child(1).child(0);
                Elements para = child2.getElementsByTag("p");
                for (Element p : para) {
                    String paragraph = p.text();

                    String key = "descrição";
                    String value = paragraph;
                    if (p.html().startsWith("<strong>") && p.html().contains("</strong>")) {
                        key = p.html().substring(p.html().indexOf("<strong>") + 8, p.html().indexOf("</strong>"));
                        value = p.html().substring(p.html().indexOf("</strong><br>\n ") + 15).trim();
                    }

                    if (tarifarioDetails.get(key) != null) {
                        String tempValue = tarifarioDetails.get(key).getAsString();
                        tarifarioDetails.addProperty(key, tempValue + "\n\n" + value);
                    } else {
                        tarifarioDetails.addProperty(key.toLowerCase(), value);
                    }
                }

                lista.add(tarifarioDetails);

                //jsonTarifarios.add(title, tarifarioDetails);
            }

            jsonTarifarios.add("tarifarios", lista);

            System.out.println("Data successfully written to file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
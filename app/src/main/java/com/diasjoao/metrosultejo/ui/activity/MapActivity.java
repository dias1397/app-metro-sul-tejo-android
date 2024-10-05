package com.diasjoao.metrosultejo.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.DataManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;
    private MapView mapView;
    private AdView adBannerView;

    private GeoPoint startPoint;
    private Drawable drawable;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initVars();
        initViews();

        setupUI();
        setupAds();

        addPolylinesAndMarkers();
    }

    private void initVars() {
        Configuration.getInstance().setUserAgentValue(getPackageName());

        startPoint = new GeoPoint(38.6662430, -9.1779545);
        drawable = getResources().getDrawable(R.drawable.custom_marker, null);
        dataManager = new DataManager(this);
    }

    private void initViews() {
        materialToolbar = findViewById(R.id.toolbar);
        mapView = findViewById(R.id.mapView);
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

        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(14.5);
        mapView.getController().setCenter(startPoint);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {});

        AdRequest adRequest = new AdRequest.Builder().build();
        adBannerView.loadAd(adRequest);
    }

    private void addPolylinesAndMarkers() {
        for (DataManager.Line line : dataManager.getLines()) { // Assuming 3 lines
            addPolylines(line);
            addMarkers(line);
        }
    }

    private void addPolylines(DataManager.Line line) {
        Polyline polyline = new Polyline();

        polyline.setColor(
                getColor(getResources().getIdentifier(line.getColor(), "color", this.getPackageName()))
        );

        List<GeoPoint> geoPoints = new ArrayList<>();
        for (DataManager.Station station : line.getStations()) {
            GeoPoint temp = station.getGeoPoint().clone();

            if (line.getColor().equals("linha1")) {
                //temp.setLatitude(temp.getLatitude() + 0.0002);
                temp.setLongitude(temp.getLongitude() + 0.0002);
            }

            if (line.getColor().equals("linha2")) {
                temp.setLatitude(temp.getLatitude() - 0.0002);
                temp.setLongitude(temp.getLongitude() - 0.0002);
            }

            if (line.getColor().equals("linha3")) {
                temp.setLatitude(temp.getLatitude() + 0.0002);
                temp.setLongitude(temp.getLongitude() - 0.0002);
            }

            geoPoints.add(temp);
        }

        polyline.setPoints(geoPoints);
        polyline.setWidth(10.0f);

        mapView.getOverlays().add(polyline);
    }

    private void addMarkers(DataManager.Line line) {
        for (DataManager.Station station : line.getStations()) {
            Marker marker = new Marker(mapView);

            marker.setPosition(station.getGeoPoint());
            marker.setTitle(station.getName());

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            marker.setIcon(drawable);

            mapView.getOverlays().add(marker);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
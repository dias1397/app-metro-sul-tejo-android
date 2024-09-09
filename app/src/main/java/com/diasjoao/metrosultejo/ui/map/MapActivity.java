package com.diasjoao.metrosultejo.ui.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.DataManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private DataManager dataManager;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        GeoPoint startPoint = new GeoPoint(38.6635900, -9.2073900);
        mapView.getController().setCenter(startPoint);

        drawable = getResources().getDrawable(R.drawable.custom_marker, null);

        dataManager = new DataManager(this);

        addPolylinesAndMarkers();
    }

    private void addPolylinesAndMarkers() {
        for (List<GeoPoint> line : dataManager.getLines()) { // Assuming 3 lines
            addPolylines(line);
            addMarkers(line);
        }
    }

    private void addPolylines(List<GeoPoint> points) {
        Polyline polyline = new Polyline();
        polyline.setPoints(points);
        //polyline.setColor(); // Or use setColor if available
        polyline.setWidth(5.0f);
        mapView.getOverlays().add(polyline);
    }

    private void addMarkers(List<GeoPoint> points) {
        for (GeoPoint point : points) {
            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            //marker.setTitle();

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            marker.setIcon(drawable);

            mapView.getOverlays().add(marker);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume(); // needed for osmdroid map lifecycle
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause(); // needed for osmdroid map lifecycle
    }
}
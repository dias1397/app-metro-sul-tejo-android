package com.diasjoao.metrosultejo.data;

import android.content.Context;
import android.util.JsonReader;

import com.diasjoao.metrosultejo.R;

import org.osmdroid.util.GeoPoint;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<List<GeoPoint>> lines = new ArrayList<>();

    public DataManager(Context context) {
        loadData(context);
    }

    private void loadData(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.locations);
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            reader.beginObject();
            reader.nextName(); // Skip "lines" key
            reader.beginArray();

            while (reader.hasNext()) {
                List<GeoPoint> line = new ArrayList<>();
                reader.beginArray();

                while (reader.hasNext()) {
                    reader.beginObject();
                    double lat = 0.0;
                    double lon = 0.0;

                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equals("lat")) {
                            lat = reader.nextDouble();
                        } else if (name.equals("lon")) {
                            lon = reader.nextDouble();
                        } else {
                            reader.skipValue();
                        }
                    }

                    reader.endObject();
                    line.add(new GeoPoint(lat, lon));
                }

                reader.endArray();
                lines.add(line);
            }

            reader.endArray();
            reader.endObject();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<List<GeoPoint>> getLines() {
        return lines;
    }
}


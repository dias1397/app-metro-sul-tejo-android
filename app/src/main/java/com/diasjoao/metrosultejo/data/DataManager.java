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

    // Line class to store line metadata and its stations
    public class Line {
        int id;
        String color;
        List<Station> stations;

        public Line(int id, String color, List<Station> stations) {
            this.id = id;
            this.color = color;
            this.stations = stations;
        }

        public String getColor() {
            return color;
        }

        public List<Station> getStations() {
            return stations;
        }
    }

    // Station class to store station metadata
    public class Station {
        int id;
        String name;
        GeoPoint geoPoint;

        public Station(int id, String name, GeoPoint geoPoint) {
            this.id = id;
            this.name = name;
            this.geoPoint = geoPoint;
        }

        public String getName() {
            return name;
        }

        public GeoPoint getGeoPoint() {
            return geoPoint;
        }
    }

    // Stores all parsed lines with metadata
    private List<Line> lines = new ArrayList<>();

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
                List<Station> stations = new ArrayList<>();
                String color = null;
                int lineId = 0;

                reader.beginObject(); // Start line object

                while (reader.hasNext()) {
                    String linePropertyName = reader.nextName();
                    if (linePropertyName.equals("id")) {
                        lineId = reader.nextInt();
                    } else if (linePropertyName.equals("color")) {
                        color = reader.nextString();
                    } else if (linePropertyName.equals("stations")) {
                        reader.beginArray(); // Begin stations array

                        while (reader.hasNext()) {
                            int stationId = 0;
                            String stationName = null;
                            double lat = 0.0;
                            double lon = 0.0;

                            reader.beginObject();
                            while (reader.hasNext()) {
                                String stationPropertyName = reader.nextName();
                                switch (stationPropertyName) {
                                    case "id":
                                        stationId = reader.nextInt();
                                        break;
                                    case "name":
                                        stationName = reader.nextString();
                                        break;
                                    case "lat":
                                        lat = reader.nextDouble();
                                        break;
                                    case "lon":
                                        lon = reader.nextDouble();
                                        break;
                                    default:
                                        reader.skipValue(); // Skip unexpected values
                                        break;
                                }
                            }
                            reader.endObject();

                            // Create and add station
                            stations.add(new Station(stationId, stationName, new GeoPoint(lat, lon)));
                        }

                        reader.endArray(); // End stations array
                    } else {
                        reader.skipValue(); // Skip other line properties if any
                    }
                }

                reader.endObject(); // End line object

                // Add the parsed line with its stations
                lines.add(new Line(lineId, color, stations));
            }

            reader.endArray();
            reader.endObject();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter for lines with metadata
    public List<Line> getLines() {
        return lines;
    }
}
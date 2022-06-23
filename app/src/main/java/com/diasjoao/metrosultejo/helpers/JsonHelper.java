package com.diasjoao.metrosultejo.helpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonHelper {

    private static Map<Integer, Integer> lineKeyById = Map.of(
            0, 0, 1, 0,
            2, 1, 3, 1,
            4, 2, 5, 2);

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static JSONObject getLineInfo(JSONObject file, int lineId) {
        try {
            JSONObject line = file.getJSONArray("lines").getJSONObject(lineKeyById.getOrDefault(lineId, 0));
            return line.getJSONArray("directions").getJSONObject(lineId % 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getStationOffset(JSONObject lineJson, int stationId) {
        try {
            return Integer.parseInt(lineJson.getJSONArray("offsets").getString(stationId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static JSONArray getStationTimes(JSONObject lineJson, Boolean isSummer, String weekday) {
        int combination;
        if (isSummer) {
            if (weekday.equals("WEEKDAY")) {
                combination = 0;
            } else if (weekday.equals("SATURDAY")) {
                combination = 1;
            } else {
                combination = 2;
            }
        } else {
            if (weekday.equals("WEEKDAY")) {
                combination = 3;
            } else if (weekday.equals("SATURDAY")) {
                combination = 4;
            } else {
                combination = 5;
            }
        }

        try {
            JSONObject departures = lineJson.getJSONArray("departures").getJSONObject(combination);
            return departures.getJSONObject("times").getJSONArray("times");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

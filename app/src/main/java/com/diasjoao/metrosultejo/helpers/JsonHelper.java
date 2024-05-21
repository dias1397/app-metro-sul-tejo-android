package com.diasjoao.metrosultejo.helpers;

import android.content.Context;

import com.diasjoao.metrosultejo.model.Station;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static JSONArray getLinesSchedule(JSONArray jsonArray, int season, int day_type) throws JSONException {
        JSONObject seasonObject = jsonArray.getJSONObject(season);
        JSONObject dayTypeObject = seasonObject.getJSONArray("dayTypes").getJSONObject(day_type);
        return dayTypeObject.getJSONArray("line");
    }

    public static JSONArray getStationTimes(JSONArray jsonArray, int line, int station) throws JSONException {
        JSONObject lineObject = jsonArray.getJSONObject(line);
        JSONObject stationObject = lineObject.getJSONArray("stations").getJSONObject(station);
        return stationObject.getJSONArray("times");
    }

    public static List<Station> mapStationTimes(JSONArray jsonArray, LocalDateTime localDateTime) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Long[] stationTimes = mapper.readValue(jsonArray.toString(), Long[].class);

            return Arrays.stream(stationTimes)
                    .filter(x -> x >= localDateTime.toLocalTime().toSecondOfDay() - 180)
                    .map(x -> new Station(x, x - localDateTime.toLocalTime().toSecondOfDay()))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.diasjoao.metrosultejo.helpers;

import android.content.Context;

import com.diasjoao.metrosultejo.data.model.Season;
import com.diasjoao.metrosultejo.data.model.Station;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonUtils {

    public static String loadJSONFromAssets(Context context, String fileName) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static List<Station> parseStationListJson(String json, int seasonId, int dayId, int lineId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Season[] seasons = objectMapper.readValue(json, Season[].class);

            return Arrays.stream(seasons)
                    .filter(season -> season.getSeasonId() == seasonId)
                    .flatMap(season -> season.getDays().stream())
                    .filter(day -> day.getDayId() == dayId)
                    .flatMap(day -> day.getLines().stream())
                    .filter(line -> line.getLineId() == lineId)
                    .flatMap(line -> line.getStations().stream())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

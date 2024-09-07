package com.diasjoao.metrosultejo.data.repository;

import android.content.Context;

import com.diasjoao.metrosultejo.data.model.Season;
import com.diasjoao.metrosultejo.data.model.Station;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleRepository {

    private Context context;
    private final String FILENAME = "schedule.json";

    public ScheduleRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private InputStreamReader getJsonReader() throws IOException {
        InputStream is = context.getAssets().open(FILENAME);
        return new InputStreamReader(is, "UTF-8");
    }

    public List<Station> findStationsBySeasonAndDayAndLine(int seasonId, int dayId, int lineId) {
        try (InputStreamReader reader = getJsonReader()) {
            JsonElement element = JsonParser.parseReader(reader);
            Season[] seasons = new Gson().fromJson(element, Season[].class);

            return Arrays.stream(seasons)
                    .filter(season -> season.getSeasonId() == seasonId)
                    .flatMap(season -> season.getDays().stream())
                    .filter(day -> day.getDayId() == dayId)
                    .flatMap(day -> day.getLines().stream())
                    .filter(line -> line.getLineId() == lineId)
                    .flatMap(line -> line.getStations().stream())
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Station findStationBySeasonAndDayAndLineAndName(int seasonId, int dayId, int lineId, int stationId) {
        try (InputStreamReader reader = getJsonReader()) {
            JsonElement element = JsonParser.parseReader(reader);
            Season[] seasons = new Gson().fromJson(element, Season[].class);

            return Arrays.stream(seasons)
                    .filter(season -> season.getSeasonId() == seasonId)
                    .flatMap(season -> season.getDays().stream())
                    .filter(day -> day.getDayId() == dayId)
                    .flatMap(day -> day.getLines().stream())
                    .filter(line -> line.getLineId() == lineId)
                    .flatMap(line -> line.getStations().stream())
                    .skip(stationId)
                    .findFirst().orElse(null);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

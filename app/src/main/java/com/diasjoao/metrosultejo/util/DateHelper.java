package com.diasjoao.metrosultejo.util;

import android.content.Context;

import com.diasjoao.metrosultejo.R;

import org.json.JSONArray;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateHelper {

    public static final long ONE_MINUTE_IN_MILLIS = 60000;

    public static Long millisecondsToMinutes(Long milliseconds) {
        return milliseconds / (1000*60);
    }

    public static String minutesToString(Long minute) {
        return String.format("%1$3s", minute + "'");
    }

    public static Boolean isHoliday(Context context, LocalDate currentDate) {
        String currentDay = currentDate.get(ChronoField.DAY_OF_MONTH) +
                "-" + currentDate.get(ChronoField.MONTH_OF_YEAR);

        for (String holiday : context.getResources().getStringArray(R.array.feriados)) {
            if (holiday.equals(currentDay))
                return true;
        }

        return false;
    }

    public static List<LocalTime> convertStationTimesToDate(JSONArray stationTimes, int stationOffset) {
        List<LocalTime> result = new ArrayList<>();

        for (int i = 0; i < stationTimes.length(); i++) {
            try {
                result.add(LocalTime.parse(stationTimes.getString(i), DateTimeFormatter.ofPattern("H[H]:mm")).plus(stationOffset, ChronoUnit.MINUTES));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}

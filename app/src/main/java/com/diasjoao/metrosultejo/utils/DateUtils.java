package com.diasjoao.metrosultejo.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.List;

public class DateUtils {

    public static final long ONE_MINUTE_IN_MILLIS = 60000;

    private static final List<LocalDate> holidays = List.of(
            LocalDate.of(2000, 1,1), LocalDate.of(2000, 4,10),
            LocalDate.of(2000, 4,25), LocalDate.of(2000, 5,1),
            LocalDate.of(2000, 6,10), LocalDate.of(2000, 6,11),
            LocalDate.of(2000, 8,15), LocalDate.of(2000, 10,5),
            LocalDate.of(2000, 11,1), LocalDate.of(2000, 12,1),
            LocalDate.of(2000, 12,8), LocalDate.of(2000, 12,25)
    );

    public static String millisecondsToString(Long milliseconds){
        return String.format("%1$3s", (((milliseconds / (1000*60))) + "'"));
    }

    public static Boolean checkHoliday(Calendar date, List<String> feriados){
        String dia = String.format("%02d",date.get(Calendar.DAY_OF_MONTH)) +
                "-" + String.format("%02d", date.get(Calendar.MONTH));

        for (String feriado : feriados) {
            if (feriado.equals(dia))
                return true;
        }

        return false;
    }

    public static int getSeasonId(LocalDateTime localDateTime) {
        LocalDate beginDate = LocalDate.of(localDateTime.getYear(), Month.JULY, 14);
        LocalDate endDate = LocalDate.of(localDateTime.getYear(), Month.SEPTEMBER, 8);

        if (localDateTime.toLocalDate().isAfter(beginDate) && localDateTime.toLocalDate().isBefore(endDate)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static int getDayTypeId(LocalDateTime localDateTime) {
        if (localDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            return 1;
        }
        if (localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY) || isHoliday(localDateTime)) {
            return 2;
        }

        return 0;
    }

    private static Boolean isHoliday(LocalDateTime localDateTime) {
        for (LocalDate holiday : holidays) {
            if (localDateTime.getDayOfMonth() == holiday.getDayOfMonth() &&
                localDateTime.getMonthValue() == holiday.getMonthValue()) {
                return true;
            }
        }

        return false;
    }
}

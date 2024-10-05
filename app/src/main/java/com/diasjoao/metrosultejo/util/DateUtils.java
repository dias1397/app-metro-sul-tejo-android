package com.diasjoao.metrosultejo.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class DateUtils {

    private static final List<LocalDate> holidays = List.of(
            LocalDate.of(2000, 1,1), LocalDate.of(2000, 4,10),
            LocalDate.of(2000, 4,25), LocalDate.of(2000, 5,1),
            LocalDate.of(2000, 6,10), LocalDate.of(2000, 6,11),
            LocalDate.of(2000, 8,15), LocalDate.of(2000, 10,5),
            LocalDate.of(2000, 11,1), LocalDate.of(2000, 12,1),
            LocalDate.of(2000, 12,8), LocalDate.of(2000, 12,25)
    );


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
        if (DayOfWeek.SUNDAY.equals(localDateTime.getDayOfWeek()) || isHoliday(localDateTime)) {
            return 3;
        } else if (DayOfWeek.SATURDAY.equals(localDateTime.getDayOfWeek())) {
            return 2;
        } else {
            return 1;
        }
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

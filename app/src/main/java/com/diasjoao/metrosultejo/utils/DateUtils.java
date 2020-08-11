package com.diasjoao.metrosultejo.utils;

import java.util.Calendar;
import java.util.List;

public class DateUtils {

    public static final long ONE_MINUTE_IN_MILLIS = 60000;

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
}

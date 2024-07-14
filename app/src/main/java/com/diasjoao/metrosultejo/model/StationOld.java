package com.diasjoao.metrosultejo.model;

public class StationOld {

    private Long stopTime;
    private Long timeDifference;

    public StationOld(Long stopTime, Long timeDifference) {
        this.stopTime = stopTime;
        this.timeDifference = timeDifference;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public Long getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Long timeDifference) {
        this.timeDifference = timeDifference;
    }

    /*
    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = LocalTime.ofSecondOfDay(stopTime).plus(3, ChronoUnit.HOURS).toString();
    }

    public String getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Long timeDifference) {
        System.out.println(timeDifference);
        if (timeDifference < 3600 )
            this.timeDifference = String.format("%02d'", (LocalTime.ofSecondOfDay(timeDifference).getMinute()));
    }*/
}

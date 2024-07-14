package com.diasjoao.metrosultejo.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class Station {

    private String name;
    private List<String> times;

    public Station() {
    }

    @JsonCreator
    public Station(@JsonProperty("name") String name, @JsonProperty("times") List<String> times) {
        this.name = name;
        this.times = times;
    }

    public String getName() {
        return name;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<LocalDateTime> getConvertedTimes() {
        LocalDate today = LocalDate.now();
        return times.stream()
                .map(timeValue -> {
                    long totalSeconds = Long.parseLong(timeValue) + 10800;

                    long days = totalSeconds / (24 * 3600);
                    long remainingSeconds = totalSeconds % (24 * 3600);

                    LocalTime time = LocalTime.ofSecondOfDay(remainingSeconds);
                    LocalDate targetDate = today.plusDays(days);

                    return LocalDateTime.of(targetDate, time);
                })
                .collect(Collectors.toList());
    }
}

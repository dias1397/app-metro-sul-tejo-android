package com.diasjoao.metrosultejo.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Season {
    // seasonId = 1 Verão, 2 Inverno
    private int seasonId;
    private List<Day> days;

    public Season() {
    }

    @JsonCreator
    public Season(@JsonProperty("seasonId") int seasonId, @JsonProperty("days") List<Day> days) {
        this.seasonId = seasonId;
        this.days = days;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}

package com.diasjoao.metrosultejo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Day {

    private int dayId;
    private List<Line> lines;

    public Day() {
    }

    @JsonCreator
    public Day(@JsonProperty("dayId") int dayId, @JsonProperty("lines") List<Line> lines) {
        this.dayId = dayId;
        this.lines = lines;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}

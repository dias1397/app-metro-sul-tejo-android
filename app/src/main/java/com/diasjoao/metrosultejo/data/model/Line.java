package com.diasjoao.metrosultejo.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Line {

    private int lineId;
    private List<Station> stations;

    public Line() {
    }

    @JsonCreator
    public Line(@JsonProperty("lineId") int lineId, @JsonProperty("stations") List<Station> stations) {
        this.lineId = lineId;
        this.stations = stations;
    }

    public int getLineId() {
        return lineId;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
}

package com.diasjoao.metrosultejo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
}

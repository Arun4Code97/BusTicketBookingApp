package com.travels.bookmybus.dto;

import java.util.ArrayList;
import java.util.List;

public class StopSetupForm {
    private List<StopDto> stopDtos = new ArrayList<>();

    public List<StopDto> getStopDtos() {
        return stopDtos;
    }

    public void setStopDtos(List<StopDto> stopDtos) {
        this.stopDtos = stopDtos;
    }
}

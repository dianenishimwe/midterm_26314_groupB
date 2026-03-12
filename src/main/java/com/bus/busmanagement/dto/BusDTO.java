package com.bus.busmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusDTO {
    private String plateNumber;
    private String model;
    private Integer capacity;
    private String status;
    private Long routeId;
}

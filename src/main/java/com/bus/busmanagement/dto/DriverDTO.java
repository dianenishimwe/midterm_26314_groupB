package com.bus.busmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Long id;
    private Long userId;
    private Long busId;
    private String licenseNumber;
    private String userName;
    private String busPlateNumber;
}

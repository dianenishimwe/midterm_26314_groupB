package com.bus.busmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Long id;
    private Long userId;
    private Long routeId;
    private Long busId;
    private String ticketNumber;
    private String seatNumber;
    private Double price;
    private LocalDateTime bookingDate;
    private LocalDateTime travelDate;
    private String status;
    private String userName;
    private String routeNumber;
    private String busPlateNumber;
}

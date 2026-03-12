package com.bus.busmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Route route;
    
    @Column(nullable = false)
    private String seatNumber;
    
    @Column(nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    public enum BookingStatus {
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }
}

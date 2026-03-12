package com.bus.busmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String plateNumber;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BusStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Route route;
    
    public enum BusStatus {
        ACTIVE,
        MAINTENANCE,
        INACTIVE
    }
}

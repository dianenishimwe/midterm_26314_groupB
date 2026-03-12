package com.bus.busmanagement.config;

import com.bus.busmanagement.model.*;
import com.bus.busmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    
    private final RouteRepository routeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (routeRepository.count() > 0) {
            System.out.println("Data already loaded, skipping...");
            return;
        }
        
        System.out.println("Loading sample routes...");
        
        createRoute("RT001", "Kigali", "Huye", 125.5, 2.5);
        createRoute("RT002", "Kigali", "Musanze", 78.3, 1.5);
        
        System.out.println("Data loading completed!");
    }
    
    private Route createRoute(String routeNumber, String startLocation, 
                             String endLocation, Double distance, Double duration) {
        Route route = Route.builder()
            .routeNumber(routeNumber)
            .startLocation(startLocation)
            .endLocation(endLocation)
            .distance(distance)
            .estimatedDuration(duration)
            .build();
        return routeRepository.save(route);
    }
}

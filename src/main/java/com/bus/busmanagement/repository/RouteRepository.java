package com.bus.busmanagement.repository;

import com.bus.busmanagement.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Route Repository - Provides CRUD operations and custom query methods
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    /**
     * Find route by route number
     */
    Route findByRouteNumber(String routeNumber);
    
    /**
     * Check if route exists by route number
     */
    boolean existsByRouteNumber(String routeNumber);
    
    /**
     * Find all routes with pagination and sorting
     */
    Page<Route> findAll(Pageable pageable);
}

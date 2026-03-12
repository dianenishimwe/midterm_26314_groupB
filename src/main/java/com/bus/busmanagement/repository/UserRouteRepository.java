package com.bus.busmanagement.repository;

import com.bus.busmanagement.model.UserRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRoute Repository - Provides CRUD operations for the join entity
 */
@Repository
public interface UserRouteRepository extends JpaRepository<UserRoute, Long> {
    
    /**
     * Find all bookings for a specific user
     */
    List<UserRoute> findByUserId(Long userId);
    
    /**
     * Find all users who booked a specific route
     */
    List<UserRoute> findByRouteId(Long routeId);
}

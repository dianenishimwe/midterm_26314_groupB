package com.bus.busmanagement.service;

import com.bus.busmanagement.model.Route;
import com.bus.busmanagement.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Route Service - Business logic for route operations
 * 
 * EXAMINATION REQUIREMENT 3: Sorting and Pagination (5 Marks)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {
    
    private final RouteRepository routeRepository;
    
    /**
     * Save a new route
     */
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }
    
    /**
     * Get all routes with pagination and sorting
     * 
     * How Sorting is implemented:
     * 1. Using Spring Data JPA's Sort class
     * 2. Sort.by("fieldName") creates sort configuration
     * 3. Can specify ascending or descending order
     * 4. Applied to the query automatically by repository
     * 
     * How Pagination works:
     * 1. PageRequest.of(page, size) creates pagination object
     * 2. Repository method returns Page<T> instead of List<T>
     * 3. Spring Data JPA executes two queries:
     *    - COUNT query to get total number of records
     *    - SELECT query with LIMIT and OFFSET for current page
     * 4. Page object wraps the content and provides metadata
     * 
     * @param page page number (0-based index)
     * @param size number of items per page
     * @param sortBy field to sort by
     * @param ascending true for ascending, false for descending
     * @return Page containing routes and pagination metadata
     */
    @Transactional(readOnly = true)
    public Page<Route> getAllRoutesPaginated(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return routeRepository.findAll(PageRequest.of(page, size, sort));
    }
    
    /**
     * Get all routes sorted by a specific field
     */
    @Transactional(readOnly = true)
    public List<Route> getAllRoutesSorted(String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return routeRepository.findAll(sort);
    }
    
    /**
     * Get all routes
     */
    @Transactional(readOnly = true)
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
    
    /**
     * Get route by ID
     */
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }
    
    /**
     * Get route by route number
     */
    @Transactional(readOnly = true)
    public Route getRouteByRouteNumber(String routeNumber) {
        return routeRepository.findByRouteNumber(routeNumber);
    }
    
    /**
     * Check if route exists
     */
    @Transactional(readOnly = true)
    public boolean existsByRouteNumber(String routeNumber) {
        return routeRepository.existsByRouteNumber(routeNumber);
    }
}

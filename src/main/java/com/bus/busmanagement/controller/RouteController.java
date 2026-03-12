package com.bus.busmanagement.controller;

import com.bus.busmanagement.model.Route;
import com.bus.busmanagement.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Route Controller - REST API endpoints for route operations
 * 
 * EXAMINATION REQUIREMENT 3: Sorting and Pagination (5 Marks)
 */
@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RouteController {
    
    private final RouteService routeService;
    
    /**
     * GET /api/routes - Get all routes
     */
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }
    
    /**
     * GET /api/routes/{id} - Get route by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/routes - Create new route
     */
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route savedRoute = routeService.saveRoute(route);
        return ResponseEntity.ok(savedRoute);
    }
    
    /**
     * EXAMINATION REQUIREMENT 3: Sorting and Pagination (5 Marks)
     * 
     * GET /api/routes/paginated?page=0&size=10&sortBy=routeNumber&ascending=true
     * 
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
     * Performance Benefits:
     * - Reduces memory usage by loading only a subset of data
     * - Improves response time for large datasets
     * - Enables lazy loading in UI applications
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<Route>> getRoutesPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "routeNumber") String sortBy,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Page<Route> routePage = routeService.getAllRoutesPaginated(page, size, sortBy, ascending);
        return ResponseEntity.ok(routePage);
    }
    
    /**
     * GET /api/routes/sorted?sortBy=routeNumber&ascending=true - Get routes with sorting
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<Route>> getRoutesSorted(
        @RequestParam(defaultValue = "routeNumber") String sortBy,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        List<Route> routes = routeService.getAllRoutesSorted(sortBy, ascending);
        return ResponseEntity.ok(routes);
    }
}

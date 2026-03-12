package com.bus.busmanagement.controller;

import com.bus.busmanagement.dto.BusDTO;
import com.bus.busmanagement.model.Bus;
import com.bus.busmanagement.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bus Controller - REST API endpoints for bus operations
 */
@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BusController {
    
    private final BusService busService;
    
    /**
     * POST /api/buses - Create a new bus
     */
    @PostMapping
    public ResponseEntity<Bus> createBus(@RequestBody BusDTO busDTO) {
        Bus savedBus = busService.createBus(busDTO);
        return ResponseEntity.ok(savedBus);
    }
    
    /**
     * GET /api/buses - Get all buses
     */
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }
    
    /**
     * GET /api/buses/{id} - Get bus by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        try {
            Bus bus = busService.getBusById(id);
            return ResponseEntity.ok(bus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/buses/exists/plate/{plateNumber} - Check if bus exists by plate number
     */
    @GetMapping("/exists/plate/{plateNumber}")
    public ResponseEntity<Map<String, Boolean>> checkBusExistsByPlate(@PathVariable String plateNumber) {
        boolean exists = busService.existsByPlateNumber(plateNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/buses/status/{status} - Get buses by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Bus>> getBusesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(busService.getBusesByStatus(status));
    }
    
    /**
     * GET /api/buses/route/{routeId} - Get buses by route
     */
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Bus>> getBusesByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(busService.getBusesByRoute(routeId));
    }
    
    /**
     * GET /api/buses/paginated - Get buses with pagination and sorting
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<Bus>> getBusesPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "plateNumber") String sortBy,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Page<Bus> busPage = busService.getAllBusesPaginated(page, size, sortBy, ascending);
        return ResponseEntity.ok(busPage);
    }
    
    /**
     * PUT /api/buses/{id} - Update bus
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody BusDTO busDTO) {
        Bus updatedBus = busService.updateBus(id, busDTO);
        return ResponseEntity.ok(updatedBus);
    }
    
    /**
     * DELETE /api/buses/{id} - Delete bus
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
}

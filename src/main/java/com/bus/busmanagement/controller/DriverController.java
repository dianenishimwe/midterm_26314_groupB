package com.bus.busmanagement.controller;

import com.bus.busmanagement.dto.DriverDTO;
import com.bus.busmanagement.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestParam Long userId, @RequestParam String licenseNumber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(userId, licenseNumber));
    }

    @PutMapping("/{driverId}/assign-bus/{busId}")
    public ResponseEntity<DriverDTO> assignBus(@PathVariable Long driverId, @PathVariable Long busId) {
        return ResponseEntity.ok(driverService.assignBus(driverId, busId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DriverDTO> getDriverByLicenseNumber(@PathVariable String licenseNumber) {
        return ResponseEntity.ok(driverService.getDriverByLicenseNumber(licenseNumber));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverDTO> getDriverByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(driverService.getDriverByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Long id, @RequestParam String licenseNumber) {
        return ResponseEntity.ok(driverService.updateDriver(id, licenseNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}

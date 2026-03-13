package com.bus.busmanagement.controller;

import com.bus.busmanagement.dto.DriverDTO;
import com.bus.busmanagement.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestParam @NonNull Long userId, @RequestParam String licenseNumber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(Objects.requireNonNull(userId), licenseNumber));
    }

    @PutMapping("/{driverId}/assign-bus/{busId}")
    public ResponseEntity<DriverDTO> assignBus(@PathVariable @NonNull Long driverId, @PathVariable @NonNull Long busId) {
        return ResponseEntity.ok(driverService.assignBus(Objects.requireNonNull(driverId), Objects.requireNonNull(busId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(driverService.getDriverById(Objects.requireNonNull(id)));
    }

    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DriverDTO> getDriverByLicenseNumber(@PathVariable String licenseNumber) {
        return ResponseEntity.ok(driverService.getDriverByLicenseNumber(licenseNumber));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverDTO> getDriverByUserId(@PathVariable @NonNull Long userId) {
        return ResponseEntity.ok(driverService.getDriverByUserId(Objects.requireNonNull(userId)));
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable @NonNull Long id, @RequestParam String licenseNumber) {
        return ResponseEntity.ok(driverService.updateDriver(Objects.requireNonNull(id), licenseNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable @NonNull Long id) {
        driverService.deleteDriver(Objects.requireNonNull(id));
        return ResponseEntity.noContent().build();
    }
}
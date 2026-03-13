package com.bus.busmanagement.service;

import com.bus.busmanagement.dto.DriverDTO;
import com.bus.busmanagement.model.Bus;
import com.bus.busmanagement.model.Driver;
import com.bus.busmanagement.model.User;
import com.bus.busmanagement.repository.BusRepository;
import com.bus.busmanagement.repository.DriverRepository;
import com.bus.busmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final BusRepository busRepository;

    public DriverDTO createDriver(@NonNull Long userId, String licenseNumber) {
        User user = userRepository.findById(Objects.requireNonNull(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Driver driver = new Driver();
        driver.setUser(user);
        driver.setLicenseNumber(licenseNumber);

        Driver saved = driverRepository.save(driver);
        return convertToDTO(saved);
    }

    public DriverDTO assignBus(@NonNull Long driverId, @NonNull Long busId) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(driverId))
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        Bus bus = busRepository.findById(Objects.requireNonNull(busId))
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        driver.setBus(bus);
        Driver updated = driverRepository.save(driver);
        return convertToDTO(updated);
    }

    public DriverDTO getDriverById(@NonNull Long id) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return convertToDTO(driver);
    }

    public DriverDTO getDriverByLicenseNumber(String licenseNumber) {
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return convertToDTO(driver);
    }

    public DriverDTO getDriverByUserId(@NonNull Long userId) {
        Driver driver = driverRepository.findByUserId(Objects.requireNonNull(userId))
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return convertToDTO(driver);
    }

    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DriverDTO updateDriver(@NonNull Long id, String licenseNumber) {
        Driver driver = driverRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setLicenseNumber(licenseNumber);
        Driver updated = driverRepository.save(driver);
        return convertToDTO(updated);
    }

    public void deleteDriver(@NonNull Long id) {
        driverRepository.deleteById(Objects.requireNonNull(id));
    }

    private DriverDTO convertToDTO(Driver driver) {
        DriverDTO dto = new DriverDTO();
        dto.setId(driver.getId());
        dto.setUserId(driver.getUser().getId());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setUserName(driver.getUser().getFullName());
        if (driver.getBus() != null) {
            dto.setBusId(driver.getBus().getId());
            dto.setBusPlateNumber(driver.getBus().getPlateNumber());
        }
        return dto;
    }
}
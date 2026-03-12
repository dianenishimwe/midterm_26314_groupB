package com.bus.busmanagement.service;

import com.bus.busmanagement.dto.BusDTO;
import com.bus.busmanagement.model.Bus;
import com.bus.busmanagement.model.Route;
import com.bus.busmanagement.repository.BusRepository;
import com.bus.busmanagement.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BusService {
    
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    
    public Bus createBus(BusDTO busDTO) {
        Route route = null;
        if (busDTO.getRouteId() != null) {
            route = routeRepository.findById(busDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + busDTO.getRouteId()));
        }
        
        Bus bus = Bus.builder()
            .plateNumber(busDTO.getPlateNumber())
            .model(busDTO.getModel())
            .capacity(busDTO.getCapacity())
            .status(Bus.BusStatus.valueOf(busDTO.getStatus()))
            .route(route)
            .build();
        
        return busRepository.save(bus);
    }
    
    @Transactional(readOnly = true)
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Bus getBusById(Long id) {
        return busRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public boolean existsByPlateNumber(String plateNumber) {
        return busRepository.existsByPlateNumber(plateNumber);
    }
    
    @Transactional(readOnly = true)
    public List<Bus> getBusesByStatus(String status) {
        return busRepository.findByStatus(Bus.BusStatus.valueOf(status));
    }
    
    @Transactional(readOnly = true)
    public List<Bus> getBusesByRoute(Long routeId) {
        return busRepository.findByRouteId(routeId);
    }
    
    @Transactional(readOnly = true)
    public Page<Bus> getAllBusesPaginated(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return busRepository.findAll(pageable);
    }
    
    public Bus updateBus(Long id, BusDTO busDTO) {
        Bus bus = getBusById(id);
        
        if (busDTO.getPlateNumber() != null) {
            bus.setPlateNumber(busDTO.getPlateNumber());
        }
        if (busDTO.getModel() != null) {
            bus.setModel(busDTO.getModel());
        }
        if (busDTO.getCapacity() != null) {
            bus.setCapacity(busDTO.getCapacity());
        }
        if (busDTO.getStatus() != null) {
            bus.setStatus(Bus.BusStatus.valueOf(busDTO.getStatus()));
        }
        if (busDTO.getRouteId() != null) {
            Route route = routeRepository.findById(busDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));
            bus.setRoute(route);
        }
        
        return busRepository.save(bus);
    }
    
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
}

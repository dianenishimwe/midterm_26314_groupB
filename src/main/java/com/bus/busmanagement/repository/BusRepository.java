package com.bus.busmanagement.repository;

import com.bus.busmanagement.model.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    Optional<Bus> findByPlateNumber(String plateNumber);
    
    boolean existsByPlateNumber(String plateNumber);
    
    List<Bus> findByStatus(Bus.BusStatus status);
    
    List<Bus> findByRouteId(Long routeId);

    @Override
    @NonNull
    Page<Bus> findAll(@NonNull Pageable pageable);
}
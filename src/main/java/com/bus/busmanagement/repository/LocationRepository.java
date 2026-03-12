package com.bus.busmanagement.repository;

import com.bus.busmanagement.model.Location;
import com.bus.busmanagement.model.LocationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    
    boolean existsByCode(String code);
    boolean existsByName(String name);

    Optional<Location> findByCode(String code);
    Optional<Location> findByName(String name);

    List<Location> findByLocationType(LocationType locationType);
    Page<Location> findByLocationType(LocationType locationType, Pageable pageable);

    List<Location> findByParent(Location parent);
    Page<Location> findByParent(Location parent, Pageable pageable);
    
    List<Location> findByLocationTypeAndParent(LocationType locationType, Location parent);

    List<Location> findByParentIsNull();

    long countByLocationType(LocationType locationType);
    long countByParent(Location parent);
}
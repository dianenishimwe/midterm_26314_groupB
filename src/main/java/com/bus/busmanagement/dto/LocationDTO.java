package com.bus.busmanagement.dto;

import com.bus.busmanagement.model.LocationType;
import java.util.UUID;

public class LocationDTO {
    private UUID id;
    private String code;
    private String name;
    private LocationType locationType;
    private String description;
    private UUID parentId;

    public LocationDTO() {}

    public LocationDTO(UUID id, String code, String name, LocationType locationType, String description, UUID parentId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.locationType = locationType;
        this.description = description;
        this.parentId = parentId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}

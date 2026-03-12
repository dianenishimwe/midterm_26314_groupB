package com.bus.busmanagement.service;

import com.bus.busmanagement.model.Location;
import com.bus.busmanagement.model.LocationType;
import com.bus.busmanagement.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class LocationDataLoader implements CommandLineRunner {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (locationRepository.count() == 0) {
            loadLocationData();
        }
    }
    
    private void loadLocationData() {
        // Provinces
        Location kigali = createLocation("b0b8b60a-98a6-4f2c-95ea-80f2d84ed7bb", null, "KG", LocationType.PROVINCE, "Kigali City", "Capital city of Rwanda");
        Location northern = createLocation("2b965328-a43d-4af7-80c0-b153c9fb1897", null, "NT", LocationType.PROVINCE, "Northern Province", "Northern Province of Rwanda");
        Location eastern = createLocation("a42a2d38-aa8a-447d-a21f-e34ccf057124", null, "ET", LocationType.PROVINCE, "Eastern Province", "Eastern Province of Rwanda");
        Location southern = createLocation("eb8d4394-e2b5-4bc0-baf2-c54c23918b1d", null, "ST", LocationType.PROVINCE, "Southern Province", "Southern Province of Rwanda");
        
        // Districts
        Location gasabo = createLocation("34e1366e-7e5c-4fd8-bf6e-db92c1368c2e", kigali, "KG-GAS", LocationType.DISTRICT, "Gasabo", "District in Kigali");
        createLocation("8df09372-5cee-48d0-bd59-ca9c001265e9", kigali, "KG-NYA", LocationType.DISTRICT, "Nyarugenge", "District in Kigali");
        createLocation("a33cf8fe-a3b3-4528-a789-b47c5bad6ea8", kigali, "KG-KIC", LocationType.DISTRICT, "Kicukiro", "District in Kigali");
        Location musanze = createLocation("82e5527c-01f8-4354-93a5-4ad55821d9df", northern, "NT-MUS", LocationType.DISTRICT, "Musanze", "District in Northern Province");
        Location kayonza = createLocation("2d69ec19-93c2-487a-9726-a8cbc2cb0089", eastern, "ET-KAY", LocationType.DISTRICT, "Kayonza", "District in Eastern Province");
        Location huye = createLocation("4b94b5c4-9d1b-4627-9ffa-801798d76de1", southern, "ST-HUY", LocationType.DISTRICT, "Huye", "District in Southern Province");
        
        // Sectors
        Location remera = createLocation("4223ec3d-c26e-4bf3-928e-23933ccfa455", gasabo, "KG-GAS-REM", LocationType.SECTOR, "Remera", "Sector in Gasabo");
        createLocation("9bb9b3ba-a5a7-4774-b434-b709215ae1b3", gasabo, "KG-GAS-KAC", LocationType.SECTOR, "Kacyiru", "Sector in Gasabo");
        Location kinigi = createLocation("bc5c768f-a89b-4873-8657-bf06d2827eb0", musanze, "NT-MUS-KIN", LocationType.SECTOR, "Kinigi", "Sector in Musanze");
        Location mukarange = createLocation("8207517f-1bf1-42e3-92eb-6b077af74ee5", kayonza, "ET-KAY-MUK", LocationType.SECTOR, "Mukarange", "Sector in Kayonza");
        Location tumba = createLocation("b24ddbbe-815d-49a5-945c-45c44f6492d2", huye, "ST-HUY-TUM", LocationType.SECTOR, "Tumba", "Sector in Huye");
        
        // Cells
        Location gisimenti = createLocation("2fd6870e-8391-49d8-bdf5-ec4a2466580c", remera, "KG-GAS-REM-GIS", LocationType.CELL, "Gisimenti", "Cell in Remera");
        Location nyange = createLocation("9f0c591c-75f9-4626-9907-21d0c0ecc339", kinigi, "NT-MUS-KIN-NYA", LocationType.CELL, "Nyange", "Cell in Kinigi");
        Location nyagatovu = createLocation("f15f2624-d9fe-411b-96ea-5f0395c84eb3", mukarange, "ET-KAY-MUK-NYA", LocationType.CELL, "Nyagatovu", "Cell in Mukarange");
        Location cyarwa = createLocation("278cda3d-e030-4bb5-a0ac-a38a6afa8ac7", tumba, "ST-HUY-TUM-CYA", LocationType.CELL, "Cyarwa", "Cell in Tumba");
        
        // Villages
        createLocation("cc848825-4545-4727-b6fd-0edd8e961a8c", gisimenti, "KG-GAS-REM-GIS-V1", LocationType.VILLAGE, "Kigali Village 1", "Village in Gisimenti");
        createLocation("73f22402-d8c1-4b0a-99fd-02998a1c9f6f", nyange, "NT-MUS-KIN-NYA-NYA", LocationType.VILLAGE, "Nyange", "Village in Nyange");
        createLocation("7f40a251-4528-43c4-a476-a09accbf858f", nyagatovu, "ET-KAY-MUK-NYA-KIZ", LocationType.VILLAGE, "Kazirabwayi", "Village in Nyagatovu");
        createLocation("b39cc929-0698-40a4-9c3f-582983a8fbfa", cyarwa, "ST-HUY-TUM-CYA-V1", LocationType.VILLAGE, "Agahora", "Village in Cyarwa");
        
        System.out.println("Location data loaded successfully!");
    }
    
    private Location createLocation(String id, Location parent, String code, LocationType type, String name, String description) {
        Location location = new Location();
        // Don't set ID manually - let JPA generate it
        location.setParent(parent);
        location.setCode(code);
        location.setLocationType(type);
        location.setName(name);
        location.setDescription(description);
        return locationRepository.save(location);
    }
}
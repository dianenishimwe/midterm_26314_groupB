package com.bus.busmanagement.controller;

import com.bus.busmanagement.model.UserRoute;
import com.bus.busmanagement.service.UserRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user-routes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRouteController {

    private final UserRouteService userRouteService;

    @PostMapping
    public ResponseEntity<UserRoute> createUserRoute(@RequestBody UserRoute userRoute) {
        UserRoute savedUserRoute = userRouteService.saveUserRoute(userRoute);
        return ResponseEntity.ok(savedUserRoute);
    }

    @GetMapping
    public ResponseEntity<List<UserRoute>> getAllUserRoutes() {
        return ResponseEntity.ok(userRouteService.getAllUserRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoute> getUserRouteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userRouteService.getUserRouteById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoute>> getUserRoutesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userRouteService.getUserRoutesByUserId(userId));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<UserRoute>> getUserRoutesByRouteId(@PathVariable Long routeId) {
        return ResponseEntity.ok(userRouteService.getUserRoutesByRouteId(routeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRoute(@PathVariable Long id) {
        try {
            userRouteService.deleteUserRoute(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

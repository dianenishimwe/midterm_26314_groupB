package com.bus.busmanagement.service;

import com.bus.busmanagement.model.UserRoute;
import com.bus.busmanagement.repository.UserRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRouteService {

    private final UserRouteRepository userRouteRepository;

    public UserRoute saveUserRoute(UserRoute userRoute) {
        return userRouteRepository.save(userRoute);
    }

    @Transactional(readOnly = true)
    public List<UserRoute> getAllUserRoutes() {
        return userRouteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserRoute getUserRouteById(Long id) {
        return userRouteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("UserRoute not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserRoute> getUserRoutesByUserId(Long userId) {
        return userRouteRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<UserRoute> getUserRoutesByRouteId(Long routeId) {
        return userRouteRepository.findByRouteId(routeId);
    }

    public void deleteUserRoute(Long id) {
        userRouteRepository.deleteById(id);
    }
}

package com.bus.busmanagement.service;

import com.bus.busmanagement.dto.TicketDTO;
import com.bus.busmanagement.model.Ticket;
import com.bus.busmanagement.model.User;
import com.bus.busmanagement.model.Route;
import com.bus.busmanagement.model.Bus;
import com.bus.busmanagement.repository.TicketRepository;
import com.bus.busmanagement.repository.UserRepository;
import com.bus.busmanagement.repository.RouteRepository;
import com.bus.busmanagement.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    public Ticket createTicket(TicketDTO ticketDTO) {
        User user = userRepository.findById(ticketDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Route route = routeRepository.findById(ticketDTO.getRouteId())
            .orElseThrow(() -> new RuntimeException("Route not found"));
        
        Bus bus = ticketDTO.getBusId() != null ? 
            busRepository.findById(ticketDTO.getBusId()).orElse(null) : null;

        String ticketNumber = "TKT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Ticket ticket = Ticket.builder()
            .user(user)
            .route(route)
            .bus(bus)
            .ticketNumber(ticketNumber)
            .seatNumber(ticketDTO.getSeatNumber())
            .price(ticketDTO.getPrice())
            .bookingDate(LocalDateTime.now())
            .travelDate(ticketDTO.getTravelDate())
            .status(Ticket.TicketStatus.BOOKED)
            .build();

        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Transactional(readOnly = true)
    public Ticket getTicketByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByRouteId(Long routeId) {
        return ticketRepository.findByRouteId(routeId);
    }

    public Ticket updateTicketStatus(Long id, String status) {
        Ticket ticket = getTicketById(id);
        ticket.setStatus(Ticket.TicketStatus.valueOf(status));
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}

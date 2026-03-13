package com.bus.busmanagement.repository;

import com.bus.busmanagement.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByRouteId(Long routeId);
    List<Ticket> findByBusId(Long busId);
    List<Ticket> findByStatus(Ticket.TicketStatus status);
}

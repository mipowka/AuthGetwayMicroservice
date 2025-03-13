package org.example.ticketservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ticketservice.entity.Ticket;
import org.example.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Optional<Ticket> updateTicket(Long id, Ticket ticketDetails) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTicketNumber(ticketDetails.getTicketNumber());
            ticket.setSeatNumber(ticketDetails.getSeatNumber());
            return ticketRepository.save(ticket);
        });
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}

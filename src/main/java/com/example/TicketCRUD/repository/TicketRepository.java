package com.example.TicketCRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TicketCRUD.entities.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}

package com.example.TicketCRUD;

import com.example.TicketCRUD.entities.Ticket;
import com.example.TicketCRUD.repository.TicketRepository;
import com.example.TicketCRUD.services.TicketService;
import com.example.TicketCRUD.vos.TicketVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class TicketCRUDApplicationTests {

	@Mock
	private TicketRepository ticketRepository;

	private TicketService ticketService;

	@BeforeEach
	void setUp() {
		ticketService = new TicketService(ticketRepository);
	}

	@Test
	void testGetAllTickets() {
		List<Ticket> ticketList = new ArrayList<>();
		ticketList.add(new Ticket(1L, "Ticket 1", "Description 1", "Admin", new Timestamp(System.currentTimeMillis()), null, null));
		ticketList.add(new Ticket(2L, "Ticket 2", "Description 2", "Admin", new Timestamp(System.currentTimeMillis()), null, null));

		Mockito.when(ticketRepository.findAll()).thenReturn(ticketList);

		List<TicketVo> ticketVoList = ticketService.getAllTickets();
		Assertions.assertEquals(2, ticketVoList.size());
	}

	@Test
	void testCreateTicket() {
		TicketVo ticketVo = TicketVo.builder()
				.ticketName("Ticket 1")
				.ticketDesc("Description 1")
				.build();

		Ticket ticket = Ticket.builder()
				.ticketName("Ticket 1")
				.ticketDesc("Description 1")
				.createdBy("Admin")
				.createdOn(new Timestamp(System.currentTimeMillis()))
				.build();

		Mockito.when(ticketRepository.save(ArgumentMatchers.any(Ticket.class))).thenReturn(ticket);

		String result = ticketService.createTicket(ticketVo);
		Assertions.assertEquals("Created ticket successfully", result);
	}

	@Test
	void testViewTicket() {
		Ticket ticket = new Ticket(1L, "Ticket 1", "Description 1", "Admin", new Timestamp(System.currentTimeMillis()), null, null);

		Mockito.when(ticketRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(ticket));

		TicketVo ticketVo = ticketService.viewTicket(1L);
		Assertions.assertEquals("Ticket 1", ticketVo.getTicketName());
		Assertions.assertEquals("Description 1", ticketVo.getTicketDesc());
	}

	@Test
	void testUpdateTicket() {
		Ticket ticket = new Ticket(1L, "Ticket 1", "Description 1", "Admin", new Timestamp(System.currentTimeMillis()), null, null);

		Mockito.when(ticketRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(ticket));
		Mockito.when(ticketRepository.save(ArgumentMatchers.any(Ticket.class))).thenReturn(ticket);

		String result = ticketService.updateTicket(1L, "Updated Ticket", "Updated Description");
		Assertions.assertEquals("Updated ticket successfully", result);
	}

	@Test
	void testDeleteTicket() {
		Mockito.when(ticketRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(new Ticket()));

		String result = ticketService.deleteTicket(1L);
		Assertions.assertEquals("Deleted Successfully", result);
	}
}

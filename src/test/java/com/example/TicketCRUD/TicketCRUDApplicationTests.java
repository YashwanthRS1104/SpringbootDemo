package com.example.TicketCRUD;

import com.example.TicketCRUD.entities.Ticket;
import com.example.TicketCRUD.repository.TicketRepository;
import com.example.TicketCRUD.services.TicketService;
import com.example.TicketCRUD.vos.TicketVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TicketCRUDApplicationTests {

	@Mock
	private TicketRepository ticketRepository;

	@Mock
	private CacheManager cacheManager;

	@Mock
	private Cache cache;

//	@InjectMocks
	private TicketService ticketService;


	@BeforeEach
	void setUp() {
		ticketService = new TicketService(ticketRepository, cacheManager);
	}

	@Test
	public void testGetTicketById_CacheHit() {
		Long id = 1L;
		TicketVo ticketVo = TicketVo.builder()
				.ticketName("Ticket 1")
				.ticketDesc("Description 1")
				.build();

		Cache.ValueWrapper valueWrapper = mock(Cache.ValueWrapper.class);

		when(cacheManager.getCache("ticket")).thenReturn(cache);
		when(cache.get(id)).thenReturn(valueWrapper);
		when(valueWrapper.get()).thenReturn(ticketVo);

		verify(ticketRepository, never()).findById(id);
//		System.out.println(((TicketVo)cacheManager.getCache("ticket").get(1L).get()).getTicketName());
		TicketVo result = ticketService.viewTicket(id);

		assertEquals(ticketVo, result);
	}

	@Test
	public void testGetTicketById_CacheMiss() {
		Long id = 1L;
		TicketVo ticketVo = TicketVo.builder()
				.ticketName("Ticket 1")
				.ticketDesc("Description 1")
				.build();

		when(cacheManager.getCache("ticket")).thenReturn(cache);
		when(cache.get(id)).thenReturn(() -> null);

		TicketVo result = ticketService.viewTicket(id);

		assertEquals(ticketVo, result);
		verify(ticketRepository, times(1)).findById(id);
	}

	@Test
	void testGetAllTickets() {
		List<Ticket> ticketList = new ArrayList<>();
		ticketList.add(new Ticket(1L, "Ticket 1", "Description 1", "Admin", new Timestamp(System.currentTimeMillis()), null, null));
		ticketList.add(new Ticket(2L, "Ticket 2", "Description 2", "Admin", new Timestamp(System.currentTimeMillis()), null, null));

		when(ticketRepository.findAll()).thenReturn(ticketList);

		List<TicketVo> ticketVoList = ticketService.getAllTickets();
		assertEquals(2, ticketVoList.size());
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

		when(ticketRepository.save(ArgumentMatchers.any(Ticket.class))).thenReturn(ticket);

		String result = ticketService.createTicket(ticketVo);
		assertEquals("Created ticket successfully", result);
	}

	@Test
	void testViewTicket() {
		Ticket ticket = new Ticket(1L, "Ticket 1", "Description 1", "Admin", new Timestamp(System.currentTimeMillis()), null, null);

		when(ticketRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(ticket));

		TicketVo ticketVo = ticketService.viewTicket(1L);
		assertEquals("Ticket 1", ticketVo.getTicketName());
		assertEquals("Description 1", ticketVo.getTicketDesc());
	}

	@Test
	void testUpdateTicket() {
		Ticket ticket = new Ticket(1L, "Ticket 1", "Description 1", "Admin", new Timestamp(System.currentTimeMillis()), null, null);

		when(ticketRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(ticket));
		when(ticketRepository.save(ArgumentMatchers.any(Ticket.class))).thenReturn(ticket);

		String result = ticketService.updateTicket(1L, "Updated Ticket", "Updated Description");
		assertEquals("Updated ticket successfully", result);
	}

	@Test
	void testDeleteTicket() {
		when(ticketRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(new Ticket()));

		String result = ticketService.deleteTicket(1L);
		assertEquals("Deleted Successfully", result);
	}
}

package com.example.TicketCRUD.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.TicketCRUD.entities.Ticket;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.TicketCRUD.repository.TicketRepository;
import com.example.TicketCRUD.vos.TicketVo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

	private TicketRepository ticketRepository;

	public TicketService(TicketRepository ticketRepository){
		this.ticketRepository = ticketRepository;
	}

	private Ticket voToTicket(TicketVo vo) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return Ticket.builder()
				.ticketName(vo.getTicketName())
				.ticketDesc(vo.getTicketDesc())
				.createdBy("Admin")
				.createdOn(timestamp)
				.build();
	}

	private TicketVo ticketToVo(Ticket ticket) {
		return TicketVo.builder()
				.ticketName(ticket.getTicketName())
				.ticketDesc(ticket.getTicketDesc())
				.build();
	}

	@Transactional
	private Ticket updateTicket(Ticket ticket, String newName, String newDesc) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return Ticket.builder()
				.id(ticket.getId())
				.ticketName(newName)
				.ticketDesc(newDesc)
				.createdBy(ticket.getCreatedBy())
				.createdOn(ticket.getCreatedOn())
				.modifiedBy("Admin")
				.modifiedOn(timestamp)
				.build();
	}

	@Cacheable(value = "tickets")
	@Transactional
	public List<TicketVo> getAllTickets() {
		List<Ticket> ticketList = ticketRepository.findAll();
		List<TicketVo> ticketVoList = new ArrayList<>();
		for (Ticket vo : ticketList)
			ticketVoList.add(ticketToVo(vo));
		return ticketVoList;
	}

	@CacheEvict(value = "tickets", allEntries = true)
	@Transactional
	public String createTicket(TicketVo vo) {
		Ticket ticket = voToTicket(vo);
		ticketRepository.save(ticket);
		return "Created ticket successfully";
	}

	@Cacheable(value = "ticket", key = "#id")
	@Transactional
	public TicketVo viewTicket(Long id) {
		Optional<Ticket> optionalTicket = ticketRepository.findById(id);
		Ticket ticket = null;

		if(optionalTicket.isPresent())
			ticket = optionalTicket.get();

		if(ticket == null) return null;
		return ticketToVo(ticket);
	}


	@Caching(evict = {
			@CacheEvict(value = "tickets", allEntries = true),
			@CacheEvict(value = "ticket", key = "#id")
	})
	@Transactional
	public String updateTicket(Long id, String newName, String newDesc) {
		Optional<Ticket> optionalTicket = ticketRepository.findById(id);
		Ticket ticket = null;

		if(optionalTicket.isPresent())
			ticket = optionalTicket.get();

		if(ticket == null)
			return "Could not find the ticket";

		Ticket updatedTicket = updateTicket(ticket, newName, newDesc);

		ticketRepository.save(updatedTicket);
		return "Updated ticket successfully";
	}

	@Caching(evict = {
			@CacheEvict(value = "tickets", allEntries = true),
			@CacheEvict(value = "ticket", key = "#id")
	})
	@Transactional
	public String deleteTicket(Long id) {
		Optional<Ticket> optionalTicket = ticketRepository.findById(id);
		Ticket ticket = null;

		if(optionalTicket.isPresent())
			ticket = optionalTicket.get();

		if(ticket == null)
			return "Could not find ticket";

		ticketRepository.deleteById(id);

		return "Deleted Successfully";
	}
}

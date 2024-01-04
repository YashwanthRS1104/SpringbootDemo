package com.example.TicketCRUD.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.TicketCRUD.entities.Ticket;
import org.springframework.stereotype.Service;

import com.example.TicketCRUD.repository.TicketRepository;
import com.example.TicketCRUD.vos.TicketVo;

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

	public List<TicketVo> getAllTickets() {
		List<Ticket> ticketList = ticketRepository.findAll();
		List<TicketVo> ticketVoList = new ArrayList<>();
		for (Ticket vo : ticketList)
			ticketVoList.add(ticketToVo(vo));
		return ticketVoList;
	}

	public String createTicket(TicketVo vo) {
		Ticket ticket = voToTicket(vo);
		ticketRepository.save(ticket);
		return "Created ticket succesfully";
	}

	public TicketVo viewTicket(Long id) {
		Ticket ticket = ticketRepository.findById(id).get();
		if(ticket == null) return null;
		return ticketToVo(ticket);
	}

	public String updateTicket(Long id, String newName, String newDesc) {
		Ticket ticket = ticketRepository.findById(id).get();
		if(ticket == null) return null;

		Ticket updatedTicket = updateTicket(ticket, newName, newDesc);

		ticketRepository.save(updatedTicket);
		return "Updated ticket succesfully";
	}

	public String deleteTicket(Long id) {
		Ticket ticket = ticketRepository.findById(id).get();
		if(ticket == null)
			return "Could not find ticket";

		ticketRepository.deleteById(id);

		return "Deleted Succesfully";
	}
}

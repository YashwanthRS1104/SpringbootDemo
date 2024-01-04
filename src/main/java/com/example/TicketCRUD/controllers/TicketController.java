package com.example.TicketCRUD.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.TicketCRUD.services.TicketService;
import com.example.TicketCRUD.vos.TicketVo;

@RestController
@RequestMapping("/api")
public class TicketController {

	private TicketService ticketService;

	public TicketController (TicketService ticketService){
		this.ticketService = ticketService;
	}

	@GetMapping("tickets")
	public List<TicketVo> getAllTickets() {
		return ticketService.getAllTickets();
	}

	@GetMapping("tickets/{id}")
	public TicketVo getTicket(@PathVariable("id") Long id) {
		return ticketService.viewTicket(id);
	}

	@PostMapping("tickets/{id}")
	public String updateTicket(@PathVariable("id") Long id, @RequestBody TicketVo newVo) {
		return ticketService.updateTicket(id, newVo.getTicketName(), newVo.getTicketDesc());
	}


	@PostMapping("tickets/new")
	public String createTicket(@RequestBody TicketVo vo) {
		return ticketService.createTicket(vo);
	}

	@PostMapping("tickets/delete/{id}")
	public String deleteTicket(@PathVariable("id") Long id) {
		return ticketService.deleteTicket(id);
	}
}

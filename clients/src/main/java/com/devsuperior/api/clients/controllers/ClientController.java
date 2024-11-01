package com.devsuperior.api.clients.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.api.clients.dto.ClientDTO;
import com.devsuperior.api.clients.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {
	
	@Autowired
	private ClientService service;
	
	@GetMapping
	public List<ClientDTO> findAll() {
		return service.findAll();
	}
	
	@GetMapping(value = "/pagedData")
	public Page<ClientDTO> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}

	@GetMapping(value = "/{id}")
	public ClientDTO findById(@PathVariable Long id) {
		return service.findById(id);
	}
}

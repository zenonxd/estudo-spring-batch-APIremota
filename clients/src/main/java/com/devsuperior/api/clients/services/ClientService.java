package com.devsuperior.api.clients.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.api.clients.dto.ClientDTO;
import com.devsuperior.api.clients.entities.Client;
import com.devsuperior.api.clients.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(Pageable pageable) {
		Page<Client> result = repository.findAll(pageable);
		return result.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> result = repository.findAll();
		return result.stream().map(x -> new ClientDTO(x)).toList();
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Client entity = repository.findById(id).get();
		return new ClientDTO(entity);
	}
}

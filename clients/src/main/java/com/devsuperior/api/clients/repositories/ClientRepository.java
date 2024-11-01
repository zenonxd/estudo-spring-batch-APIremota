package com.devsuperior.api.clients.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.api.clients.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}

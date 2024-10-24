package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Clients;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Long> {

	@Query(value = "select c from Clients c where c.sharedKey = ?1", nativeQuery = false)
	public Clients findBysharedkey(String sharedKey);

}

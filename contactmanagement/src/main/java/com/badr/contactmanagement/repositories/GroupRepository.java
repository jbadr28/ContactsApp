package com.badr.contactmanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Groupe;


@Repository
public interface GroupRepository extends JpaRepository<Groupe, Long>{

	//public List<Contact> findByContact(Long id);
	
	public Groupe findOneGroupeByNom(String nom);

	public Groupe findByNom(String nom);
	
}

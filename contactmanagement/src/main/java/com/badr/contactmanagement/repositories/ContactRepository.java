package com.badr.contactmanagement.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.badr.contactmanagement.entities.Contact;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{


	public List<Contact> findByNom(String nom);
	
	public List<Contact> findByTelephone1OrTelephone2(String telephone1,String telephone2);

}

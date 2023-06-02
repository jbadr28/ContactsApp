package com.badr.contactmanagement.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Groupe;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{


	public List<Contact> findByNom(String nom);
	
	public List<Contact> findByTelephone1OrTelephone2(String telephone1,String telephone2);
	
	public List<Contact> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom,String prenom);
	
	public List<Contact> findByTelephone1ContainingOrTelephone2Containing(String telephone1,String telephone2);
	
	@Query(value = "SELECT * FROM contact WHERE SOUNDEX(nom) = SOUNDEX(?1) OR SOUNDEX(prenom)=SOUNDEX(?1)", nativeQuery = true)
	public List<Contact> findBySoundexNomOrSoundexPrenom(String nom);
	

}

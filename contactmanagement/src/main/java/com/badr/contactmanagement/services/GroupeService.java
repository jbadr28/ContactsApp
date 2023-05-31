package com.badr.contactmanagement.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Groupe;
import com.badr.contactmanagement.repositories.ContactRepository;
import com.badr.contactmanagement.repositories.GroupRepository;

@Service
public class GroupeService {
	
	@Autowired
	private GroupRepository groupeRepo;
	
	@Autowired
	private ContactRepository contactRepo;
	
//	public List<Contact> getAllContactsInGroupe(Long groupId){
//		
//		return groupeRepo.findAllContactsById(groupId);
//		
//	}
	
	public Groupe saveGroupe(Groupe groupe) {
		return groupeRepo.save(groupe);
	}
	
	public List<Groupe> getAllGroupes(){
		return groupeRepo.findAll();
	}
	
	public Groupe findGroupeByNom(String nom) {
		return groupeRepo.findByNom(nom);
	}
	
	@PostConstruct
	public void init() {
		if(groupeRepo.findOneGroupeByNom("female")==null) {
			System.out.println("female groupe creation first time");
			Groupe female = new Groupe();
			female.setNom("female");
			groupeRepo.save(female);
		}else {
			System.out.println("Groupe female already created");
		}
		if(groupeRepo.findOneGroupeByNom("male")==null) {
			System.out.println("male groupe creation first time");
			Groupe male = new Groupe();
			male.setNom("male");
			groupeRepo.save(male);
		}else {
			System.out.println("Groupe male already created");
		}
	}
	
	public void deleteGroupe(Long id) {
		Groupe groupe = groupeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));
		for (Contact contact : groupe.getContacts()) {
			contact.getGroups().remove(groupe);
            contactRepo.save(contact);
        }
		groupeRepo.deleteById(id);
	}

	public Groupe findById(Long id) {
		return groupeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("group not found"));
		
	}

}

package com.badr.contactmanagement.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Groupe;
import com.badr.contactmanagement.repositories.ContactRepository;
import com.badr.contactmanagement.repositories.GroupRepository;


@Service
public class ContactService {

	
	private final ContactRepository contactRepo;
	
	
	private final GroupRepository groupRepo;
	
	
	@Autowired
	public ContactService(ContactRepository contactRepo, GroupRepository groupRepo) {
		this.contactRepo = contactRepo;
		this.groupRepo = groupRepo;
	}

	public Contact saveContact(Contact cont) {
		
		return contactRepo.save(cont);
	}
	
	public List<Contact> getAllContacts(){
		Sort sortByName = Sort.by("nom").ascending();
		return contactRepo.findAll(sortByName);
	}
	
	public Contact addContactToGroup(Contact contact, Groupe group) {
        //Contact contact = contactRepo.findById(contactId).orElseThrow(() -> new RuntimeException("contact not found"));
        System.out.println("contact"+contact.toString());
        //Groupe group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("group not found"));
 
        contact.getGroups().add(group);
        group.getContacts().add(contact);

        contactRepo.save(contact);
        groupRepo.save(group);
        return contact;
    }
	
	
	public void deleteContact(Long id) {
		Contact contact = contactRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Contact not found"));
		for (Groupe group : contact.getGroups()) {
            group.getContacts().remove(contact);
            groupRepo.save(group);
        }
			
		 contactRepo.deleteById(id);
	}
	
	public List<Contact> getContactByNom(String nom) {
		return contactRepo.findByNom(nom);
	}
	
	
	public List<Contact> getContactByPhoneNumber(String phoneNumber) {
		return contactRepo.findByTelephone1OrTelephone2(phoneNumber,phoneNumber);
	}

	public Optional<Contact> findById(Long id) {
		// TODO Auto-generated method stub
		return contactRepo.findById(id);
	}
	
	public void update(Contact contact) {
		contactRepo.save(contact);
	}
}

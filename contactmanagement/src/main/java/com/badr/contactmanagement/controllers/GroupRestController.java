package com.badr.contactmanagement.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Groupe;
import com.badr.contactmanagement.services.GroupeService;

@RestController
@RequestMapping("/groups")
public class GroupRestController {
	@Autowired
	private GroupeService groupeService;
	
	@GetMapping
	public List<Groupe> getAllGroups(){
		return groupeService.getAllGroupes();
	}
	
	@PostMapping
	public Groupe addGroupe(@RequestBody Groupe gr) {
		gr.setId(null);
		return groupeService.saveGroupe(gr);
	}
	
	@DeleteMapping("/{id}")
	public void deleteGroupe(@PathVariable Long id) {
		groupeService.deleteGroupe(id);
	}
	
	@GetMapping("/contactsIngroup/{id}")
	public Set<Contact> getContactInGroupe(@PathVariable Long id){
		Groupe gr = groupeService.findById(id);
		return gr.getContacts();
		
	}

}

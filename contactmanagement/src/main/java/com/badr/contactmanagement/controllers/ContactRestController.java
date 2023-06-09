package com.badr.contactmanagement.controllers;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Gender;
import com.badr.contactmanagement.entities.Groupe;
import com.badr.contactmanagement.services.ContactService;
import com.badr.contactmanagement.services.GroupeService;

@RestController
@RequestMapping("/contacts")
public class ContactRestController {
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private GroupeService groupeService;
	
	
//		return "successfully added";
//	}
	
	@DeleteMapping("/{id}")
    public void deleteContact(@PathVariable("id") Long contactId) {
        contactService.deleteContact(contactId);
    }
	
	@GetMapping("/number")
	public List<Contact> getContactByphoneNumber(@RequestParam String phone) {
		return contactService.getContactByPhoneNumber(phone);
	}
	

	@PostMapping("/addgroup")
	public ResponseEntity<String> addgrouptoContact(@RequestParam("contactId") String contactId, @RequestParam("groupId") String groupId){
		System.out.println("contactID : "+contactId+" groupId : "+groupId);
		Long grId = Long.valueOf(groupId);
		Long contId = Long.valueOf(contactId);
		System.out.println(groupeService.findById(grId).getNom());
		System.out.println(contactService.findById(Long.valueOf(contactId)));
		contactService.addContactToGroup(contactService.findById(contId).orElseThrow(() -> new IllegalArgumentException("Contact not found")), groupeService.findById(grId));
		return ResponseEntity.ok("User created successfully");
	}
	
	@DeleteMapping("/deletegroup")
	public ResponseEntity<String> removeGroupfromContact(@RequestParam("contactId") String contactId, @RequestParam("groupId") String groupId){
		System.out.println("contactID : "+contactId+" groupId : "+groupId);
		Long grId = Long.valueOf(groupId);
		Long contId = Long.valueOf(contactId);
		Contact contact = contactService.findById(contId).orElseThrow(()->
				new IllegalArgumentException("Contact not found") );
		contact.getGroups().remove(groupeService.findById(grId));
		contactService.saveContact(contact);
		return ResponseEntity.ok("User created successfully");
	}

}

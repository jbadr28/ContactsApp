package com.badr.contactmanagement.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.services.ContactService;
import com.badr.contactmanagement.services.GroupeService;

@Controller
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private GroupeService groupeService;
	
	@GetMapping
	public String home(Model model) {
		model.addAttribute("user","Badr Eddine Jalili");
		List<Contact> contacts = contactService.getAllContacts();
		model.addAttribute("contacts",contacts);
		model.addAttribute("contact_number",contacts.size());
		return "index";
	}
	
	
	@PostMapping("/delete")
	public String deleteContact(@RequestParam("id") Long id) {
		System.out.println("delete called");
		contactService.deleteContact(id);
		return "redirect:/";
	}
	
	@GetMapping("/home/{id}")
	public String ContactDetails(@RequestParam("id") Long id, Model model) throws Exception {
		System.out.println("redirectiojn");
		Contact c = contactService.findById(id).orElseThrow(()-> {return new Exception("Contact not Found"); });
		model.addAttribute("contact_modefic",c);
		return "redirect:/modifycontact/"+id;
	}
	

}

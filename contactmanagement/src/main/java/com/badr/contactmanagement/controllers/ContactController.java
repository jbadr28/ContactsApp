package com.badr.contactmanagement.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.badr.contactmanagement.entities.Contact;
import com.badr.contactmanagement.entities.Gender;
import com.badr.contactmanagement.entities.Groupe;
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
		model.addAttribute("user", "Badr Eddine Jalili");
		List<Contact> contacts = contactService.getAllContacts();
		List<Groupe> groups = groupeService.getAllGroupes();
		model.addAttribute("contacts", contacts);
		model.addAttribute("contact_number", contacts.size());
		model.addAttribute("group_number",groups.size());
		return "index";
	}

	@PostMapping("/delete")
	public String deleteContact(@RequestParam("id") Long id) {
		System.out.println("delete called");
		contactService.deleteContact(id);
		return "redirect:/";
	}

	@PostMapping("/home")
	public String ContactDetails(@RequestParam("id") Long id, Model model) throws Exception {
		System.out.println("redirectiojn");
		System.out.println(id);
		Contact c = contactService.findById(id).orElseThrow(() -> {
			return new Exception("Contact not Found");
		});
		model.addAttribute("contact_modified", c);
		List<Groupe> gr = groupeService.getAllGroupes();
		Set<Groupe> groupeIn = c.getGroups();
		// System.out.println("before modifectatino"+c);
		System.out.println("before modifectatino" + groupeIn);
		Set<Groupe> groupeOut = new HashSet<Groupe>();
		for (Groupe g : gr) {
			if (!groupeIn.contains(g)) {
				groupeOut.add(g);
			}
		}
		model.addAttribute("groupsIn",groupeIn);
		model.addAttribute("NonGroupe", groupeOut);
		model.addAttribute("groupes", gr);
		return "/modifycontact";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) throws Exception {
		System.out.println("redirectiojn");
		System.out.println(id);
		Contact c = contactService.findById(id).orElseThrow(() -> {
			return new Exception("Contact not Found");
		});
		model.addAttribute("contact_modified", c);
		List<Groupe> gr = groupeService.getAllGroupes();
		Set<Groupe> groupeIn = c.getGroups();
		// System.out.println("before modifectatino"+c);
		System.out.println("before modifectatino" + groupeIn);
//		List<Groupe> groupeOut = new ArrayList<Groupe>();
//		for (Groupe g : gr) {
//			if (!groupeIn.contains(g)) {
//				groupeOut.add(g);
//			}
//		}
//		model.addAttribute("NonGroupe", groupeOut);
		model.addAttribute("groupes", gr);
		model.addAttribute("groupsIn",groupeIn);
		model.addAttribute("user", "Badr Eddine Jalili");
		Contact existingContact = contactService.findById(id).orElseThrow(() -> {
			return new Exception("Contact not Found");
		});

		model.addAttribute("contact", existingContact);
		return "modifycontact";
	}

	@PostMapping("/edit/{id}")
	public String updateContact(@PathVariable("id") Long id, Model model,
			@ModelAttribute("updated_contact") Contact updatedContact,@ModelAttribute("selectedGroups") Object groupNames) throws Exception {
		System.out.println("contact to be updated" + updatedContact);
		Contact modified = contactService.findById(updatedContact.getId()).orElseThrow(() -> {
			return new Exception("contact not found");
		});
		modified.setNom(updatedContact.getNom());
		modified.setPrenom(updatedContact.getPrenom());
		modified.setAdress(updatedContact.getAdress());
		modified.setGender(updatedContact.getGender());
		modified.setTelephone1(updatedContact.getTelephone1());
		modified.setTelephone2(updatedContact.getTelephone2());
		modified.setEmailpresonel(updatedContact.getEmailpresonel());
		modified.setEmailprofessional(updatedContact.getEmailprofessional());
		System.out.println("ModelAttribute groupsIN" + groupNames);
//		for(String g : groupNames) {
//			System.out.println(g);
//		}
		  System.out.println(updatedContact.getGroups()); 
//		  List<String> gruop =updatedContact.getGroupNames(); 
//		  for(String f:gruop) {
//			  System.out.println(f);
//		  }
		  System.out.println("group in"+model.getAttribute("GroupsIn"));
		  System.out.println("Nongroup"+model.getAttribute("NonGroupe"));
		System.out.println(model.getAttribute("selectedGroups"));
		contactService.update(modified);

		return "redirect:/";
	}
	
	@GetMapping("/createContact")
	public String redirectToCreatContactForm(Model model) {
		model.addAttribute("newContact",new Contact());
		return "/createContact";
	}
	
	@PostMapping("/createContact")
	public String createContact(@ModelAttribute("newContact") Contact contact) {
		contact.setId(null);
		Contact registeredContact = contactService.saveContact(contact);
		if( registeredContact.getGender().equals(Gender.Male)) {
			System.out.println("ffff");
			Groupe sexGroupe = groupeService.findGroupeByNom("male");
			contactService.addContactToGroup(contact, sexGroupe);
		}else if(registeredContact.getGender().equals(Gender.Female)) {
			System.out.println("mmmm");
			Groupe sexGroupe = groupeService.findGroupeByNom("female");
			contactService.addContactToGroup(contact, sexGroupe);
		}if(registeredContact.getNom()!=null) {
			if(groupeService.findGroupeByNom(registeredContact.getNom())==null) {
				Groupe groupeWithSameNom = new Groupe();
				groupeWithSameNom.setNom(registeredContact.getNom());
				Groupe registered = groupeService.saveGroupe(groupeWithSameNom);
				contactService.addContactToGroup(contact, registered);
			}else {
				Groupe groupeWithSameNom =groupeService.findGroupeByNom(registeredContact.getNom());
				contactService.addContactToGroup(contact, groupeWithSameNom);
			}
		}
		
		return "redirect:/";
	}
	

}

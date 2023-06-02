package com.badr.contactmanagement.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.badr.contactmanagement.nlp.NlpClass;
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
		model.addAttribute("groups",groups);
		model.addAttribute("contact_number", contacts.size());
		model.addAttribute("group_number",groups.size());
		return "index";
	}

	@PostMapping("/deleteGroup")
	public String deleteGroup(@RequestParam("id") Long id) {
		groupeService.deleteGroupe(id);
		return "redirect:/";
	}
	@PostMapping("/search")
	public String search(@RequestParam("searchInput") String searchInput,Model model) {
		Set<Contact> contacts =new HashSet<Contact>();
		if(NlpClass.isNumeric(searchInput)) {
			List<Contact> contactsNumber = contactService.searchByPhoneNumCont(searchInput);
			for(Contact c: contactsNumber) {
				System.out.println("contact by phone "+c);
				contacts.add(c);
			}
			model.addAttribute("contacts", contacts);
			model.addAttribute("contact_number", contacts.size());
		}else {
			
			List<Contact> allContacts = contactService.getAllContacts();
			System.out.println("searchinput "+searchInput);
			List<Contact> contactbyregex = contactService.search(searchInput);
			for(Contact c: contactbyregex) {
				System.out.println(c);
				contacts.add(c);
			}
			List<Contact> conSoundex = contactService.searchSoundex(searchInput);
			for(Contact c : conSoundex) {
				System.out.println("found by soundex "+c);
				contacts.add(c);
			}
			
			for(Contact c : allContacts) {
				if(Math.min(
					NlpClass.minEditDistance(c.getNom(), searchInput), 
					NlpClass.minEditDistance(c.getPrenom(), searchInput)
					)<=3){
					System.out.println("min edit distance is "+Math.min(
					NlpClass.minEditDistance(c.getNom(), searchInput), 
					NlpClass.minEditDistance(c.getPrenom(), searchInput)
					));
					contacts.add(c);
					}
			}
			List<Contact> sortedConatcts = new ArrayList<>(contacts);
			Collections.sort(sortedConatcts, Comparator.comparingInt(c -> 
			Math.min(
					NlpClass.minEditDistance(c.getNom(), searchInput), 
					NlpClass.minEditDistance(c.getPrenom(), searchInput)
					)));
			model.addAttribute("contacts", sortedConatcts);
			model.addAttribute("contact_number", sortedConatcts.size());
			
		}
		
		model.addAttribute("user", "Badr Eddine Jalili");
		List<Groupe> groups = groupeService.getAllGroupes();
		model.addAttribute("group_number",groups.size());
		model.addAttribute("groups",groups);
		return "index";
	}
	@PostMapping("/delete")
	public String deleteContact(@RequestParam("id") Long id) {
		System.out.println("delete called");
		contactService.deleteContact(id);
		return "redirect:/";
	}

	@PostMapping("/modifygroup")
	public String groupDetails(@RequestParam("id") Long id, Model model) {
		Groupe group = groupeService.findById(id);
		model.addAttribute("group_modified", group);
		List<Contact> allContacts = contactService.getAllContacts();
		Set<Contact> contactIn = group.getContacts();
		Set<Contact> contactOut = new HashSet<Contact>();
		for(Contact con:allContacts) {
			if(!contactIn.contains(con)) {
				contactOut.add(con);
			}
		}
		model.addAttribute("contactsIn",contactIn);
		model.addAttribute("Noncontact",contactOut);
		model.addAttribute("contacts",allContacts);
		return "/modifygroup";
	}
	@PostMapping("/home")
	public String contactDetails(@RequestParam("id") Long id, Model model) throws Exception {
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

	@PostMapping("/editgroup/{id}")
	public String updateGroup(@PathVariable("id") Long id, Model model,
			@ModelAttribute("updated_group")Groupe updatedgroup) {
		Groupe modified = groupeService.findById(id);
		modified.setNom(updatedgroup.getNom());
		groupeService.saveGroupe(updatedgroup);
		return "redirect:/";
	}
	@PostMapping("/edit/{id}")
	public String updateContact(@PathVariable("id") Long id, Model model,
			@ModelAttribute("updated_contact") Contact updatedContact) throws Exception {
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
		contactService.update(modified);

		return "redirect:/";
	}
	
	@GetMapping("/createGroup")
	public String redirectToCreateGroupForm(Model model) {
		model.addAttribute("newGroup",new Groupe());
		return "/createGroup";
	}
	@PostMapping("/createGroup")
	public String createGroup(@ModelAttribute("newGroup")Groupe group) {
		groupeService.saveGroupe(group);
		return "redirect:/";
	}
	@GetMapping("/createContact")
	public String redirectToCreateContactForm(Model model) {
		model.addAttribute("newContact",new Contact());
		return "/createContact";
	}
	
	@PostMapping("/createContact")
	public String createContact(@ModelAttribute("newContact") Contact contact) {
		contact.setId(null);
		Contact registeredContact = contactService.saveContact(contact);
		if( registeredContact.getGender().equals(Gender.Male)) {
			System.out.println("m");
			Groupe sexGroupe = groupeService.findGroupeByNom("male");
			if(sexGroupe==null) {
				sexGroupe=new Groupe();
				sexGroupe.setNom("male");
				groupeService.saveGroupe(sexGroupe);
			}
			contactService.addContactToGroup(contact, sexGroupe);
		}else if(registeredContact.getGender().equals(Gender.Female)) {
			System.out.println("f");
			Groupe sexGroupe = groupeService.findGroupeByNom("female");
			if(sexGroupe==null) {
				sexGroupe=new Groupe();
				sexGroupe.setNom("female");
				groupeService.saveGroupe(sexGroupe);
			}
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

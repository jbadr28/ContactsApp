package com.badr.contactmanagement.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Groupe {

	@Id
	@GeneratedValue(strategy  =  GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(unique = true)
	private String nom;
	
	
	@ManyToMany(mappedBy = "groups",cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Contact> contacts = new HashSet<Contact>();



	public Groupe() {
		
	}
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}

	@JsonIgnore
	public Set<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
		for(Contact cont: contacts) {
			cont.getGroups().add(this);
		}
	}


	public Groupe(Long id, String nom, Set<Contact> contacts) {

		this.id = id;
		this.nom = nom;
		this.contacts = contacts;
	}

	
	
	
}

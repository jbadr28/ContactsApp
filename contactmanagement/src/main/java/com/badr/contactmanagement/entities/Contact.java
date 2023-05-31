package com.badr.contactmanagement.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nom;

	private String prenom;
	
	private String telephone1;
	
	private String telephone2;
	
	private String adress;
	
	private String emailpresonal;
	

	private String emailprofessional;
	@Enumerated(EnumType.STRING)
	private Gender gender ;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "group_contact", joinColumns = @JoinColumn(name = "id_contact"), inverseJoinColumns = @JoinColumn(name = "id_group"))
	@JsonIgnore
	@JsonManagedReference
	
	//@JsonBackReference
	private Set<Groupe> groups = new HashSet<Groupe>();
	public Contact() {
		
	}





	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getEmailpresonel() {
		return emailpresonal;
	}

	public void setEmailpresonel(String emailpresonal) {
		this.emailpresonal = emailpresonal;
	}

	public String getEmailprofessional() {
		return emailprofessional;
	}

	public void setEmailprofessional(String emailprofessional) {
		this.emailprofessional = emailprofessional;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Set<Groupe> getGroups() {
		return groups;
	}

	public void setGroups(Set<Groupe> groups) {
		this.groups = groups;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	public Contact(Long id, String nom,String prenom, String telephone1, String telephone2, String adress, String emailpresonal,
			String emailprofessional, Gender gender, Set<Groupe> groups) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone1 = telephone1;
		this.telephone2 = telephone2;
		this.adress = adress;
		this.emailpresonal = emailpresonal;
		this.emailprofessional = emailprofessional;
		this.gender = gender;
		this.groups = groups;
	}




	@Override
	public String toString() {
		return "Contact [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", telephone1=" + telephone1
				+ ", telephone2=" + telephone2 + ", adress=" + adress + ", emailpresonel=" + emailpresonal
				+ ", emailprofessional=" + emailprofessional + ", gender=" + gender + ", groups=" + groups + "]";
	}
	
	
	

}

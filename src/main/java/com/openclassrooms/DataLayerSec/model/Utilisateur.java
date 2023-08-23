package com.openclassrooms.DataLayerSec.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.openclassrooms.DataLayerSec.service.UtilisateurService.InsufficientBalanceException;

import jakarta.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "utilisateur")
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int utilisateurId;

	public int getUtilisateurId() {
		return utilisateurId;
	}

	public void setUtilisateurId(int utilisateurId) {
		this.utilisateurId = utilisateurId;
	}

	@Column(name = "prenom")
	private String prenom;

	@Column(name = "nom")
	private String nom;

	@Column(name = "motdepasse")
	private String motDePasse;

	@Column(name = "adresseemail")
	private String adresseEmail;

	@Column(name = "soldeducompte")
	private BigDecimal soldeDuCompte;

	/*
	 * @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =
	 * FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "IDutilisateuremetteur", referencedColumnName = "ID")
	 */

	@OneToMany
	@JoinColumn(name = "IDutilisateuremetteur", referencedColumnName = "ID")
	List<Transfert> transferts = new ArrayList<>();

	public List<Transfert> getTransferts() {
		return transferts;
	}

	public void setTransferts(List<Transfert> transferts) {
		this.transferts = transferts;
	}


	/*
	 * @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =
	 * FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "IDutilisateurdeposant", referencedColumnName = "ID")
	 * 
	 * List<Depot> depots = new ArrayList<>();
	 * 
	 * 
	 * public List<Depot> getDepots() { return depots; }
	 * 
	 * 
	 * public void setDepots(List<Depot> depots) { this.depots = depots; }
	 * 
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =
	 * FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "IDutilisateurretirant", referencedColumnName = "ID")
	 * List<Retrait> retraits = new ArrayList<>();
	 * 
	 * public List<Retrait> getRetraits() { return retraits; }
	 * 
	 * 
	 * public void setRetraits(List<Retrait> retraits) { this.retraits = retraits; }
	 * 
	 */
	@OneToMany
	@JoinColumn(name = "IDutilisateur", referencedColumnName = "ID")
	List<Operation> operations = new ArrayList<>();
	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	@ManyToMany
	@JoinTable(name = "utilisateurami", joinColumns = @JoinColumn(name = "utilisateurid"), inverseJoinColumns = @JoinColumn(name = "amiid"))
	private List<Utilisateur> amis = new ArrayList<>();

	public List<Utilisateur> getAmis() {
		return amis;
	}

	public void setAmis(List<Utilisateur> amis) {
		this.amis = amis;
	}

	public void effectuerDepot(BigDecimal montant) {
		soldeDuCompte = soldeDuCompte.add(montant);
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getAdresseEmail() {
		return adresseEmail;
	}

	public void setAdresseEmail(String adresseEmail) {
		this.adresseEmail = adresseEmail;
	}

	public BigDecimal getSoldeDuCompte() {
		return soldeDuCompte;
	}

	public void setSoldeDuCompte(BigDecimal soldeDuCompte) {
		this.soldeDuCompte = soldeDuCompte;
	}
	
	public void effectuerRetrait(BigDecimal montant) {
	        this.soldeDuCompte = this.soldeDuCompte.subtract(montant);
	}
}

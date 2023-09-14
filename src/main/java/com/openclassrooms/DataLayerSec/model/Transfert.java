package com.openclassrooms.DataLayerSec.model; 

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transfert")
public class Transfert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int transfertId;
    
    @ManyToOne
  	@JoinColumn(name = "IDutilisateuremetteur", referencedColumnName = "ID")
  	private Utilisateur utilisateurEmetteur;

      @ManyToOne
    	@JoinColumn(name = "IDutilisateurbeneficiaire", referencedColumnName = "ID")
    	private Utilisateur utilisateurBeneficiaire;
    
    @Column(name = "Montant")
    private BigDecimal montant;

    @Column(name = "Dateetheuredutransfert")
    private LocalDateTime dateHeureTransfert;

	public int getTransfertId() {
		return transfertId;
	}

	public void setTransfertId(int transfertId) {
		this.transfertId = transfertId;
	}

	public Utilisateur getUtilisateurEmetteur() {
		return utilisateurEmetteur;
	}

	public void setUtilisateurEmetteur(Utilisateur utilisateurEmetteur) {
		this.utilisateurEmetteur = utilisateurEmetteur;
	}

	public Utilisateur getUtilisateurBeneficiaire() {
		return utilisateurBeneficiaire;
	}

	public void setUtilisateurBeneficiaire(Utilisateur utilisateurBeneficiaire) {
		this.utilisateurBeneficiaire = utilisateurBeneficiaire;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public LocalDateTime getDateHeureTransfert() {
		return dateHeureTransfert;
	}

	public void setDateHeureTransfert(LocalDateTime dateHeureTransfert) {
		this.dateHeureTransfert = dateHeureTransfert;
	}
}

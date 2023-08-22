package com.openclassrooms.DataLayerSec.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "depot")
public class Depot {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private int depotId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "IDutilisateurdeposant", referencedColumnName = "ID", insertable = false, updatable = false)
	private Utilisateur utilisateurDeposant;

	
	@Column(name = "IDutilisateurdeposant")
	private int IDutilisateurdeposant;
	
	 @Column(name = "Montant")
	    private BigDecimal montant;

	    @Column(name = "Dateetheuredudepot")
	    private LocalDateTime dateetheuredudepot;

		public int getDepotId() {
			return depotId;
		}

		public void setDepotId(int depotId) {
			this.depotId = depotId;
		}

		public Utilisateur getUtilisateurDeposant() {
			return utilisateurDeposant;
		}

		public void setUtilisateurDeposant(Utilisateur utilisateurDeposant) {
			this.utilisateurDeposant = utilisateurDeposant;
		}

		public int getIDutilisateurdeposant() {
			return IDutilisateurdeposant;
		}

		public void setIDutilisateurdeposant(int iDutilisateurdeposant) {
			IDutilisateurdeposant = iDutilisateurdeposant;
		}

		public BigDecimal getMontant() {
			return montant;
		}

		public void setMontant(BigDecimal montant) {
			this.montant = montant;
		}

		public LocalDateTime getDateetheuredudepot() {
			return dateetheuredudepot;
		}

		public void setDateetheuredudepot(LocalDateTime dateetheuredudepot) {
			this.dateetheuredudepot = dateetheuredudepot;
		}

		@Override
		public String toString() {
			return "depotId=" + depotId + ", utilisateurDeposant=" + utilisateurDeposant
					+ ", IDutilisateurdeposant=" + IDutilisateurdeposant + ", montant=" + montant
					+ ", dateetheuredudepot=" + dateetheuredudepot;
		}

}

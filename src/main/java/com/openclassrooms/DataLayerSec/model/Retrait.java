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
@Table(name = "retrait")
public class Retrait {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private int retraiId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "IDutilisateurretirant", referencedColumnName = "ID", insertable = false, updatable = false)
	private Utilisateur utilisateurRetirant;

	@Column(name = "IDutilisateurretirant")
	private int IDutilisateurretirant;
	
	 @Column(name = "Montant")
	    private BigDecimal montant;

	    @Column(name = "Dateetheureduretrait")
	    private LocalDateTime dateetheureduretrait;

		public int getRetraiId() {
			return retraiId;
		}

		public void setRetraiId(int retraiId) {
			this.retraiId = retraiId;
		}

		public Utilisateur getUtilisateurRetirant() {
			return utilisateurRetirant;
		}

		public void setUtilisateurRetirant(Utilisateur utilisateurRetirant) {
			this.utilisateurRetirant = utilisateurRetirant;
		}

		public int getIDutilisateurretirant() {
			return IDutilisateurretirant;
		}

		public void setIDutilisateurretirant(int iDutilisateurretirant) {
			IDutilisateurretirant = iDutilisateurretirant;
		}

		public BigDecimal getMontant() {
			return montant;
		}

		public void setMontant(BigDecimal montant) {
			this.montant = montant;
		}

		public LocalDateTime getDateetheureduretrait() {
			return dateetheureduretrait;
		}

		public void setDateetheureduretrait(LocalDateTime dateetheureduretrait) {
			this.dateetheureduretrait = dateetheureduretrait;
		}

		@Override
		public String toString() {
			return "retraiId=" + retraiId + ", utilisateurRetirant=" + utilisateurRetirant
					+ ", IDutilisateurretirant=" + IDutilisateurretirant + ", montant=" + montant
					+ ", dateetheureduretrait=" + dateetheureduretrait;
		}
}

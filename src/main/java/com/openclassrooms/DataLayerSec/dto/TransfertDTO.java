package com.openclassrooms.DataLayerSec.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransfertDTO {

	private int transfertId;

	private UtilisateurDTO utilisateurEmetteurDTO;

	private UtilisateurDTO utilisateurBeneficiaireDTO;

	private BigDecimal montant;

	private LocalDateTime dateHeureTransfert;

	public int getTransfertId() {
		return transfertId;
	}

	public void setTransfertId(int transfertId) {
		this.transfertId = transfertId;
	}

	public UtilisateurDTO getUtilisateurEmetteurDTO() {
		return utilisateurEmetteurDTO;
	}

	public void setUtilisateurEmetteurDTO(UtilisateurDTO utilisateurEmetteurDTO) {
		this.utilisateurEmetteurDTO = utilisateurEmetteurDTO;
	}

	public UtilisateurDTO getUtilisateurBeneficiaireDTO() {
		return utilisateurBeneficiaireDTO;
	}

	public void setUtilisateurBeneficiaireDTO(UtilisateurDTO utilisateurBeneficiaireDTO) {
		this.utilisateurBeneficiaireDTO = utilisateurBeneficiaireDTO;
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

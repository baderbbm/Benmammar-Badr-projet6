package com.openclassrooms.DataLayerSec.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.openclassrooms.DataLayerSec.model.NatureOperation;

public class OperationDTO {

	private int operationId;

	private UtilisateurDTO utilisateur;

	private BigDecimal montant;

	private LocalDateTime dateetheureoperation;

	private NatureOperation natureoperation;

	public int getOperationId() {
		return operationId;
	}

	public void setOperationId(int operationId) {
		this.operationId = operationId;
	}

	public UtilisateurDTO getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(UtilisateurDTO utilisateur) {
		this.utilisateur = utilisateur;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public LocalDateTime getDateetheureoperation() {
		return dateetheureoperation;
	}

	public void setDateetheureoperation(LocalDateTime dateetheureoperation) {
		this.dateetheureoperation = dateetheureoperation;
	}

	public NatureOperation getNatureoperation() {
		return natureoperation;
	}

	public void setNatureoperation(NatureOperation natureoperation) {
		this.natureoperation = natureoperation;
	}

}

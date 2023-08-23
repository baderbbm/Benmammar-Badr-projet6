package com.openclassrooms.DataLayerSec.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int operationId;

    @ManyToOne
    @JoinColumn(name = "IDutilisateur", referencedColumnName = "ID")
    private Utilisateur utilisateur;

    @Column(name = "Montant")
    private BigDecimal montant;

    @Column(name = "Dateetheureoperation")
    private LocalDateTime dateetheureoperation;

    @Enumerated(EnumType.STRING)
    @Column(name = "Natureoperation")
    private NatureOperation natureoperation;

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
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

    @Override
    public String toString() {
        return "operationId=" + operationId + ", utilisateur=" + utilisateur + ", montant=" + montant
                + ", dateetheureoperation=" + dateetheureoperation + ", natureoperation=" + natureoperation;
    }
}

package com.openclassrooms.DataLayerSec.dto;

import java.math.BigDecimal;

public class UtilisateurDTO {

    private String prenom;
    private String nom;
    private String motDePasse;
    private String adresseEmail;
    private BigDecimal soldeDuCompte;

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
}

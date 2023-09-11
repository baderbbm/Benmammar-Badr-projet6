package com.openclassrooms.DataLayerSec.config;

import java.util.ArrayList;
import org.springframework.security.core.userdetails.User;
import com.openclassrooms.DataLayerSec.model.Utilisateur;

public class CustomUserDetails extends User {
    private final Utilisateur utilisateur;
    public CustomUserDetails(Utilisateur utilisateur) {
    	super(utilisateur.getAdresseEmail(), utilisateur.getMotDePasse(), new ArrayList<>()); 
        this.utilisateur = utilisateur;
    }
}

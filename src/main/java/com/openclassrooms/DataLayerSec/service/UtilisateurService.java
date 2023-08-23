package com.openclassrooms.DataLayerSec.service;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.openclassrooms.DataLayerSec.model.NatureOperation;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;
import com.openclassrooms.DataLayerSec.repository.UtilisateurRepository;
 
@Service
public class UtilisateurService {
 
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	
	@Autowired
	private OperationRepository operationRepository;
	
	public Iterable<Utilisateur> getUsers() {
		return utilisateurRepository.findAll();
	}
	
	public Optional<Utilisateur> getUserById(Integer id) {
		return utilisateurRepository.findById(id); 
	}	
	
	
	// exception personnalisée 
	public class EmailExistsException extends RuntimeException {
	    public EmailExistsException(String message) {
	        super(message);
	    }
	}

	
	public Utilisateur addUtilisateur(Utilisateur utilisateur) {
        try {
            return utilisateurRepository.save(utilisateur);
            // capturer et traiter spécifiquement l'exception liée à l'existence d'un emai
        } catch (DataIntegrityViolationException ex) {
            throw new EmailExistsException("L'email existe déjà.");
        }
    }
	
	public void deleteUtilisateurById(Integer id) {
		utilisateurRepository.deleteById(id);
	}
	public Iterable<Utilisateur> getUsersByName(String name) {
		return utilisateurRepository.findByNom(name);
	}
	
	 public Iterable<Utilisateur> findByNameNative(String nom) {
	        return utilisateurRepository.findByNameNative(nom);
	    }
	 
	 public Iterable<Utilisateur> findByNameJPQL(String nom) {
	        return utilisateurRepository.findByNameJPQL(nom);
	    }
	 
	 public Utilisateur findByAdresseEmail(String adresseEmail) {
	        return utilisateurRepository.findByAdresseEmail(adresseEmail);
	    }

	    public void ajouterAmi(Utilisateur utilisateur, Utilisateur ami) {
	        utilisateur.getAmis().add(ami);
	        utilisateurRepository.save(utilisateur);
	    }
	    
	    public void effectuerDepot(Utilisateur utilisateur, BigDecimal montant) {
	        utilisateur.effectuerDepot(montant);
	        utilisateurRepository.save(utilisateur);
	        
	        Operation operation = new Operation();
	        operation.setUtilisateur(utilisateur);
	        operation.setMontant(montant);
	        operation.setDateetheureoperation(LocalDateTime.now());
	        operation.setNatureoperation(NatureOperation.depot);
	        operationRepository.save(operation);
	    }
	    
		// exception personnalisée 
	    public class InsufficientBalanceException extends RuntimeException {
	        public InsufficientBalanceException(String message) {
	            super(message);
	        }
	    }

	    
	    public void effectuerRetrait(Utilisateur utilisateur, BigDecimal montant) {
	        if (utilisateur.getSoldeDuCompte().compareTo(montant) >= 0) {
	            utilisateur.effectuerRetrait(montant);
	            utilisateurRepository.save(utilisateur);

	            Operation operation = new Operation();
	            operation.setUtilisateur(utilisateur);
	            operation.setMontant(montant);
	            operation.setDateetheureoperation(LocalDateTime.now());
	            operation.setNatureoperation(NatureOperation.retrait);
	            operationRepository.save(operation);
	        } else {
	            throw new InsufficientBalanceException("Solde insuffisant pour effectuer le retrait.");
	        }
	    }	 
}

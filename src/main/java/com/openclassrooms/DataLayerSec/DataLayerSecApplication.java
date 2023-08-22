package com.openclassrooms.DataLayerSec;
 
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
import com.openclassrooms.DataLayerSec.model.Utilisateur;

import com.openclassrooms.DataLayerSec.service.UtilisateurService;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class DataLayerSecApplication implements CommandLineRunner {
 
	@Autowired
	private UtilisateurService utilisateurService;

	
	public static void main(String[] args) {
		SpringApplication.run(DataLayerSecApplication.class, args);
	}
 
	@Override
	@Transactional
	public void run(String... args) throws Exception {	
	
		/*
		
		//   afficher les utilisateurs 
		
		utilisateurService.getUsers().forEach(
				utilisateur -> System.out.println(utilisateur.getNom()));
		
		//  ajouter un utilisateur 
		
		Utilisateur newUtilisateur = new Utilisateur();
		
		newUtilisateur.setLogin("sab");
		newUtilisateur.setMotDePasse("sab123");
		newUtilisateur.setNom("Couturier");
		newUtilisateur.setPrenom("Paul");
		newUtilisateur.setAdresseEmail("sab@gmail.com");
		newUtilisateur.setSoldeDuCompte(new BigDecimal(6000));
		
		
		newUtilisateur = utilisateurService.addUtilisateur(newUtilisateur);
			
		utilisateurService.getUsers().forEach(
				utilisateur -> System.out.println(utilisateur.getNom()));
		
		//  mettre a jour un utilisateur 
		
		Utilisateur existingUtilisateur = utilisateurService.getUserById(7).get();
		System.out.println(existingUtilisateur.getLogin());
		
		existingUtilisateur.setLogin("bbm13");
		utilisateurService.addUtilisateur(existingUtilisateur);
		
		
		//   supprimer un utilisateur 
		
		existingUtilisateur = utilisateurService.getUserById(1).get();
				
		utilisateurService.deleteUtilisateurById(1);
		
	*/
		
		/*
		utilisateurService.getUsers().forEach(
				utilisateur -> System.out.println(utilisateur.getNom()));
		
		Utilisateur newUtilisateur = new Utilisateur();
		newUtilisateur.setNom("Couturier");
		newUtilisateur.setPrenom("Paul");
		newUtilisateur.setAdresseEmail("sab@gmail.com");
		newUtilisateur.setMotDePasse("sab123");
		newUtilisateur.setSoldeDuCompte(new BigDecimal(6000));
		newUtilisateur = utilisateurService.addUtilisateur(newUtilisateur);
		
		
		newUtilisateur = new Utilisateur();
		newUtilisateur.setNom("Couturier");
		newUtilisateur.setPrenom("Paul");
		newUtilisateur.setAdresseEmail("sabb@gmail.com");
		newUtilisateur.setMotDePasse("sab123");
		newUtilisateur.setSoldeDuCompte(new BigDecimal(6000));
		newUtilisateur = utilisateurService.addUtilisateur(newUtilisateur);
		utilisateurService.getUsers().forEach(
				utilisateur -> System.out.println(utilisateur.getNom()));
		
*/
	    
		//Utilisateur existingUtilisateur = utilisateurService.getUserById(1).get();
		
		//utilisateurService.deleteUtilisateurById(1);
		/*
		Iterable<Utilisateur> searchResults = utilisateurService.getUsersByName("Benmammar");
		searchResults.forEach(user -> System.out.println(user.getPrenom()));
		
		searchResults = utilisateurService.findByNameNative("Benmammar");
		searchResults.forEach(user -> System.out.println(user.getUtilisateurId()));

		searchResults = utilisateurService.findByNameJPQL("Benmammar");
		searchResults.forEach(user -> System.out.println(user.getUtilisateurId()));
		*/
					
	}
}
////
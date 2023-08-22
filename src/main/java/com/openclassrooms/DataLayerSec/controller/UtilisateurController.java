package com.openclassrooms.DataLayerSec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService.EmailExistsException;

@Controller
public class UtilisateurController {
	private final UtilisateurService utilisateurService;

	@Autowired
	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	@GetMapping("/enregistrerUtilisateur")
	public String showEnregistrerUtilisateurForm(ModelMap model) {
		model.put("utilisateur", new Utilisateur());
		return "enregistrer-utilisateur"; 
	}

	@PostMapping("/enregistrerUtilisateur")
	public String enregistrerUtilisateur(@ModelAttribute Utilisateur utilisateur, ModelMap model) {
		try {
			utilisateurService.addUtilisateur(utilisateur);
			model.addAttribute("success", "L'utilisateur a été enregistré avec succès.");
			return "enregistrer-utilisateur";
		} catch (EmailExistsException e) {
			model.addAttribute("error", "Cet email existe déjà.");
			return "enregistrer-utilisateur";
		}
	}

	@GetMapping("/ajouterAmi")
	public String showAjouterAmiForm(ModelMap model) {
		model.put("ami", new Utilisateur()); 
		return "ajouter-ami";
	}

	@PostMapping("/ajouterAmi")
	public String ajouterAmi(@RequestParam String adresseEmailAmi, ModelMap model) {
		// Rechercher l'utilisateur actuel par son adresse e-mail
		// Utilisez les informations d'authentification si Spring Security
		Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail("badr@gmail.com");

		// Utilisateur utilisateurActuel = ...;

		// Rechercher l'ami par son adresse e-mail
		Utilisateur ami = utilisateurService.findByAdresseEmail(adresseEmailAmi);
		if (ami != null) {
			if (!utilisateurActuel.getAmis().contains(ami)) {
				utilisateurService.ajouterAmi(utilisateurActuel, ami);
				model.addAttribute("success", "Ami ajouté avec succès.");
			} else {
				model.addAttribute("error", "Cet utilisateur est déjà dans votre liste d'amis.");
			}
		} else {
			model.addAttribute("error", "Aucun utilisateur trouvé avec cette adresse e-mail.");
		}

		return "ajouter-ami";
	}
}

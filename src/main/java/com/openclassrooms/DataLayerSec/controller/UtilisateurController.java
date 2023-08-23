package com.openclassrooms.DataLayerSec.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional; 

import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService.EmailExistsException;
import com.openclassrooms.DataLayerSec.service.UtilisateurService.InsufficientBalanceException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
	

	@GetMapping("/effectuerDepot")
	public String showEffectuerDepotForm(@RequestParam Integer utilisateurId, HttpSession session, ModelMap model) {
	    Optional<Utilisateur> optionalUtilisateur = utilisateurService.getUserById(utilisateurId);
	    if (optionalUtilisateur.isPresent()) {
	        Utilisateur utilisateur = optionalUtilisateur.get();
	        session.setAttribute("utilisateur", utilisateur); // Stocker l'utilisateur dans l'attribut de session
	        model.addAttribute("utilisateur", utilisateur);
	        return "effectuerDepot";
	    }
	    return "erreur-utilisateur";
	}

	@PostMapping("/effectuerDepot")
	public ModelAndView effectuerDepot(@RequestParam BigDecimal montant, HttpSession session) {
	    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur"); // Récupérer l'utilisateur depuis l'attribut de session
	    
	    if (utilisateur != null) {
	        utilisateurService.effectuerDepot(utilisateur, montant);
	        
	        //ModelAndView modelAndView = new ModelAndView("redirect:/effectuerDepot?utilisateurId="+utilisateur.getUtilisateurId());
	        ModelAndView modelAndView= new ModelAndView("operation-succes", "operation-succes", "Opération effectuée avec succès.");
	        modelAndView.addObject("utilisateur", utilisateur);
	        return modelAndView;
	    } else {
	    	
	        return new ModelAndView("erreur-utilisateur", "erreur-utilisateur", "Utilisateur introuvable dans la session.");
	     
	}
	}
	
	@PostMapping("/effectuerRetrait")
	public ModelAndView effectuerRetrait(@RequestParam BigDecimal montant, HttpSession session) {
	    Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

	    if (utilisateur != null) {
	        try {
	            utilisateurService.effectuerRetrait(utilisateur, montant);

	           // ModelAndView modelAndView = new ModelAndView("redirect:/effectuerDepot?utilisateurId=" + utilisateur.getUtilisateurId());
		        ModelAndView modelAndView= new ModelAndView("operation-succes", "operation-succes", "Opération effectuée avec succès.");
	            modelAndView.addObject("utilisateur", utilisateur);
	            return modelAndView;
	        } catch (InsufficientBalanceException e) {
	            return new ModelAndView("erreur-solde", "erreur-solde", "Solde insuffisant pour effectuer le retrait.");
	        }
	    } else {
	        return new ModelAndView("erreur-utilisateur", "erreur-utilisateur", "Utilisateur introuvable dans la session.");
	    }
	}
	
	@GetMapping("/effectuerRetrait")
	public String showEffectuerRetraitForm(@RequestParam Integer utilisateurId, HttpSession session, ModelMap model) {
	    Optional<Utilisateur> optionalUtilisateur = utilisateurService.getUserById(utilisateurId);
	    if (optionalUtilisateur.isPresent()) {
	        Utilisateur utilisateur = optionalUtilisateur.get();
	        session.setAttribute("utilisateur", utilisateur);
	        model.addAttribute("utilisateur", utilisateur);
	        return "effectuerRetrait";
	    }
	    return "erreur-utilisateur";
	}
}

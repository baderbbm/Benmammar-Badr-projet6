package com.openclassrooms.DataLayerSec.controller;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Transfert;
import java.util.List;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.service.OperationService;
import com.openclassrooms.DataLayerSec.service.TransfertService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService.EmailExistsException;
import com.openclassrooms.DataLayerSec.service.UtilisateurService.InsufficientBalanceException;

@Controller
public class UtilisateurController {
	private UtilisateurService utilisateurService;
	
	@Autowired
	private  OperationService operationService;
	
	
	@Autowired
	private  TransfertService transfertService;

	@Autowired
	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	@GetMapping("/enregistrerUtilisateur")
	public String showEnregistrerUtilisateurForm(@ModelAttribute("utilisateurDTO") UtilisateurDTO utilisateurDTO) {
	    return "enregistrer-utilisateur";
	}
	
	
	@PostMapping("/enregistrerUtilisateur")
	public String enregistrerUtilisateur(@ModelAttribute UtilisateurDTO utilisateurDTO, RedirectAttributes redirectAttributes) {
	    try {
	        utilisateurService.addUtilisateur(utilisateurDTO);
	        redirectAttributes.addFlashAttribute("success", "L'utilisateur a été enregistré avec succès.");
	    } catch (EmailExistsException e) {
	        redirectAttributes.addFlashAttribute("error", "Cet email existe déjà.");
	    }
	    return "redirect:/enregistrerUtilisateur";
	}

	
	@GetMapping("/ajouterAmi")
	public String showAjouterAmiForm() {
		return "ajouter-ami";
	}

	
	@PostMapping("/ajouterAmi")
	public String ajouterAmi(@RequestParam String adresseEmailAmi, ModelMap model, Authentication authentication) {
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail(userDetails.getUsername());
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

	/*
	@PostMapping("/ajouterAmi")
	public String ajouterAmi(@RequestParam String adresseEmailAmi, ModelMap model, Authentication authentication) {
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    UtilisateurDTO utilisateurActuelDTO = utilisateurService.findByAdresseEmail(userDetails.getUsername());
	    UtilisateurDTO amiDTO = utilisateurService.findByAdresseEmail(adresseEmailAmi);

	    if (amiDTO != null) {
	        if (!utilisateurActuelDTO.getAmis().contains(amiDTO)) {
	            utilisateurService.ajouterAmi(utilisateurActuelDTO, amiDTO);
	            model.addAttribute("success", "Ami ajouté avec succès.");
	        } else {
	            model.addAttribute("error", "Cet utilisateur est déjà dans votre liste d'amis.");
	        }
	    } else {
	        model.addAttribute("error", "Aucun utilisateur trouvé avec cette adresse e-mail.");
	    }

	    return "ajouter-ami";
	}

	*/
	

	@GetMapping("/effectuerDepot")
    public String showEffectuerDepotForm() {
        return "effectuerDepot";
    }

    @PostMapping("/effectuerDepot")
    public String effectuerDepot(@RequestParam BigDecimal montant, ModelMap model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail(userDetails.getUsername());
        
        if (utilisateurActuel != null) {
            utilisateurService.effectuerDepot(utilisateurActuel, montant);
            model.addAttribute("success", "Dépôt effectué avec succès.");
        } else {
            model.addAttribute("error", "Utilisateur introuvable.");
        }

        return "effectuerDepot";
    }
	    
    @GetMapping("/effectuerRetrait")
    public String showEffectuerRetraitForm() {
        return "effectuerRetrait";
    }

    
    @PostMapping("/effectuerRetrait")
    public String effectuerRetrait(@RequestParam BigDecimal montant, ModelMap model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail(userDetails.getUsername());

        if (utilisateurActuel != null) {
            try {
                utilisateurService.effectuerRetrait(utilisateurActuel, montant);
                model.addAttribute("success", "Retrait effectué avec succès.");
            } catch (InsufficientBalanceException e) {
                model.addAttribute("error", "Solde insuffisant pour effectuer le retrait.");
            }
        } else {
            model.addAttribute("error", "Utilisateur introuvable.");
        }

        // Réutiliser la même vue pour afficher les messages
        return "effectuerRetrait";
    }
    
	
	@GetMapping("/effectuerVirement")
	public String showEffectuerVirementForm(ModelMap model, Authentication authentication) {
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail(userDetails.getUsername());
	    
	    // Chargez la liste des amis de l'utilisateur authentifié
	    List<Utilisateur> amis = utilisateurActuel.getAmis();
	    
	    model.addAttribute("amis", amis);
	    
	    return "effectuerVirement";
	}
    
    @PostMapping("/effectuerVirement")
    public String effectuerVirement(
        @RequestParam String adresseEmailBeneficiaire,
        @RequestParam BigDecimal montant,
        ModelMap model,
        Authentication authentication
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail(userDetails.getUsername());
        Utilisateur beneficiaire = utilisateurService.findByAdresseEmail(adresseEmailBeneficiaire);

        if (beneficiaire != null) {
            try {
                utilisateurService.effectuerVirement(utilisateurActuel.getAdresseEmail(), beneficiaire.getAdresseEmail(), montant);
                model.addAttribute("success", "Virement effectué avec succès.");
            } catch (InsufficientBalanceException e) {
                model.addAttribute("error", "Solde insuffisant pour effectuer le virement.");
            }
        } else {
            model.addAttribute("error", "Bénéficiaire introuvable.");
        }

        return "effectuerVirement";
    }

	  @GetMapping("/historiqueOperations")
	  public String showHistoriqueOperations(ModelMap model, Authentication authentication) {
	      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	      Utilisateur utilisateurActuel = utilisateurService.findByAdresseEmail(userDetails.getUsername());

	      if (utilisateurActuel != null) {
	          List<Operation> operations = operationService.findByUtilisateur(utilisateurActuel);
	          List<Transfert> transferts = transfertService.getVirementsByUtilisateurEmetteur(utilisateurActuel);
	          model.addAttribute("operations", operations);
	          model.addAttribute("transferts", transferts);
	      } else {
	          model.addAttribute("error", "Utilisateur introuvable.");
	      }

	      return "historique-operations";
	  }

	  @GetMapping("/login")
	  public String login() {
	    return "login";
	  }
	  
	  @GetMapping("/home")
	  public String home() {
	    return "home";
	  }
}


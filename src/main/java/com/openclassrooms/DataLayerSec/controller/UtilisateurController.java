package com.openclassrooms.DataLayerSec.controller;

import java.math.BigDecimal;
import java.util.List;
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
import com.openclassrooms.DataLayerSec.dto.OperationDTO;
import com.openclassrooms.DataLayerSec.dto.TransfertDTO;
import com.openclassrooms.DataLayerSec.exceptions.EmailExistsException;
import com.openclassrooms.DataLayerSec.exceptions.InsufficientBalanceException;
import com.openclassrooms.DataLayerSec.service.OperationService;
import com.openclassrooms.DataLayerSec.service.TransfertService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;

@Controller
public class UtilisateurController {
	private UtilisateurService utilisateurService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private TransfertService transfertService;

	@Autowired
	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	// Affiche le formulaire d'enregistrement de l'utilisateur
	@GetMapping("/enregistrerUtilisateur")
	public String showEnregistrerUtilisateurForm(@ModelAttribute("utilisateurDTO") UtilisateurDTO utilisateurDTO) {
		return "enregistrer-utilisateur";
	}

	// Enregistre un nouvel utilisateur
	@PostMapping("/enregistrerUtilisateur")
	public String enregistrerUtilisateur(@ModelAttribute UtilisateurDTO utilisateurDTO,
			RedirectAttributes redirectAttributes) {
		try {
			utilisateurService.addUtilisateur(utilisateurDTO);
			redirectAttributes.addFlashAttribute("success", "L'utilisateur a été enregistré avec succès.");
		} catch (EmailExistsException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/enregistrerUtilisateur";
	}

	// Affiche le formulaire d'ajout d'ami
	@GetMapping("/ajouterAmi")
	public String showAjouterAmiForm() {
		return "ajouter-ami";
	}

	// Ajoute un ami à l'utilisateur actuel
	@PostMapping("/ajouterAmi")
	public String ajouterAmi(@RequestParam String adresseEmailAmi, ModelMap model, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		UtilisateurDTO utilisateurActuelDTO = utilisateurService.findByAdresseEmailDTO(userDetails.getUsername());
		UtilisateurDTO amiDTO = utilisateurService.findByAdresseEmailDTO(adresseEmailAmi);
		try {
			utilisateurService.ajouterAmi(utilisateurActuelDTO, amiDTO);
			model.addAttribute("success", "Ami ajouté avec succès.");
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "ajouter-ami";
	}

	// Affiche le formulaire d'effectuer un dépôt
	@GetMapping("/effectuerDepot")
	public String showEffectuerDepotForm() {
		return "effectuerDepot";
	}

	// Effectue un dépôt sur le compte de l'utilisateur actuel
	@PostMapping("/effectuerDepot")
	public String effectuerDepot(@RequestParam BigDecimal montant, ModelMap model, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		UtilisateurDTO utilisateurActuelDTO = utilisateurService.findByAdresseEmailDTO(userDetails.getUsername());
		try {
			utilisateurService.effectuerDepot(utilisateurActuelDTO, montant);
			model.addAttribute("success", "Dépôt effectué avec succès.");
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "effectuerDepot";
	}

	// Affiche le formulaire d'effectuer un retrait
	@GetMapping("/effectuerRetrait")
	public String showEffectuerRetraitForm() {
		return "effectuerRetrait";
	}

	// Effectue un retrait du compte de l'utilisateur actuel
	@PostMapping("/effectuerRetrait")
	public String effectuerRetrait(@RequestParam BigDecimal montant, ModelMap model, Authentication authentication)
			throws Exception {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		UtilisateurDTO utilisateurActuelDTO = utilisateurService.findByAdresseEmailDTO(userDetails.getUsername());
		try {
			utilisateurService.effectuerRetrait(utilisateurActuelDTO, montant);
			model.addAttribute("success", "Retrait effectué avec succès.");
		} catch (InsufficientBalanceException e) {
			model.addAttribute("error", "Solde insuffisant pour effectuer le retrait.");
		}
		return "effectuerRetrait";
	}

	// Affiche le formulaire d'effectuer un virement
	@GetMapping("/effectuerVirement")
	public String showEffectuerVirementForm(ModelMap model, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		model.addAttribute("amis", utilisateurService.getAmis(userDetails.getUsername()));
		return "effectuerVirement";
	}

	// Effectue un virement vers un bénéficiaire
	@PostMapping("/effectuerVirement")
	public String effectuerVirement(@RequestParam String adresseEmailBeneficiaire, @RequestParam BigDecimal montant,
			@RequestParam String description, ModelMap model, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		UtilisateurDTO utilisateurActuelDTO = utilisateurService.findByAdresseEmailDTO(userDetails.getUsername());
		UtilisateurDTO beneficiaireDTO = utilisateurService.findByAdresseEmailDTO(adresseEmailBeneficiaire);
		try {
			utilisateurService.effectuerVirement(utilisateurActuelDTO.getAdresseEmail(),
					beneficiaireDTO.getAdresseEmail(), montant, description);
			model.addAttribute("success", "Virement effectué avec succès.");
		} catch (InsufficientBalanceException e) {
			model.addAttribute("error", "Solde insuffisant pour effectuer le virement.");
		}
		return "effectuerVirement";
	}

	// Affiche l'historique des opérations de l'utilisateur actuel
	@GetMapping("/historiqueOperations")
	public String showHistoriqueOperations(ModelMap model, Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		UtilisateurDTO utilisateurActuelDTO = utilisateurService.findByAdresseEmailDTO(userDetails.getUsername());
		List<OperationDTO> operations = operationService.findByUtilisateurDTO(utilisateurActuelDTO);
		List<TransfertDTO> transferts = transfertService.getVirementsByUtilisateurEmetteurDTO(utilisateurActuelDTO);
		model.addAttribute("operations", operations);
		model.addAttribute("transferts", transferts);
		return "historique-operations";
	}

	// Affiche la page de connexion
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// Affiche la page d'accueil
	@GetMapping("/home")
	public String home() {
		return "home";
	}
}

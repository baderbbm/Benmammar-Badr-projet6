package com.openclassrooms.DataLayerSec.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.exceptions.EmailExistsException;
import com.openclassrooms.DataLayerSec.exceptions.InsufficientBalanceException;
import com.openclassrooms.DataLayerSec.model.NatureOperation;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Transfert;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;
import com.openclassrooms.DataLayerSec.repository.TransfertRepository;
import com.openclassrooms.DataLayerSec.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private TransfertRepository transfertRepository;

	@Autowired
	private OperationRepository operationRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;


	public UtilisateurDTO addUtilisateur(UtilisateurDTO utilisateurDTO) {
		try {
			String encryptedPassword = passwordEncoder.encode(utilisateurDTO.getMotDePasse());
			utilisateurDTO.setMotDePasse(encryptedPassword);
			return convertToDTO(utilisateurRepository.save(convertToEntity(utilisateurDTO)));
		} catch (DataIntegrityViolationException ex) {
			throw new EmailExistsException("L'email existe déjà.");
		}
	}

	  public UtilisateurDTO findByAdresseEmailDTO(String adresseEmail) { 
		  return convertToDTO(findByAdresseEmail(adresseEmail)); 
		  }
	  
	 
	public Utilisateur findByAdresseEmail(String adresseEmail) {
		return utilisateurRepository.findByAdresseEmail(adresseEmail);
	}

	public void ajouterAmi(UtilisateurDTO utilisateurDTO, UtilisateurDTO amiDTO) throws Exception {
		if (amiDTO==null) throw new Exception ("Aucun utilisateur trouvé avec cette adresse e-mail.");
		Utilisateur utilisateur = findByAdresseEmail(utilisateurDTO.getAdresseEmail());
		Utilisateur ami = findByAdresseEmail(amiDTO.getAdresseEmail());
		if (utilisateur.getAmis().contains(ami)) throw new Exception ("Cet utilisateur est déjà dans votre liste d'amis.");
		utilisateur.getAmis().add(ami);
		utilisateurRepository.save(utilisateur);	
	}

	public void effectuerDepot(UtilisateurDTO utilisateurDTO, BigDecimal montant) throws Exception {
		if (utilisateurDTO==null) throw new Exception ("Utilisateur introuvable.");
		BigDecimal montantAvecPrelevement = montant.multiply(BigDecimal.valueOf(0.995)); // 0.5% de prélèvement
		Utilisateur utilisateur=findByAdresseEmail(utilisateurDTO.getAdresseEmail());
		utilisateur.effectuerDepot(montantAvecPrelevement);
		utilisateurRepository.save(utilisateur);
		Operation operation = new Operation();
		operation.setUtilisateur(utilisateur);
		operation.setMontant(montantAvecPrelevement);
		operation.setDateetheureoperation(LocalDateTime.now());
		operation.setNatureoperation(NatureOperation.depot);
		operationRepository.save(operation);
	}

	public void effectuerRetrait(UtilisateurDTO utilisateurDTO, BigDecimal montant) throws Exception {
		if (utilisateurDTO==null) throw new Exception ("Utilisateur introuvable.");
		BigDecimal montantAvecPrelevement = montant.multiply(BigDecimal.valueOf(0.995)); // 0.5% de prélèvement
		Utilisateur utilisateur=findByAdresseEmail(utilisateurDTO.getAdresseEmail());		
		if (utilisateur.getSoldeDuCompte().compareTo(montant) >= 0) {
			utilisateur.effectuerRetrait(montantAvecPrelevement);
			utilisateurRepository.save(utilisateur);

			Operation operation = new Operation();
			operation.setUtilisateur(utilisateur);
			operation.setMontant(montantAvecPrelevement);
			operation.setDateetheureoperation(LocalDateTime.now());
			operation.setNatureoperation(NatureOperation.retrait);
			operationRepository.save(operation);
		} else {
			throw new InsufficientBalanceException("Solde insuffisant pour effectuer le retrait.");
		}
	}

	public Utilisateur convertToEntity(UtilisateurDTO utilisateurDTO) {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setUtilisateurId(utilisateurDTO.getUtilisateurId());
		utilisateur.setPrenom(utilisateurDTO.getPrenom());
		utilisateur.setNom(utilisateurDTO.getNom());
		utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());
		utilisateur.setAdresseEmail(utilisateurDTO.getAdresseEmail());
		utilisateur.setSoldeDuCompte(utilisateurDTO.getSoldeDuCompte());
		return utilisateur;
	}

	public UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
		if (utilisateur == null) {
			return null;
		}
		UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
		utilisateurDTO.setUtilisateurId(utilisateur.getUtilisateurId());
		utilisateurDTO.setPrenom(utilisateur.getPrenom());
		utilisateurDTO.setNom(utilisateur.getNom());
		utilisateurDTO.setMotDePasse(utilisateur.getMotDePasse());
		utilisateurDTO.setAdresseEmail(utilisateur.getAdresseEmail());
		utilisateurDTO.setSoldeDuCompte(utilisateur.getSoldeDuCompte());

		return utilisateurDTO;
	}
	 public void effectuerVirement(String adresseEmailEmetteur, String adresseEmailBeneficiaire, BigDecimal montant) {
	        BigDecimal montantAvecPrelevement = montant.multiply(BigDecimal.valueOf(0.995)); // 0.5% de prélèvement
	        Utilisateur emetteur = findByAdresseEmail(adresseEmailEmetteur);
	        Utilisateur beneficiaire = findByAdresseEmail(adresseEmailBeneficiaire);
	            BigDecimal soldeEmetteur = emetteur.getSoldeDuCompte();
	            if (soldeEmetteur.compareTo(montant) >= 0) {
	                emetteur.effectuerRetrait(montant);
	                beneficiaire.effectuerDepot(montantAvecPrelevement);
	                // Mise à jour des utilisateurs dans la base de données
	                utilisateurRepository.save(emetteur);
	                utilisateurRepository.save(beneficiaire);
	                // Création et sauvegarde du transfert
	                Transfert transfert = new Transfert();
	                transfert.setUtilisateurEmetteur(emetteur);
	                transfert.setUtilisateurBeneficiaire(beneficiaire);
	                transfert.setMontant(montant);
	                transfert.setDateHeureTransfert(LocalDateTime.now());
	                transfertRepository.save(transfert);
	            } else {
	                throw new InsufficientBalanceException("Solde insuffisant pour effectuer le virement.");
	            }
	    }
	 
		public List<UtilisateurDTO> getAmis(String emailAddress) {
			List<UtilisateurDTO> amis = new ArrayList<>();
			for (Utilisateur ami : findByAdresseEmail(emailAddress).getAmis()) {
				amis.add(convertToDTO(ami));
			}
			return amis;
		}
}

package com.openclassrooms.DataLayerSec.service;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.NatureOperation;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Transfert;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;
import com.openclassrooms.DataLayerSec.repository.TransfertRepository;
import com.openclassrooms.DataLayerSec.repository.UtilisateurRepository;
 
@Service
public class UtilisateurService {
 
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private TransfertRepository transfertRepository;
	
	@Autowired
	private OperationRepository operationRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	public Iterable<Utilisateur> getUsers() {
		return utilisateurRepository.findAll();
	}
	
	public  Optional<Utilisateur>  getUserById(Integer id) {
		return utilisateurRepository.findById(id); 
		//return utilisateurRepository.findByUtilisateurId(id); 
	}	
	
	
	// exception personnalisée 
	public class EmailExistsException extends RuntimeException {
	    public EmailExistsException(String message) {
	        super(message);
	    }
	}

	
	public UtilisateurDTO addUtilisateur(UtilisateurDTO utilisateurDTO) {
	    try {
	        String encryptedPassword = passwordEncoder.encode(utilisateurDTO.getMotDePasse());
	        utilisateurDTO.setMotDePasse(encryptedPassword);
	        return convertToDTO(utilisateurRepository.save(convertToEntity(utilisateurDTO)));
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
	 
	 /*
	 public UtilisateurDTO findByAdresseEmail(String adresseEmail) {
	        return convertToDTO(utilisateurRepository.findByAdresseEmail(adresseEmail));
	    }
	 */
	 
	 public Utilisateur findByAdresseEmail(String adresseEmail) {
	        return utilisateurRepository.findByAdresseEmail(adresseEmail);
	    }
	 

	 /*
	 public void ajouterAmi(UtilisateurDTO utilisateurDTO, UtilisateurDTO amiDTO) {
		    Utilisateur utilisateur = convertToEntity(utilisateurDTO);
		    Utilisateur ami = convertToEntity(amiDTO);
		    utilisateur.getAmis().add(ami);
		    utilisateurRepository.save(utilisateur);
		}
	 
	 */

	 public void ajouterAmi(Utilisateur utilisateur, Utilisateur ami) {
		    utilisateur.getAmis().add(ami);
		    utilisateurRepository.save(utilisateur);
		}

	    
	    public void effectuerDepot(Utilisateur utilisateur, BigDecimal montant) {
	        BigDecimal montantAvecPrelevement = montant.multiply(BigDecimal.valueOf(0.995)); // 0.5% de prélèvement
	        utilisateur.effectuerDepot(montantAvecPrelevement);
	        utilisateurRepository.save(utilisateur);
	        
	        Operation operation = new Operation();
	        operation.setUtilisateur(utilisateur);
	        operation.setMontant(montantAvecPrelevement);
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
	        BigDecimal montantAvecPrelevement = montant.multiply(BigDecimal.valueOf(0.995)); // 0.5% de prélèvement
	        if (utilisateur.getSoldeDuCompte().compareTo(montant) >= 0) {
	            utilisateur.effectuerRetrait(montantAvecPrelevement );
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

	        if (emetteur != null && beneficiaire != null) {
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
	        } else {
	            throw new IllegalArgumentException("Utilisateurs introuvables.");
	        }
	    }
}

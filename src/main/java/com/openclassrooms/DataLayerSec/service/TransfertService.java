package com.openclassrooms.DataLayerSec.service;
 
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.DataLayerSec.dto.TransfertDTO;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Transfert;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.TransfertRepository;
import jakarta.transaction.Transactional;
 
@Service
@Transactional
public class TransfertService {
 
	@Autowired
	private TransfertRepository transfertRepository;
	 @Autowired
		private UtilisateurService utilisateurService;
	
	public List<Transfert> getVirementsByUtilisateurEmetteur(UtilisateurDTO utilisateurEmetteurDTO) {
		Utilisateur utilisateurEmetteur=utilisateurService.findByAdresseEmail(utilisateurEmetteurDTO.getAdresseEmail());		
        return transfertRepository.findByUtilisateurEmetteur(utilisateurEmetteur);
    }
	
	public List<TransfertDTO> getVirementsByUtilisateurEmetteurDTO(UtilisateurDTO utilisateurEmetteurDTO) {
			
        return convertToTransfertDTOList(getVirementsByUtilisateurEmetteur(utilisateurEmetteurDTO));
    }
	
	public static List<TransfertDTO> convertToTransfertDTOList(List<Transfert> transferts) {
        List<TransfertDTO> transfertDTOList = new ArrayList<>();
        for (Transfert transfert : transferts) {
            TransfertDTO transfertDTO = new TransfertDTO();
            transfertDTO.setTransfertId(transfert.getTransfertId());
            UtilisateurDTO emetteurDTO = new UtilisateurDTO();
            transfertDTO.setUtilisateurEmetteurDTO(emetteurDTO); 
            UtilisateurDTO beneficiaireDTO = new UtilisateurDTO();
            Utilisateur utilisateurBeneficiaire = transfert.getUtilisateurBeneficiaire();
            if (utilisateurBeneficiaire != null)
            beneficiaireDTO.setNom(utilisateurBeneficiaire.getNom());
            transfertDTO.setUtilisateurBeneficiaireDTO(beneficiaireDTO); 
            transfertDTO.setMontant(transfert.getMontant());
            transfertDTO.setDateHeureTransfert(transfert.getDateHeureTransfert());
            transfertDTO.setDescription(transfert.getDescription());
            transfertDTOList.add(transfertDTO);
        }
        return transfertDTOList;
    }
	}

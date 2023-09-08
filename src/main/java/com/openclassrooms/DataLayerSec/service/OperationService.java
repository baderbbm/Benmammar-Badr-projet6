package com.openclassrooms.DataLayerSec.service;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.DataLayerSec.dto.TransfertDTO;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Transfert;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.TransfertRepository;
 
@Service
public class TransfertService {
 
	@Autowired
	private TransfertRepository transfertRepository;
	 @Autowired
		private UtilisateurService utilisateurService;
	
	public Iterable<Transfert> getTransferts() {
		return transfertRepository.findAll();
	}
	
	public Optional<Transfert> getTransfertById(Integer id) {
		return transfertRepository.findById(id); 
	}	
	
	public Transfert addTransfert(Transfert transfert) {
		return transfertRepository.save(transfert);		
	}
	
	public void deleteTransfertById(Integer id) {
		transfertRepository.deleteById(id);
	}
	
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
            beneficiaireDTO.setNom(transfert.getUtilisateurBeneficiaire().getNom());
            transfertDTO.setUtilisateurBeneficiaireDTO(beneficiaireDTO); 
            transfertDTO.setMontant(transfert.getMontant());
            transfertDTO.setDateHeureTransfert(transfert.getDateHeureTransfert());
            transfertDTOList.add(transfertDTO);
        }

        return transfertDTOList;
    }

}

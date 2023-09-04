package com.openclassrooms.DataLayerSec.service;
 
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Operation;
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

}

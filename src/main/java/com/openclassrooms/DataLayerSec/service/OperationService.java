package com.openclassrooms.DataLayerSec.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;

@Service
public class OperationService {
    
    private final OperationRepository operationRepository;

    @Autowired
	private UtilisateurService utilisateurService;
    
    @Autowired
    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> findByUtilisateur(UtilisateurDTO utilisateurDTO) {
		Utilisateur utilisateur=utilisateurService.findByAdresseEmail(utilisateurDTO.getAdresseEmail());		
        return operationRepository.findByUtilisateur(utilisateur);
    }
}

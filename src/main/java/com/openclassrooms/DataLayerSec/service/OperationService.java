package com.openclassrooms.DataLayerSec.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.DataLayerSec.dto.OperationDTO;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
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

    public List<OperationDTO> findByUtilisateurDTO(UtilisateurDTO utilisateurDTO) {
          return convertToOperationDTOList(findByUtilisateur(utilisateurDTO));
      }
   
public static List<OperationDTO> convertToOperationDTOList(List<Operation> operations) {
    List<OperationDTO> operationDTOList = new ArrayList<>();

    for (Operation operation : operations) {
        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setOperationId(operation.getOperationId());
         UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        operationDTO.setUtilisateur(utilisateurDTO); 
        operationDTO.setMontant(operation.getMontant());
        operationDTO.setDateetheureoperation(operation.getDateetheureoperation());
        operationDTO.setNatureoperation(operation.getNatureoperation());
        operationDTOList.add(operationDTO);
    }
    return operationDTOList;
}
}

package com.openclassrooms.DataLayerSec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.openclassrooms.DataLayerSec.dto.OperationDTO;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.NatureOperation;
import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;
import com.openclassrooms.DataLayerSec.service.OperationService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

	 @Mock
	    private OperationRepository operationRepository;

	    @Mock
	    private UtilisateurService utilisateurService;

	    @InjectMocks
	    private OperationService operationService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }
   
    @Test
    public void testConvertToOperationDTOList() {
        List<Operation> operationsSimulees = new ArrayList<>();
        Operation operation1 = new Operation();
        operation1.setOperationId(1);
        operation1.setMontant(BigDecimal.valueOf(50.0));
        operation1.setDateetheureoperation(LocalDateTime.now());
        operation1.setNatureoperation(NatureOperation.depot);
        operationsSimulees.add(operation1);
        Operation operation2 = new Operation();
        operation2.setOperationId(2);
        operation2.setMontant(BigDecimal.valueOf(75.0));
        operation2.setDateetheureoperation(LocalDateTime.now().minusDays(1));
        operation2.setNatureoperation(NatureOperation.retrait);
        operationsSimulees.add(operation2);
        List<OperationDTO> operationDTOs = OperationService.convertToOperationDTOList(operationsSimulees);
        assertEquals(2, operationDTOs.size());
        OperationDTO operationDTO1 = operationDTOs.get(0);
        assertEquals(1, operationDTO1.getOperationId());
        assertEquals(BigDecimal.valueOf(50.0), operationDTO1.getMontant());
        assertEquals(NatureOperation.depot, operationDTO1.getNatureoperation());
        OperationDTO operationDTO2 = operationDTOs.get(1);
        assertEquals(2, operationDTO2.getOperationId());
        assertEquals(BigDecimal.valueOf(75.0), operationDTO2.getMontant());
        assertEquals(NatureOperation.retrait, operationDTO2.getNatureoperation());
    }
    
    @Test
    public void testFindByUtilisateur() {
        // Créez un UtilisateurDTO simulé
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setAdresseEmail("utilisateur@example.com");

        // Créez un Utilisateur simulé
        Utilisateur utilisateurSimule = new Utilisateur();
        utilisateurSimule.setAdresseEmail("utilisateur@example.com");

        // Créez une liste d'opérations simulées
        List<Operation> operationsSimulees = new ArrayList<>();
        Operation operation1 = new Operation();
        operation1.setOperationId(1);
        operation1.setMontant(BigDecimal.valueOf(50.0));
        operation1.setDateetheureoperation(LocalDateTime.now());
        operation1.setNatureoperation(NatureOperation.depot);
        operation1.setUtilisateur(utilisateurSimule);
        operationsSimulees.add(operation1);

        // Configurez les comportements simulés pour les méthodes du service
        when(utilisateurService.findByAdresseEmail(utilisateurDTO.getAdresseEmail()))
                .thenReturn(utilisateurSimule);
        when(operationRepository.findByUtilisateur(utilisateurSimule)).thenReturn(operationsSimulees);

        // Appelez la méthode à tester
        List<Operation> result = operationService.findByUtilisateur(utilisateurDTO);

        // Effectuez des assertions
        assertEquals(1, result.size());
        Operation operationResult = result.get(0);
        assertEquals(1, operationResult.getOperationId());
        assertEquals(BigDecimal.valueOf(50.0), operationResult.getMontant());
        assertEquals(NatureOperation.depot, operationResult.getNatureoperation());
    }
        
}

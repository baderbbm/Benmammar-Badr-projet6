package com.openclassrooms.DataLayerSec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.openclassrooms.DataLayerSec.dto.TransfertDTO;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.model.Transfert;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.TransfertRepository;
import com.openclassrooms.DataLayerSec.service.TransfertService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;

public class TransfertServiceTest {
	
	  @Mock
	    private TransfertRepository transfertRepository;

	    @Mock
	    private UtilisateurService utilisateurService;

	    @InjectMocks
	    private TransfertService transfertService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }

    @Test
    public void testConvertToTransfertDTOList() {
        List<Transfert> transfertsSimules = new ArrayList<>();
        Transfert transfert1 = new Transfert();
        transfert1.setTransfertId(1);
        transfert1.setMontant(BigDecimal.valueOf(100.0));
        transfert1.setDateHeureTransfert(LocalDateTime.now());
        Utilisateur emetteurSimule = new Utilisateur();
        emetteurSimule.setNom("EmetteurSimule");
        transfert1.setUtilisateurEmetteur(emetteurSimule);
        Utilisateur beneficiaireSimule = new Utilisateur();
        beneficiaireSimule.setNom("BeneficiaireSimule");
        transfert1.setUtilisateurBeneficiaire(beneficiaireSimule);
        transfertsSimules.add(transfert1);
        List<TransfertDTO> transfertDTOs = TransfertService.convertToTransfertDTOList(transfertsSimules);
        assertEquals(1, transfertDTOs.size());
        TransfertDTO transfertDTO = transfertDTOs.get(0);
        assertEquals(1, transfertDTO.getTransfertId());
        assertEquals(BigDecimal.valueOf(100.0), transfertDTO.getMontant());
        assertEquals(transfert1.getDateHeureTransfert(), transfertDTO.getDateHeureTransfert());
    }
    
    @Test
    public void testGetVirementsByUtilisateurEmetteur() {
        UtilisateurDTO utilisateurEmetteurDTO = new UtilisateurDTO();
        utilisateurEmetteurDTO.setAdresseEmail("emetteur@example.com");
        Utilisateur utilisateurEmetteurSimule = new Utilisateur();
        utilisateurEmetteurSimule.setAdresseEmail("emetteur@example.com");
        List<Transfert> transfertsSimules = new ArrayList<>();
        Transfert transfert1 = new Transfert();
        transfert1.setTransfertId(1);
        transfertsSimules.add(transfert1);
        when(utilisateurService.findByAdresseEmail(utilisateurEmetteurDTO.getAdresseEmail()))
                .thenReturn(utilisateurEmetteurSimule);
        when(transfertRepository.findByUtilisateurEmetteur(utilisateurEmetteurSimule))
                .thenReturn(transfertsSimules);
        List<Transfert> result = transfertService.getVirementsByUtilisateurEmetteur(utilisateurEmetteurDTO);
        assertEquals(1, result.size());
        Transfert transfertResult = result.get(0);
        assertEquals(1, transfertResult.getTransfertId());
    }
}


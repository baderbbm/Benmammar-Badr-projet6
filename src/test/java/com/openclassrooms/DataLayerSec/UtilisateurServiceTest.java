package com.openclassrooms.DataLayerSec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.exceptions.EmailExistsException;
import com.openclassrooms.DataLayerSec.exceptions.InsufficientBalanceException;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.OperationRepository;
import com.openclassrooms.DataLayerSec.repository.TransfertRepository;
import com.openclassrooms.DataLayerSec.repository.UtilisateurRepository;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;
import org.apache.commons.math3.util.Precision;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UtilisateurServiceTest {
	
	
	@Mock
	private OperationRepository operationRepository;
	
	@Mock
	private TransfertRepository transfertRepository;

	@Mock
	private UtilisateurRepository utilisateurRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private UtilisateurService utilisateurService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddUtilisateur() {
		UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
		utilisateurDTO.setAdresseEmail("test@example.com");
		utilisateurDTO.setMotDePasse("password");
		utilisateurDTO.setSoldeDuCompte(BigDecimal.ZERO);
		when(utilisateurRepository.findByAdresseEmail("test@example.com")).thenReturn(null);
		when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
		when(utilisateurRepository.save(any())).thenAnswer(invocation -> {
			Utilisateur utilisateur = invocation.getArgument(0);
			utilisateur.setAdresseEmail("test@example.com");
			return utilisateur;
		});
		UtilisateurDTO savedUtilisateurDTO = utilisateurService.addUtilisateur(utilisateurDTO);
		assertThat(savedUtilisateurDTO).isNotNull();
		assertThat(savedUtilisateurDTO.getAdresseEmail()).isEqualTo("test@example.com");
		assertThat(savedUtilisateurDTO.getMotDePasse()).isEqualTo("hashedPassword");
	}

	@Test
	public void testAddUtilisateurWithExistingEmail() {
		UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
		utilisateurDTO.setAdresseEmail("test@example.com");
		utilisateurDTO.setMotDePasse("password");
		utilisateurDTO.setSoldeDuCompte(BigDecimal.ZERO);
		when(utilisateurRepository.findByAdresseEmail("test@example.com")).thenReturn(new Utilisateur());
		try {
			utilisateurService.addUtilisateur(utilisateurDTO);
		} catch (EmailExistsException e) {
			assertThat(e.getMessage()).isEqualTo("L'email existe déjà.");
		}
	}

	
	@Test
    public void testEffectuerDepot() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setAdresseEmail("test@example.com");
        Utilisateur utilisateurSimule = new Utilisateur();
        utilisateurSimule.setSoldeDuCompte(BigDecimal.valueOf(100.0));
        when(utilisateurRepository.findByAdresseEmail("test@example.com")).thenReturn(utilisateurSimule);
        BigDecimal montant = BigDecimal.valueOf(1000.0);
        utilisateurService.effectuerDepot(utilisateurDTO, montant);
        verify(utilisateurRepository, times(1)).save(utilisateurSimule);
        assertTrue(Precision.equals(BigDecimal.valueOf(1095.0000).doubleValue(), 
       utilisateurSimule.getSoldeDuCompte().doubleValue(), 0.001));
    }

	

	
	  @Test public void testEffectuerRetraitWithSufficientBalance() throws Exception {
	  UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
	  utilisateurDTO.setAdresseEmail("test@example.com");
	  utilisateurDTO.setMotDePasse("password");
	  utilisateurDTO.setSoldeDuCompte(BigDecimal.valueOf(100.0));
      Utilisateur utilisateur = new Utilisateur();
	  utilisateur.setSoldeDuCompte(BigDecimal.valueOf(100.0));
	  when(utilisateurRepository.findByAdresseEmail("test@example.com")).thenReturn(utilisateur);
	  utilisateurService.effectuerRetrait(utilisateurDTO,BigDecimal.valueOf(50.0));
	 verify(utilisateurRepository, times(1)).save(any()); 
	 }
	 
	 

	@Test
	public void testEffectuerRetraitWithInsufficientBalance() throws Exception {
		UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
		utilisateurDTO.setAdresseEmail("test@example.com");
		utilisateurDTO.setMotDePasse("password");
		utilisateurDTO.setSoldeDuCompte(BigDecimal.valueOf(50.0));
		Utilisateur utilisateur = mock(Utilisateur.class);
		when(utilisateur.getSoldeDuCompte()).thenReturn(BigDecimal.valueOf(50.0));
		when(utilisateurRepository.findByAdresseEmail("test@example.com")).thenReturn(utilisateur);
		try {
			utilisateurService.effectuerRetrait(utilisateurDTO, BigDecimal.valueOf(100.0));
		} catch (InsufficientBalanceException e) {
			assertThat(e.getMessage()).isEqualTo("Solde insuffisant pour effectuer le retrait.");
		}
	}
	
	@Test
    public void testAjouterAmi() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setAdresseEmail("utilisateur@example.com");
        UtilisateurDTO amiDTO = new UtilisateurDTO();
        amiDTO.setAdresseEmail("ami@example.com");
        Utilisateur utilisateurSimule = new Utilisateur();
        Utilisateur amiSimule = new Utilisateur();
        when(utilisateurRepository.findByAdresseEmail("utilisateur@example.com")).thenReturn(utilisateurSimule);
        when(utilisateurRepository.findByAdresseEmail("ami@example.com")).thenReturn(amiSimule);
        utilisateurService.ajouterAmi(utilisateurDTO, amiDTO);
        assertTrue(utilisateurSimule.getAmis().contains(amiSimule));
        verify(utilisateurRepository, times(1)).save(utilisateurSimule);
    }
	
	@Test
    public void testGetAmis() throws Exception {
        String emailAddress = "utilisateur@example.com";
        Utilisateur utilisateurSimule = new Utilisateur();
        utilisateurSimule.setAdresseEmail(emailAddress);
        Utilisateur ami1Simule = new Utilisateur();
        ami1Simule.setAdresseEmail("ami1@example.com");
        Utilisateur ami2Simule = new Utilisateur();
        ami2Simule.setAdresseEmail("ami2@example.com");
        utilisateurSimule.getAmis().add(ami1Simule);
        utilisateurSimule.getAmis().add(ami2Simule);
        when(utilisateurRepository.findByAdresseEmail(emailAddress)).thenReturn(utilisateurSimule);
        List<UtilisateurDTO> amisDTO = utilisateurService.getAmis(emailAddress);
        verify(utilisateurRepository, times(1)).findByAdresseEmail(emailAddress);
        assertEquals(2, amisDTO.size());
        assertEquals("ami1@example.com", amisDTO.get(0).getAdresseEmail());
        assertEquals("ami2@example.com", amisDTO.get(1).getAdresseEmail());
    }
	
	@Test
    public void testEffectuerVirement() throws Exception {
        String adresseEmailEmetteur = "emetteur@example.com";
        String adresseEmailBeneficiaire = "beneficiaire@example.com";
        BigDecimal montant = BigDecimal.valueOf(100.0);
        Utilisateur emetteurSimule = new Utilisateur();
        emetteurSimule.setSoldeDuCompte(BigDecimal.valueOf(200.0));
        Utilisateur beneficiaireSimule = new Utilisateur();
        beneficiaireSimule.setSoldeDuCompte(BigDecimal.valueOf(50.0));
        when(utilisateurRepository.findByAdresseEmail(adresseEmailEmetteur)).thenReturn(emetteurSimule);
        when(utilisateurRepository.findByAdresseEmail(adresseEmailBeneficiaire)).thenReturn(beneficiaireSimule);
        utilisateurService.effectuerVirement(adresseEmailEmetteur, adresseEmailBeneficiaire, montant);
        assertEquals(BigDecimal.valueOf(100.0), emetteurSimule.getSoldeDuCompte());
        assertTrue(Precision.equals(BigDecimal.valueOf(50.0 + 99.5).doubleValue(), 
        		beneficiaireSimule.getSoldeDuCompte().doubleValue(), 0.001));       
        verify(utilisateurRepository, times(2)).save(any());
        verify(transfertRepository, times(1)).save(any());
    }

}

package com.openclassrooms.DataLayerSec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.openclassrooms.DataLayerSec.controller.UtilisateurController;
import com.openclassrooms.DataLayerSec.dto.UtilisateurDTO;
import com.openclassrooms.DataLayerSec.exceptions.EmailExistsException;
import com.openclassrooms.DataLayerSec.exceptions.InsufficientBalanceException;
import com.openclassrooms.DataLayerSec.service.OperationService;
import com.openclassrooms.DataLayerSec.service.TransfertService;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

@WebMvcTest(controllers = UtilisateurController.class)
@AutoConfigureMockMvc
public class UtilisateurControllerTest {
	
	 @Autowired
	    private UtilisateurController utilisateurController;

	@Mock
	private Authentication authentication;

	@MockBean
	private UserDetails userDetails;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UtilisateurService utilisateurService;

	@MockBean
	private OperationService operationService;

	@MockBean
	private TransfertService transfertService;

	@BeforeEach
    public void setUp() {
        when(utilisateurService.findByAdresseEmailDTO("utilisateur@example.com"))
                .thenReturn(new UtilisateurDTO());
    }

	@Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

	@Test
	@WithMockUser 
	public void testHome() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/home")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("home"));
	}

	@Test
	public void testShowEnregistrerUtilisateurForm() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/enregistrerUtilisateur").with(user("testuser").password("password")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("enregistrer-utilisateur"));
	}
	
	@Test
	public void testShowAjouterAmiForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/ajouterAmi").with(user("testuser").password("password")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("ajouter-ami"));
	}

	@Test
	public void testShowEffectuerDepotForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/effectuerDepot").with(user("testuser").password("password")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("effectuerDepot"));
	}

	@Test
	public void testShowEffectuerRetraitForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/effectuerRetrait").with(user("testuser").password("password")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("effectuerRetrait"));
	}

	@Test
	public void testShowEffectuerVirementForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/effectuerVirement").with(user("testuser").password("password")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("effectuerVirement"));
	}

	@Test
	public void testShowHistoriqueOperations() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/historiqueOperations").with(user("testuser").password("password")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("historique-operations"));
	}
	
	   @Test
	    public void testAjouterAmi_Success() throws Exception {
	        UtilisateurDTO utilisateurActuelDTO = new UtilisateurDTO();
	        utilisateurActuelDTO.setAdresseEmail("utilisateur@example.com");
	        UtilisateurDTO amiDTO = new UtilisateurDTO();
	        amiDTO.setAdresseEmail("ami@example.com");
	        when(authentication.getPrincipal()).thenReturn(new User("utilisateur@example.com", "", Collections.emptyList()));
	        when(utilisateurService.findByAdresseEmailDTO("utilisateur@example.com")).thenReturn(utilisateurActuelDTO);
	        when(utilisateurService.findByAdresseEmailDTO("ami@example.com")).thenReturn(amiDTO);
	        ModelMap model = new ModelMap();
	        String viewName = utilisateurController.ajouterAmi("ami@example.com", model, authentication);
	        verify(utilisateurService, times(1)).ajouterAmi(utilisateurActuelDTO, amiDTO);
	        assert model.containsAttribute("success");
	        assert "ajouter-ami".equals(viewName);
	    }

	    @Test
	    public void testAjouterAmi_Error() throws Exception {
	        when(authentication.getPrincipal()).thenReturn(new User("utilisateur@example.com", "", Collections.emptyList()));
	        when(utilisateurService.findByAdresseEmailDTO(toString())).thenReturn(null); 
	        ModelMap model = new ModelMap();
	        String viewName = utilisateurController.ajouterAmi("ami@example.com", model, authentication);
	        verify(utilisateurService, never()).ajouterAmi(any(UtilisateurDTO.class), any(UtilisateurDTO.class));
	        assert "ajouter-ami".equals(viewName);
	    }
	    
	    @Test
	    @WithMockUser
	    public void testEffectuerDepot_Success() throws Exception {
	        UtilisateurDTO utilisateurActuelDTO = new UtilisateurDTO();
	        utilisateurActuelDTO.setAdresseEmail("utilisateur@example.com");
	        BigDecimal montant = new BigDecimal("100.00");
	        when(authentication.getPrincipal()).thenReturn(new User("utilisateur@example.com", "", Collections.emptyList()));
	        when(utilisateurService.findByAdresseEmailDTO("utilisateur@example.com")).thenReturn(utilisateurActuelDTO);
	        ModelMap model = new ModelMap();
	        String viewName = utilisateurController.effectuerDepot(montant, model, authentication);
	        verify(utilisateurService, times(1)).effectuerDepot(utilisateurActuelDTO, montant);
	        assert model.containsAttribute("success");
	        assert "effectuerDepot".equals(viewName);
	    }

	    @Test
	    public void testEffectuerDepot_Error() throws Exception {
	        BigDecimal montant = new BigDecimal("100.00");
	        UserDetails userDetails = new User("utilisateur@example.com", "", Collections.emptyList());
	        when(authentication.getPrincipal()).thenReturn(userDetails);
	        when(utilisateurService.findByAdresseEmailDTO(userDetails.getUsername())).thenReturn(null); // Simulate an error by not finding the user

	        ResultActions resultActions = mockMvc.perform(
	                MockMvcRequestBuilders.post("/effectuerDepot")
	                        .param("montant", montant.toString())
	                        .with(user("utilisateur@example.com").password("password"))
	        );
	        resultActions
	                .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	        verify(utilisateurService, never()).effectuerDepot(any(UtilisateurDTO.class), any(BigDecimal.class));
	    }

	    @Test
	    public void testEffectuerRetrait_Success() throws Exception {
	        UtilisateurDTO utilisateurActuelDTO = new UtilisateurDTO();
	        utilisateurActuelDTO.setAdresseEmail("utilisateur@example.com");
	        BigDecimal montant = new BigDecimal("50.00");
	        when(authentication.getPrincipal()).thenReturn(new User("utilisateur@example.com", "", Collections.emptyList()));
	        when(utilisateurService.findByAdresseEmailDTO("utilisateur@example.com")).thenReturn(utilisateurActuelDTO);
	        ModelMap model = new ModelMap();
	        String viewName = utilisateurController.effectuerRetrait(montant, model, authentication);
	        verify(utilisateurService, times(1)).effectuerRetrait(utilisateurActuelDTO, montant);
	        assert model.containsAttribute("success");
	        assert "effectuerRetrait".equals(viewName);
	    }

	    @Test
	    public void testEffectuerRetrait_InsufficientBalance() throws Exception {
	        BigDecimal montant = new BigDecimal("100.00");
	        UserDetails userDetails = new User("utilisateur@example.com", "", Collections.emptyList());
	        when(authentication.getPrincipal()).thenReturn(userDetails);
	        when(utilisateurService.findByAdresseEmailDTO(userDetails.getUsername())).thenReturn(new UtilisateurDTO()); 
	        doThrow(new InsufficientBalanceException(null)).when(utilisateurService).effectuerRetrait(any(UtilisateurDTO.class), any(BigDecimal.class));
	        ModelMap model = new ModelMap();
	        ResultActions resultActions = mockMvc.perform(
	                MockMvcRequestBuilders.post("/effectuerRetrait")
	                        .param("montant", montant.toString())
	                        .with(user("utilisateur@example.com").password("password"))
	        );
	        resultActions
	                .andExpect(MockMvcResultMatchers.status().isForbidden());
	        verify(utilisateurService, never()).effectuerRetrait(any(UtilisateurDTO.class), any(BigDecimal.class));
	    }
	    
	    @Test
	    @WithMockUser
	    public void testEffectuerVirement_Success() throws Exception {
	        UtilisateurDTO utilisateurActuelDTO = new UtilisateurDTO();
	        utilisateurActuelDTO.setAdresseEmail("utilisateur@example.com");
	        UtilisateurDTO beneficiaireDTO = new UtilisateurDTO();
	        beneficiaireDTO.setAdresseEmail("beneficiaire@example.com");
	        BigDecimal montant = new BigDecimal("50.00");
	        when(authentication.getPrincipal()).thenReturn(new User("utilisateur@example.com", "", Collections.emptyList()));
	        when(utilisateurService.findByAdresseEmailDTO("utilisateur@example.com")).thenReturn(utilisateurActuelDTO);
	        when(utilisateurService.findByAdresseEmailDTO("beneficiaire@example.com")).thenReturn(beneficiaireDTO);
	        ModelMap model = new ModelMap();
	        String viewName = utilisateurController.effectuerVirement("beneficiaire@example.com", montant," ", model, authentication);
	        verify(utilisateurService, times(1)).effectuerVirement("utilisateur@example.com", "beneficiaire@example.com", montant, " ");
	        assert model.containsAttribute("success");
	        assert "effectuerVirement".equals(viewName);
	    }

	    @Test
	    @WithMockUser
	    public void testEffectuerVirement_InsufficientBalance() throws Exception {
	        BigDecimal montant = new BigDecimal("100.00");
	        UserDetails userDetails = new User("utilisateur@example.com", "", Collections.emptyList());
	        when(authentication.getPrincipal()).thenReturn(userDetails);
	        when(utilisateurService.findByAdresseEmailDTO(userDetails.getUsername())).thenReturn(new UtilisateurDTO()); 
	        doThrow(new InsufficientBalanceException(null)).when(utilisateurService).
	        effectuerVirement(anyString(), anyString(), any(BigDecimal.class),  anyString());
	        ModelMap model = new ModelMap();
	        ResultActions resultActions = mockMvc.perform(
	                MockMvcRequestBuilders.post("/effectuerVirement")
	                        .param("adresseEmailBeneficiaire", "beneficiaire@example.com")
	                        .param("montant", montant.toString())
	                        .with(user("utilisateur@example.com").password("password"))
	        );
	        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden()) ;
	        verify(utilisateurService, never()).effectuerVirement(anyString(), anyString(), any(BigDecimal.class),  anyString());
	    }
	    
	    @Test
	    public void testEnregistrerUtilisateur_Success() throws Exception {
	        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
	        utilisateurDTO.setAdresseEmail("nouvel_utilisateur@example.com");
	        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	        String viewName = utilisateurController.enregistrerUtilisateur(utilisateurDTO, redirectAttributes);
	        verify(utilisateurService, times(1)).addUtilisateur(utilisateurDTO);
	        verify(redirectAttributes, times(1)).addFlashAttribute("success", "L'utilisateur a été enregistré avec succès.");
	        assert "redirect:/enregistrerUtilisateur".equals(viewName);
	    }

	    @Test
	    public void testEnregistrerUtilisateur_EmailExists() throws Exception {
	        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
	        utilisateurDTO.setAdresseEmail("utilisateur_existant@example.com");
	        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	        doThrow(new EmailExistsException("Adresse email déjà existante")).when(utilisateurService).addUtilisateur(utilisateurDTO);
	        String viewName = utilisateurController.enregistrerUtilisateur(utilisateurDTO, redirectAttributes);
	        verify(utilisateurService, times(1)).addUtilisateur(utilisateurDTO);
	        verify(redirectAttributes, times(1)).addFlashAttribute("error", "Adresse email déjà existante");
	        assert "redirect:/enregistrerUtilisateur".equals(viewName);
	    }
	}

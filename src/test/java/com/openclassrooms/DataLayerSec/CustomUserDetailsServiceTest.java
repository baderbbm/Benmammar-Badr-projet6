package com.openclassrooms.DataLayerSec;

import com.openclassrooms.DataLayerSec.config.CustomUserDetailsService;
import com.openclassrooms.DataLayerSec.model.Utilisateur;
import com.openclassrooms.DataLayerSec.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService userDetailsService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new CustomUserDetailsService(utilisateurRepository);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        String email = "test@example.com";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setAdresseEmail(email);
        utilisateur.setMotDePasse("password"); 
        when(utilisateurRepository.findByAdresseEmail(email)).thenReturn(utilisateur);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.isEnabled());
        verify(utilisateurRepository, times(1)).findByAdresseEmail(email);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String email = "nonexistent@example.com";
        when(utilisateurRepository.findByAdresseEmail(email)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
        verify(utilisateurRepository, times(1)).findByAdresseEmail(email);
    }
}

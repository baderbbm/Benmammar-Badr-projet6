package com.openclassrooms.DataLayerSec;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.openclassrooms.DataLayerSec.service.UtilisateurService;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class DataLayerSecApplication implements CommandLineRunner {
 
	@Autowired
	private UtilisateurService utilisateurService;

	
	public static void main(String[] args) {
		SpringApplication.run(DataLayerSecApplication.class, args);
	}
 
	@Override
	@Transactional
	public void run(String... args) throws Exception {	
					
	}
}

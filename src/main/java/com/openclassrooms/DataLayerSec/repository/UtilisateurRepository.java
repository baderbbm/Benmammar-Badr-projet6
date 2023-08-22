package com.openclassrooms.DataLayerSec.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.openclassrooms.DataLayerSec.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {
		
	// requêtes dérivées
	public Iterable<Utilisateur> findByNom(String name);
	
	public Utilisateur findByAdresseEmail(String AdresseEmail);

	public Utilisateur findByUtilisateurId (int UtilisateurId);
	
	// requêtes natives SQL
	@Query(value = "SELECT * FROM utilisateur WHERE nom = :nom", nativeQuery = true)
	public Iterable<Utilisateur> findByNameNative(@Param("nom") String nom);
	
	// requêtes JPQL 
	@Query("FROM Utilisateur WHERE nom = ?1")
    public Iterable<Utilisateur> findByNameJPQL(String name);
	
}

package com.openclassrooms.DataLayerSec.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Transfert;
import com.openclassrooms.DataLayerSec.model.Utilisateur;

@Repository
public interface TransfertRepository extends JpaRepository<Transfert, Integer> {
    List<Transfert> findByUtilisateurEmetteur(Utilisateur utilisateurEmetteur);
}


package com.openclassrooms.DataLayerSec.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.DataLayerSec.model.Operation;
import com.openclassrooms.DataLayerSec.model.Utilisateur;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
    List<Operation> findByUtilisateur(Utilisateur utilisateur);
}

package com.openclassrooms.DataLayerSec.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.DataLayerSec.model.Transfert;

@Repository
public interface TransfertRepository extends CrudRepository<Transfert, Integer> {
	
	
}
package com.openclassrooms.DataLayerSec.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.DataLayerSec.model.Operation;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Integer> {
	
	
}
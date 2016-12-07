package com.waldo.repo;

import org.springframework.data.repository.CrudRepository;

import com.waldo.model.ExifData;

/**
 * @author Suresh
 * 
 * 	Spring jpa data repository interface for ExifData model
 *
 */
public interface ExifDataRepo extends CrudRepository<ExifData, Integer>{

}

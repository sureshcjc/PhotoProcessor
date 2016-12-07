package com.waldo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waldo.model.ExifData;
import com.waldo.repo.ExifDataRepo;

/**
 * @author Suresh
 * 
 * 	Implementation of Exif DAO service
 *
 */
@Service
public class ExifDataServiceImpl implements ExifDataService {
	
	@Autowired
	ExifDataRepo repo;

	@Override
	public List<?> getAll() {
		//TODO: Add pagination
		return (ArrayList<ExifData>) repo.findAll();
	}

	@Override
	public ExifData getById(Integer id) {
		return repo.findOne(id);
	}

	@Override
	public ExifData save(ExifData obj) {
		return repo.save(obj);
	}

	@Override
	public void delete(Integer id) {
		repo.delete(id);
	}

}

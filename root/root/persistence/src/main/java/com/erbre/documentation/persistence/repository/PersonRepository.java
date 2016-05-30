package com.erbre.documentation.persistence.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.erbre.documentation.persistence.entity.PersonEntity;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<PersonEntity, Long> {
	
	List<PersonEntity> findByFirstName(String name);

}


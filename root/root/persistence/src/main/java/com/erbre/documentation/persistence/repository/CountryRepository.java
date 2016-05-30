package com.erbre.documentation.persistence.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.erbre.documentation.persistence.entity.CountryEntity;

@Repository
public interface CountryRepository extends PagingAndSortingRepository<CountryEntity, Long> {

	List<CountryEntity> findByName(String name);

	List<CountryEntity> findByCode(String code);

}

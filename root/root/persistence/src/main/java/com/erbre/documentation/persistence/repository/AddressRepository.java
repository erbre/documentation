package com.erbre.documentation.persistence.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.erbre.documentation.persistence.entity.AddressEntity;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {

	List<AddressEntity> findByCity(String name);


}
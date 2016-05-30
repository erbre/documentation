package com.erbre.documentation.persistence.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.data.repository.PagingAndSortingRepository;

public abstract class AbstractDozerCrudObjectManager<T, E, K extends Serializable, R extends PagingAndSortingRepository<E, K>>
		extends AbstractCrudObjectManager<T, E, K, R> {

	@Inject 
	protected Mapper mapper;

	@Override
	protected E dto2Entity(T o) {
		return mapper.map(o, getEntityClass());
	}

	@Override
	protected T entity2Dto(E o) {
		return mapper.map(o, getDtoClass());
	}

	protected abstract Class<T> getDtoClass();

	protected abstract Class<E> getEntityClass();

}

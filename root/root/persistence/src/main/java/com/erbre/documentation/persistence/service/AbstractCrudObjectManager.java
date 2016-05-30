package com.erbre.documentation.persistence.service;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
@Transactional
public abstract class AbstractCrudObjectManager<T, E, K extends Serializable, R extends PagingAndSortingRepository<E, K>>
		implements CrudObjectManager<T, K> {

	@Inject
	protected R repository;

	@Override
	public Iterable<T> findAll(Sort sort) {
		return entity2Dto(repository.findAll(sort));
	}

	@Override
	public Iterable<T> save(Iterable<T> list) {
		Iterable<E> entities = dto2Entity(list);
		Iterable<E> entities2 = repository.save(entities);
		Iterable<T> dtos = entity2Dto(entities2);
		return dtos;
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		Page<E> pageE = repository.findAll(pageable);
		Page<T> page = entity2Dto(pageE);
		return page;
	}

	@Override
	public T findOne(K id) {
		return entity2Dto(repository.findOne(id));
	}

	@Override
	public T read(T id) {
		K key = keyFromDto(id);
		return entity2Dto(repository.findOne(key));
	}

	@Override
	public boolean exists(K id) {
		return repository.exists(id);
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public void delete(K id) {
		repository.delete(id);
	}

	@Override
	public void delete(T o) {
		E entity = dto2Entity(o);
		repository.delete(entity);
	}

	@Override
	public void delete(Iterable<T> entities) {
		repository.delete(dto2Entity(entities));
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}

	@Override
	public T save(T o) {
		E entity = dto2Entity(o);
		E entity2 = repository.save(entity);
		return entity2Dto(entity2);
	}

	@Override
	public Iterable<T> findAll(Iterable<K> ids) {
		Iterable<E> list = repository.findAll(ids);
		return entity2Dto(list);
	}

	@Override
	public Iterable<T> findAll() {
		Iterable<E> list = repository.findAll();
		return entity2Dto(list);
	}

	protected Iterable<T> entity2Dto(Iterable<E> list) {
		ArrayList<T> retour = new ArrayList<>();
		if (list != null) {
			list.forEach((e) -> retour.add(entity2Dto(e)));
		}
		return retour;
	}

	protected Iterable<E> dto2Entity(Iterable<T> list) {
		ArrayList<E> retour = new ArrayList<>();
		if (list != null) {
			list.forEach((e) -> retour.add(dto2Entity(e)));
		}
		return retour;
	}

	protected abstract E dto2Entity(T o);

	protected abstract T entity2Dto(E o);

	protected Page<T> entity2Dto(Page<E> o) {
		Page<T> page = o.map(this::entity2Dto);
		return page;
	}

	protected abstract K keyFromDto(T o);

}

package com.erbre.documentation.persistence.service;

import java.io.Serializable;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Transactional
public interface CrudObjectManager<T, K extends Serializable> {

	public T save(T o);

	public Iterable<T> findAll();

	Iterable<T> findAll(Iterable<K> ids);

	Iterable<T> findAll(Sort sort);

	Iterable<T> save(Iterable<T> list);

	T findOne(K id);

	boolean exists(K id);

	Page<T> findAll(Pageable pageable);

	long count();

	public void delete(K id);

	public void delete(T id);

	public T read(T id);

	void delete(Iterable<T> entities);

	void deleteAll();

}

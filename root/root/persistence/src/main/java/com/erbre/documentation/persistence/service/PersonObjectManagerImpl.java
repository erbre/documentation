package com.erbre.documentation.persistence.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.persistence.entity.PersonEntity;
import com.erbre.documentation.persistence.repository.PersonRepository;

@Service
@Transactional
public class PersonObjectManagerImpl extends AbstractDozerCrudObjectManager<PersonType, PersonEntity, Long, PersonRepository>
		implements PersonObjectManager {

	@Override
	protected Class<PersonType> getDtoClass() {
		return PersonType.class;
	}

	@Override
	protected Class<PersonEntity> getEntityClass() {
		return PersonEntity.class;
	}

	@Override
	protected Long keyFromDto(PersonType o) {
		return o.getId();
	}

}

package com.erbre.documentation.persistence.service;

import javax.transaction.Transactional;

import com.erbre.documentation.model.PersonType;
@Transactional
public interface PersonObjectManager extends CrudObjectManager<PersonType, Long> {

}

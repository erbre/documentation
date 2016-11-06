package com.erbre.documentation.service.impl;

import java.util.Random;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

import org.dozer.Mapper;

import com.erbre.documentation.model.PersonListType;
import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.persistence.service.PersonObjectManager;
import com.erbre.documentation.service.contrat.PersonServiceRest;

public class PersonServiceRestImpl implements PersonServiceRest {

    @Inject
    protected PersonObjectManager om;

    @Inject
    protected Mapper mapper;

    @Override
    public JAXBElement<PersonListType> findAll() {

        Iterable<PersonType> result = om.findAll();
         PersonListType list = new com.erbre.documentation.model.ObjectFactory().createPersonListType();
        result.forEach(p -> list.getItems().add(p));
        return new com.erbre.documentation.model.ObjectFactory().createPersonList(list);
    }

    @Override
    public JAXBElement<PersonType> find(Long id) {
        PersonType result = om.findOne(id);
        om.findOne(id);
        if (new Random().nextBoolean()) {
            om.findAll();
        }
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @Override
    public JAXBElement<PersonType> find(@QueryParam("person") JAXBElement<PersonType> person) {
        PersonType result = om.save(person.getValue());
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @Override
    public JAXBElement<PersonType> create(@QueryParam("person") JAXBElement<PersonType> person) {
        PersonType result = om.save(person.getValue());
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @Override
    public JAXBElement<PersonType> update(@QueryParam("person") JAXBElement<PersonType> person) {
        PersonType result = om.save(person.getValue());
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @Override
    public void delete(Long id) {
        om.delete(id);
    }

}

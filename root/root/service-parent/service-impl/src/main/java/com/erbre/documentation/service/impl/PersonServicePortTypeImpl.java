package com.erbre.documentation.service.impl;

import javax.inject.Inject;
import javax.jws.WebService;

import org.dozer.Mapper;

import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.persistence.service.PersonObjectManager;
import com.erbre.documentation.person_service.v1.PersonServicePortType;
import com.erbre.documentation.person_service_message.v1.FindAllRequestType;
import com.erbre.documentation.person_service_message.v1.FindAllResponseType;
import com.erbre.documentation.person_service_message.v1.ObjectFactory;
import com.erbre.documentation.v1.PersonListType;

@WebService
public class PersonServicePortTypeImpl implements PersonServicePortType {

  
    @Inject
    protected PersonObjectManager om;

    @Inject
    protected Mapper mapper;

    @Override
    public FindAllResponseType findAllOperation(FindAllRequestType parameters) {
        ObjectFactory serviceOf = new ObjectFactory();
        com.erbre.documentation.model.ObjectFactory modelOf = new com.erbre.documentation.model.ObjectFactory();

        FindAllResponseType retour = serviceOf.createFindAllResponseType();
        Iterable<PersonType> result = om.findAll();
        com.erbre.documentation.model.PersonListType model = modelOf.createPersonListType();
        result.forEach(p -> model.getItems().add(p));

        PersonListType list = mapper.map(model, PersonListType.class);
        retour.setResult(list);
        return retour;
    }

}

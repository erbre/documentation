package com.erbre.documentation.service.impl;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

import org.dozer.Mapper;

import com.erbre.documentation.model.PersonListType;
import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.persistence.service.PersonObjectManager;

@Path("/persons")
@Produces({ "application/xml", "application/json" })
public class PersonServiceRestImpl {

    @Inject
    protected PersonObjectManager om;

    @Inject
    protected Mapper mapper;

    @GET
    public JAXBElement<PersonListType> findAll() {

        Iterable<PersonType> result = om.findAll();
        PersonListType list = new com.erbre.documentation.model.ObjectFactory().createPersonListType();
        result.forEach(p -> list.getItems().add(p));
        return new com.erbre.documentation.model.ObjectFactory().createPersonList(list);
    }

    @GET
    @PathParam("id")
    public JAXBElement<PersonType> find(Long id) {
        PersonType result = om.findOne(id);
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @GET
    @PathParam("id")
    public JAXBElement<PersonType> find(@QueryParam("person") JAXBElement<PersonType> person) {
        PersonType result = om.save(person.getValue());
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @POST
    public JAXBElement<PersonType> create(@QueryParam("person") JAXBElement<PersonType> person) {
        PersonType result = om.save(person.getValue());
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @PUT
    @PathParam("id")
    public JAXBElement<PersonType> update(@QueryParam("person") JAXBElement<PersonType> person) {
        PersonType result = om.save(person.getValue());
        return new com.erbre.documentation.model.ObjectFactory().createPerson(result);
    }

    @DELETE
    @PathParam("id")
    public void delete(Long id) {
        om.delete(id);
    }

}

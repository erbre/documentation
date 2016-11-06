package com.erbre.documentation.web.controller.service;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.erbre.documentation.model.PersonListType;
import com.erbre.documentation.model.PersonType;
import com.erbre.documentation.service.contrat.PersonServiceRest;

@Controller
@RequestMapping("/persons")
public class PersonController {

    @Inject
    protected PersonServiceRest service;

    @GET
    public JAXBElement<PersonListType> findAll() {
        return service.findAll();
    }

    @GET
    @PathParam("id")
    public JAXBElement<PersonType> find(Long id) {
        return service.find(id);
    }

    @GET
    @PathParam("id")
    public JAXBElement<PersonType> find(@QueryParam("person") JAXBElement<PersonType> person) {
        return service.find(person);
    }

    @POST
    public JAXBElement<PersonType> create(@QueryParam("person") JAXBElement<PersonType> person) {
        return service.create(person);
    }

    @PUT
    @PathParam("id")
    public JAXBElement<PersonType> update(@QueryParam("person") JAXBElement<PersonType> person) {
        return service.update(person);
    }

    @DELETE
    @PathParam("id")
    public void delete(Long id) {
        service.delete(id);
    }
}

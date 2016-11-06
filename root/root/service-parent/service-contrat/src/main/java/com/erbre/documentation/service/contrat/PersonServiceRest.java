package com.erbre.documentation.service.contrat;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

import com.erbre.documentation.model.PersonListType;
import com.erbre.documentation.model.PersonType;
@Path("/persons")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public interface PersonServiceRest {
    @GET
    public JAXBElement<PersonListType> findAll();

    @GET
    @Path("{id}")
    public JAXBElement<PersonType> find(@PathParam("id") Long id);

    @GET
    public JAXBElement<PersonType> find(@QueryParam("person") JAXBElement<PersonType> person);

    @POST
    public JAXBElement<PersonType> create(@QueryParam("person") JAXBElement<PersonType> person);

    @PUT
    public JAXBElement<PersonType> update(@QueryParam("person") JAXBElement<PersonType> person);

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id);
}

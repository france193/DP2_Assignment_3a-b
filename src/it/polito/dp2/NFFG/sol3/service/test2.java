package it.polito.dp2.NFFG.sol3.service;

import it.polito.dp2.NFFG.sol3.service.models.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by FLDeviOS on 12/01/2017.
 */
public class test2 {

    Nffgs myNffgs;

    public test2() {
    }

    // ALL NFFGS
    @GET
    @Path("/nffgs")
    @Produces(MediaType.APPLICATION_XML)
    public List<Nffg> getNffgs() {
        return myNffgs.getNffg();
    }

    /*
    // A NFFG
    @GET
    @Path("/nffgs/{nffg_id}")
    @Produces(MediaType.APPLICATION_XML)
    public Nffg getUser(@PathParam("nffg_id") int nffg_id){

    }

    // NODES OF A NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes")
    @Produces(MediaType.APPLICATION_XML)
    public Nodes getNode(@PathParam("nffg_id") int nffg_id){

    }

    // A NODE OF AN NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes/{node_id}")
    @Produces(MediaType.APPLICATION_XML)
    public Nodes getNode(@PathParam("nffg_id") int nffg_id,
                         @PathParam("node_id") int node_id){

    }

    // ALL LINKS OF A NODE OF AN NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes/{node_id}/links")
    @Produces(MediaType.APPLICATION_XML)
    public Nodes getNode(@PathParam("nffg_id") int nffg_id,
                         @PathParam("node_id") int node_id){

    }

    // A LINK OF A NODE OF AN NFFG
    @GET
    @Path("/nffgs/{nffg_id}/nodes/{node_id}/links/{link_id}")
    @Produces(MediaType.APPLICATION_XML)
    public Nodes getNode(@PathParam("nffg_id") int nffg_id,
                         @PathParam("node_id") int node_id,
                         @PathParam("link_id") int link_id){

    }

    */
}

package it.polito.dp2.NFFG.sol3.service;

/**
 * Created by FLDeviOS on 12/01/2017.
 */

import com.wordnik.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Resource class hosted at the URI relative path "/test"
 */
@Path("/test")
@Api(value = "/test")
public class test {
    @GET
    @Produces("text/plain")
    public String getTime() {
        return new Date().toString();
    }
}
package controllers;

import io.dropwizard.jersey.sessions.Session;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// For a Java class to be eligible to receive ANY requests
// it must be annotated with at least @Path
@Path("")
public class NetIDController {
    @GET
    @Path("/netid")
    @Produces(MediaType.TEXT_PLAIN)
    public String returnNetID(){
        return "ih265";
    }
}

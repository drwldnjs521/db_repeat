package de.hhu.cs.dbs.propra.presentation.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.hhu.cs.dbs.propra.application.configurations.SecurityContext;
import de.hhu.cs.dbs.propra.application.exceptions.ResourceNotFoundException;
import de.hhu.cs.dbs.propra.domain.model.Nutzer;
import de.hhu.cs.dbs.propra.domain.model.repo.NutzerRepository;
import org.glassfish.jersey.media.multipart.FormDataParam;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;
import java.util.List;


@Path("/nutzer")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class NutzerController {


    @Inject
    private NutzerRepository nutzerRepo;

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    @GET // GET http://localhost:8080/nutzer
    public Response findNutzer(@QueryParam("email") String email) throws ResourceNotFoundException {
        String jsonInString = null;
        if (email == null) {
            try {

                jsonInString = mapper.writeValueAsString(nutzerRepo.fetchAll());

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.status(200).entity(jsonInString).build();
        } else {
            List<Nutzer> nutzerList = nutzerRepo.findByEmail(email);
            if (nutzerList.isEmpty()) throw new ResourceNotFoundException();
            try {
                jsonInString = mapper.writeValueAsString(nutzerList);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.status(200).entity(jsonInString).build();
        }
    }


    @POST// POST http://localhost:8080/nutzer
    public Response saveNutzer(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("benutzername") String name) {

        Nutzer nutzer = new Nutzer(name);
        nutzer.setEmail(email);
        nutzer.setPasswort(passwort);
        nutzer.setAdresseId(4);
        nutzerRepo.save(nutzer);

        return Response.status(Response.Status.CREATED).header("Location", uriInfo.getBaseUriBuilder()).build();

    }


}










package de.hhu.cs.dbs.propra.presentation.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.hhu.cs.dbs.propra.domain.model.Premiumnutzer;
import de.hhu.cs.dbs.propra.domain.model.repo.PremiumNutzerRepo;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Path("/premiumnutzer")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class PremiumNutzerController {


    @Inject
    private PremiumNutzerRepo premiumNutzerRepo;

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;


    @GET // GET http://localhost:8080/premiumnutzer
    public Response findPremiumNutzerBy(@QueryParam("abgelaufen") boolean abgelaufen) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String jsonInString = null;
        Premiumnutzer pn = new Premiumnutzer("ZsBtLZkEDXy", "2020-06-27 12:22:21");
        try {
            jsonInString = mapper.writeValueAsString(pn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity(jsonInString).build();
        /*
        if (abgelaufen != true && abgelaufen != false) {
            try {
                jsonInString = mapper.writeValueAsString(premiumNutzerRepo.fetchAll());

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.status(200).entity(jsonInString).build();

        } else {
            try {
                jsonInString = mapper.writeValueAsString(premiumNutzerRepo.fetchByAbgelaufen(abgelaufen));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.status(200).entity(jsonInString).build();

        }

         */

    }

}
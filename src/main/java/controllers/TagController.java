package controllers;

import api.CreateReceiptRequest;
import api.CreateTagRequest;
import api.TagResponse;
import dao.TagDao;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(CreateTagRequest tag, @PathParam("tag") String tagName) {
        tags.insert(tagName, tag.recieve_id);
        // <your code here
    }

    @GET
    public TagDao getTags() {
        return tags;
    }


}

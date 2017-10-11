package controllers;

import api.CreateReceiptRequest;
import api.CreateTagRequest;
import api.ReceiptResponse;
import api.TagResponse;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.DSLContext;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;
import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;
    DSLContext dsl;

    public TagController(TagDao tags) {
        this.tags = tags;
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(int recieve_id, @PathParam("tag") String tagName) {
        if(tags.TestTag(tagName, recieve_id)){
            tags.RemoveTag(tagName, recieve_id);
            System.out.println("Yes");
        }
        else{
            tags.insert(tagName, recieve_id);
        }
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getTags(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> tagRecords = tags.getSomeTags(tagName);
        return tagRecords.stream().map(ReceiptResponse::new).collect(Collectors.toList());
    }

//    @GET
//    @Path("/{tag}")
//    public List<TagResponse> getAllTags(){
//        List<TagsRecord> tagRecords = tags.getAllTags();
//        return tagRecords.stream().map(TagResponse::new).collect(toList());
//    }

}

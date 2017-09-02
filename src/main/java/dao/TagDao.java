package dao;

import api.TagResponse;
import generated.tables.records.TagsRecord;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public boolean TestTag(String tagName, int recieve_id){
        TagsRecord record = dsl.selectFrom(TAGS).where(TAGS.RECIEVE_ID.eq(recieve_id)).and(TAGS.TAGNAME.eq(tagName)).fetchOne();
        if(record==null){
            return false;
        }
        else {
            return true;
        }
    }

    public void RemoveTag(String tagName, int recieve_id){
        System.out.println(dsl.selectFrom(TAGS).where(TAGS.ID.in(recieve_id)).fetch());
        dsl.delete(TAGS).where(TAGS.RECIEVE_ID.eq(recieve_id)).and(TAGS.TAGNAME.eq(tagName)).execute();
        System.out.println(dsl.selectFrom(TAGS).where(TAGS.ID.in(recieve_id)).fetch());
    }

    public int insert(String tagName, int recieve_id) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.RECIEVE_ID, TAGS.TAGNAME)
                .values(recieve_id, tagName)
                .returning(TAGS.ID, TAGS.RECIEVE_ID, TAGS.TAGNAME)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");

        return tagsRecord.getId();

    }

    public List<ReceiptsRecord> getSomeTags(String tagname) {

        List <Integer> receiptid = dsl.selectFrom(TAGS).where(TAGS.TAGNAME.eq(tagname)).fetch()
                .stream().map(x->x.getRecieveId()).collect(Collectors.toList());
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.in(receiptid)).fetch();
    }
}
